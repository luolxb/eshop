package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.common.constant.OrderConstant;
import com.soubao.common.constant.ShopConstant;
import com.soubao.entity.*;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.*;
import com.soubao.dao.CartMapper;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-08-29
 */
@Slf4j
@Service("cartService")
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private SpecGoodsPriceService specGoodsPriceService;
    @Autowired
    private UserService userService;
    @Autowired
    private ShopConstant shopConstant;
    @Autowired
    private ConfigService configService;
    @Autowired
    private GoodsCategoryService goodsCategoryService;
    @Autowired
    private CouponService couponService;
    @Autowired
    private PromOrderService promOrderService;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CouponListService couponListService;
    @Autowired
    private StockLogService stockLogService;
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Value("${security.salt}")
    private String salt;

    @Override
    public boolean addCart(User user, Cart cart) {
        Goods goods = goodsService.getById(cart.getGoodsId());
        SpecGoodsPrice specGoodsPrice;
        if (cart.getItemId() != null) {
            specGoodsPrice = specGoodsPriceService.getById(cart.getItemId());
        } else {
            specGoodsPrice = specGoodsPriceService.getOne((new QueryWrapper<SpecGoodsPrice>()
                    .eq("goods_id", cart.getGoodsId()).orderByDesc("store_count").last("limit 1")));
        }
        if (specGoodsPrice == null) {
            int specGoodsPriceCount = specGoodsPriceService.count((new QueryWrapper<SpecGoodsPrice>().select("item_id")
                    .eq("goods_id", cart.getGoodsId())));
            if (specGoodsPriceCount > 0) {
                throw new ShopException(ResultEnum.ADD_CART_NO_SPEC);
            }
        }
        if (goods == null) {
            throw new ShopException(ResultEnum.ADD_CART_NO_GOODS);
        }
        int userCartCount = count((new QueryWrapper<Cart>()).select("id").eq("user_id", user.getUserId()));
        if (userCartCount >= shopConstant.getCartMostNum()) {
            throw new ShopException(ResultEnum.ADD_CART_TOO_MANY);
        }
        GoodsSku goodsSku = new GoodsSku(goods, specGoodsPrice);
        // 查询购物车是否已经存在这商品
        Cart hadCart;
        hadCart = getOne((new QueryWrapper<Cart>()).eq("user_id", user.getUserId())
                .eq("goods_id", cart.getGoodsId()).eq("spec_key", goodsSku.getSpecKey()));
        if (hadCart != null) {
            hadCart.setGoodsPrice(goodsSku.getShopPrice());
            hadCart.setMemberGoodsPrice(goodsSku.getShopPrice());
            hadCart.setGoodsNum(cart.getGoodsNum() + hadCart.getGoodsNum());
            if (hadCart.getGoodsNum() > 200) {
                hadCart.setGoodsNum(200);
            }
            if (hadCart.getGoodsNum() > goodsSku.getStoreCount()) {
                throw new ShopException(ResultEnum.NOT_ENOUGH_STOCK.getCode(), "商品库存不足，剩余" + goodsSku.getStoreCount() +
                        ",当前购物车已有" + userCartCount + "件");
            }
            return saveOrUpdate(hadCart);
        } else {
            if (cart.getGoodsNum() > goodsSku.getStoreCount()) {
                throw new ShopException(ResultEnum.NOT_ENOUGH_STOCK.getCode(), "商品库存不足，剩余" + goodsSku.getStoreCount());
            }
            cart.setUserId(user.getUserId());
            cart.setGoodsSn(goods.getGoodsSn());
            cart.setGoodsName(goods.getGoodsName());
            cart.setMarketPrice(goods.getMarketPrice());
            cart.setGoodsPrice(goodsSku.getShopPrice());
            cart.setMemberGoodsPrice(goodsSku.getShopPrice());
            cart.setAddTime(System.currentTimeMillis() / 1000);
            cart.setPromType(goodsSku.getPromType());
            cart.setPromId(goodsSku.getPromId());
            cart.setStoreId(goods.getStoreId());
            cart.setShopId(0);
            cart.setSgsId(0);
            cart.setItemId(goodsSku.getItemId());
            cart.setSpecKey(goodsSku.getSpecKey());
            cart.setSpecKeyName(goodsSku.getSpecKeyName());
            cart.setSku(goodsSku.getSpecKeyName());
            return saveOrUpdate(cart);
        }
    }

    @Override
    public boolean updateCartList(User user, List<Cart> updateCartList) {
        Set<Integer> cartIds = updateCartList.stream().map(Cart::getId).collect(Collectors.toSet());
        if (cartIds.size() == 0) {
            return false;
        }
        List<Cart> cartList = this.list((new QueryWrapper<Cart>()).in("id", cartIds)
                .or().in("combination_group_id", cartIds).eq("user_id", user.getUserId()));
        if (cartList.size() == 0) {
            return false;
        }
        Set<Integer> goodsIds = new HashSet<>();
        Set<Long> itemIds = new HashSet<>();
        for (Cart cart : cartList) {
            goodsIds.add(cart.getGoodsId());
            if (cart.getItemId() > 0) itemIds.add(cart.getItemId());
        }
        Map<Integer, Goods> goodsMap = goodsService.list((new QueryWrapper<Goods>()).in("goods_id", goodsIds))
                .stream().collect(Collectors.toMap(Goods::getGoodsId, goods -> goods));
        Map<Long, SpecGoodsPrice> itemMap = new HashMap<>();
        if (itemIds.size() > 0) {
            itemMap = specGoodsPriceService.list((new QueryWrapper<SpecGoodsPrice>()).in("item_id", itemIds))
                    .stream().collect(Collectors.toMap(SpecGoodsPrice::getItemId, item -> item));
        }
        Map<Integer, Cart> updateCartMap = updateCartList.stream().collect(Collectors.toMap(Cart::getId, cart -> cart));
        for (Cart cart : cartList) {
            if (updateCartMap.containsKey(cart.getId())) {
                Cart updateCart = updateCartMap.get(cart.getId());
                if (cart.getPromType() == 7 && cart.getCombinationGroupId() != 0) {
                    //搭配购，副商品,不容许更改
                    continue;
                }
                //如果是更改数量
                if (updateCart.getGoodsNum() != null && updateCart.getGoodsNum() > 0
                        && !cart.getGoodsNum().equals(updateCart.getGoodsNum())) {
                    int limitNum = new GoodsSku(goodsMap.get(cart.getGoodsId()), itemMap.getOrDefault(cart.getItemId(), null)).getLimitNum();//商品库存
                    if (limitNum == 0) {
                        throw new ShopException(ResultEnum.NOT_ENOUGH_STOCK);
                    }
                    if (cart.getPromType() == 7) {
                        //搭配购，主商品更改套餐数量
                        Integer cartGoodsSumNum = getCartGoodsSkuNum(cartList, cart);
                        limitNum = limitNum - cartGoodsSumNum;
                    }
                    if (updateCart.getGoodsNum() > limitNum) {
                        throw new ShopException(ResultEnum.NOT_ENOUGH_STOCK.getCode(),
                                cart.getGoodsName() + cart.getSpecKeyName() + "商品数量不能大于" + limitNum);
                    }
                    cart.setGoodsNum(updateCart.getGoodsNum());
                    if (cart.getGoodsNum() > 200) {
                        cart.setGoodsNum(200);
                    }
                    if (cart.getPromType() == 7) {
                        updateGoodsNumByMasterCart(cartList, cart, goodsMap, itemMap);
                    }
                }
                //如果是更改选中状态
                if (updateCart.getSelected() != null && !cart.getSelected().equals(updateCart.getSelected())) {
                    if (cart.getPromType() == 7) {
                        //主商品更改选中状态
                        cart.setSelected(updateCart.getSelected());
                        updateSelectedByMasterCart(cartList, cart);
                    } else {
                        cart.setSelected(updateCart.getSelected());
                    }
                }
            }
        }
        if (cartList.size() > 0) {
            return updateBatchById(cartList);
        }
        return false;
    }

    private void updateSelectedByMasterCart(List<Cart> cartList, Cart masterCart) {
        for (Cart cart : cartList) {
            if (cart.getCombinationGroupId().equals(masterCart.getId())) {
                cart.setSelected(masterCart.getSelected());
            }
        }
    }

    private void updateGoodsNumByMasterCart(List<Cart> cartList, Cart masterCart, Map<Integer, Goods> goodsMap,
                                            Map<Long, SpecGoodsPrice> itemMap) {
        for (Cart cart : cartList) {
            if (cart.getCombinationGroupId().equals(masterCart.getId())) {
                int limitNum = new GoodsSku(goodsMap.get(cart.getGoodsId()), itemMap.getOrDefault(cart.getItemId(), null)).getLimitNum();//商品库存
                if (limitNum == 0) {
                    throw new ShopException(ResultEnum.NOT_ENOUGH_STOCK);
                }
                if (masterCart.getGoodsNum() > limitNum) {
                    throw new ShopException(ResultEnum.NOT_ENOUGH_STOCK.getCode(),
                            cart.getGoodsName() + cart.getSpecKeyName() + "商品数量不能大于" + limitNum);
                }
                cart.setGoodsNum(masterCart.getGoodsNum());
            }
        }
    }

    private Integer getCartGoodsSkuNum(List<Cart> cartList, Cart cart) {
        int goodsNumSum = 0;
        for (Cart cartItem : cartList) {
            //自己不统计，同个套餐的不统计
            if ((!cartItem.getId().equals(cart.getId())
                    || !cartItem.getCombinationGroupId().equals(cart.getId()))
                    && cartItem.getGoodsId().equals(cart.getGoodsId())
                    && cartItem.getItemId().equals(cart.getItemId())) {
                goodsNumSum += cart.getGoodsNum();
            }
        }
        return goodsNumSum;
    }

    @Override
    public void checkPreOrder(User user, Order templateOrder) {
        if (null != templateOrder.getIntegral() && templateOrder.getIntegral() > 0) {
            if (StringUtils.isEmpty(user.getPassword())) {
                throw new ShopException(ResultEnum.NO_PAY_PWD_SETTING);
            }
            if (StringUtils.isEmpty(templateOrder.getPayPwd())) {
                throw new ShopException(ResultEnum.NO_PAY_PWD);
            }
            if (!passwordEncoder.matches(templateOrder.getPayPwd(), user.getPaypwd().replace(salt, ""))) {
                throw new ShopException(ResultEnum.INVALID_PAY_PWD);
            }
        }
    }

    private Order preInsertOrder(User user, Order masterOrder, Order templateOrder) {
        for (Order order : masterOrder.getOrderList()) {
            order.setUserId(user.getUserId());
            order.setEmail(user.getEmail());
            order.setAddTime(System.currentTimeMillis() / 1000);
            if (masterOrder.getUserAddress() != null) {
                order.setConsignee(masterOrder.getUserAddress().getConsignee());
                order.setProvince(masterOrder.getUserAddress().getProvince());
                order.setCity(masterOrder.getUserAddress().getCity());
                order.setDistrict(masterOrder.getUserAddress().getDistrict());
                order.setTwon(masterOrder.getUserAddress().getTwon());
                order.setAddress(masterOrder.getUserAddress().getAddress());
                order.setMobile(masterOrder.getUserAddress().getMobile());
                order.setZipcode(masterOrder.getUserAddress().getZipcode());
            }
            if (templateOrder.getUserNotes() != null) {
                order.setUserNote(templateOrder.getUserNotes().get(order.getStoreId()));
            }
            if (templateOrder.getInvoiceTitle() != null) {
                order.setInvoiceTitle(templateOrder.getInvoiceTitle());
            }
            if (templateOrder.getTaxpayer() != null) {
                order.setTaxpayer(templateOrder.getTaxpayer());
            }
            order.setPayName((order.getIntegral() != null && order.getIntegral() > 0) ? "积分兑换" : "");
            if (order.getOrderAmount().compareTo(BigDecimal.ZERO) == 0) {
                order.setPayStatus(OrderConstant.PAYED);
                order.setPayTime(System.currentTimeMillis() / 1000);
            } else {
                order.setPayStatus(OrderConstant.NOT_PAY);
            }
        }
        masterOrder.setUserAddress(null);//补充数据完成后，销毁，无用数据传递给order服务，数据过多会报feign错误
        return masterOrder;
    }

    @Override
    public Order getPreCreateOrder(User user, Order templateOrder) {
        this.checkPreOrder(user, templateOrder);
        Order masterOrder = this.getOrderByCart(user, templateOrder, true);
        preInsertOrder(user, masterOrder, templateOrder);
        return masterOrder;
    }

    @Override
    public Order addAndGetOrderByGoodsSku(User user, Order templateOrder, GoodsSku goodsSku, Integer goodsNum) {
        this.checkPreOrder(user, templateOrder);
        Order masterOrder = this.getOrderByGoodsSku(user, templateOrder, goodsSku, goodsNum, true);
        return preInsertOrder(user, masterOrder, templateOrder);
    }

    @Override
    public void checkOrderShoppingConfig(Order requestOrder) {
        if (requestOrder.getIntegral() != null && requestOrder.getIntegral() > 0) {
            int usePercentPoint = Integer.parseInt((String) configService.getConfigMap().get("shopping_point_use_percent"));
            if (usePercentPoint == 0) {
                throw new ShopException(ResultEnum.USE_POINT_ERROR);
            }
        }
    }

    @Override
    public List<Cart> sortByCombination(List<Cart> carts) {
        List<Cart> sortCartList = new ArrayList<>();
        Map<Integer, List<Cart>> cartMap = new HashMap<>();
        Iterator<Cart> it = carts.iterator();
        while (it.hasNext()) {
            Cart cart = it.next();
            if (cart.getCombinationGroupId() > 0) {
                if (cartMap.containsKey(cart.getCombinationGroupId())) {
                    cartMap.get(cart.getCombinationGroupId()).add(cart);
                } else {
                    List<Cart> newCart = new ArrayList<>();
                    newCart.add(cart);
                    cartMap.put(cart.getCombinationGroupId(), newCart);
                }
                it.remove();
            }
        }
        for (Cart cart : carts) {
            sortCartList.add(cart);
            if (cartMap.containsKey(cart.getId())) {
                sortCartList.addAll(cartMap.get(cart.getId()));
            }
        }
        return sortCartList;
    }

    @Override
    public void delete(List<Cart> carts) {
        Set<Integer> cartDelGroupIds = new HashSet<>();
        Set<Integer> cartDelId = new HashSet<>();
        for (Cart cart : carts) {
            if (cart.getPromType() == 7) {
                if (cart.getCombinationGroupId() == 0) {
                    //主商品
                    cartDelId.add(cart.getId());
                    cartDelGroupIds.add(cart.getId());
                }//副商品不做处理，否则有业务漏洞
            } else {
                cartDelId.add(cart.getId());
            }
        }
        QueryWrapper<Cart> deleteQueryWrapper = new QueryWrapper<>();
        if (cartDelId.size() == 0) {
            return;
        }
        deleteQueryWrapper.in("id", cartDelId);
        if (cartDelGroupIds.size() > 0) {
            deleteQueryWrapper.or().in("combination_group_id", cartDelGroupIds);
        }
        remove(deleteQueryWrapper);
    }

    @Override
    @Transactional
    @GlobalTransactional(name = "sb-create-order", rollbackFor = Exception.class)
    public Order placeAndGetOrder(Order preOrder) {
        Order masterOrder = orderService.addAndGetMasterOrder(preOrder);//订单入库
        stockLogService.stock(masterOrder.getOrderList(), false);//扣减库存
        couponListService.deductionMasterOrder(preOrder, masterOrder);//坑，订单记录没有优惠券id,只能订单入库后马上更新，不能用mq，也不能先扣除
        return masterOrder;
    }

    /**
     * 1)获取结算的购物车列表
     * 2)补全购物车列表是否配送数据
     * 3)补全购物车列表是否满足库存销售
     * 4)购物车列表转成订单商品数据
     * 5)订单商品数据生成订单列表（按店铺生成）
     * 6)订单列表计算运费
     * 7)订单列表使用订单优惠
     * 8)订单列表使用优惠券
     * 9)订单列表生成主订单数据（使用积分抵扣）
     * 10)主订单数据用积分抵扣
     *
     * @param user          购买者
     * @param templateOrder 订单模板
     * @param isInsert      是否入库的获取订单数据
     * @return order
     */
    @Override
    public Order getOrderByCart(User user, Order templateOrder, boolean isInsert) {
        List<Cart> queryCarts = this.list((new QueryWrapper<Cart>()).eq("user_id", user.getUserId()).eq("selected", 1));//(1)
        if (queryCarts.size() == 0) {
            throw new ShopException(ResultEnum.ADD_CART_NO_GOODS.getCode(), "购物车里没有商品");
        }
        List<Cart> carts = sortByCombination(queryCarts);
        Set<Integer> goodsIds = new HashSet<>();
        Set<Long> itemIds = new HashSet<>();
        for (Cart cart : carts) {
            goodsIds.add(cart.getGoodsId());
            if (cart.getItemId() > 0) itemIds.add(cart.getItemId());
        }
        List<Goods> goodsList = goodsService.list((new QueryWrapper<Goods>()).in("goods_id", goodsIds));//获取购物车的商品信息
//        UserAddress userAddress = userService.getUserAddress(templateOrder.getAddressId());//用户地址
        if (isInsert) {
//            templateOrder.setUserAddress(userAddress);
            Set<Integer> goodsCat3Ids = goodsList.stream().map(Goods::getCatId3).collect(Collectors.toSet());
            List<GoodsCategory> goodsCategoryList = goodsCategoryService.list((new QueryWrapper<GoodsCategory>()).select("id,commission").in("id", goodsCat3Ids));
            Map<Integer, GoodsCategory> goodsCategoryMap = goodsCategoryList.stream().collect(Collectors.toMap(GoodsCategory::getId, goodsCategory -> goodsCategory));
            for (Goods goods : goodsList) {
                goods.setCommission(goodsCategoryMap.get(goods.getCatId3()).getCommission());
            }
        }
//        for (Goods goods : goodsList) {
//            goods.setIsDelivery((goodsService.isNotShippingInRegion(goods, userAddress.getDistrict())) ? 1 : 0);
//        }
        Map<Integer, Goods> goodsListMap = goodsList.stream().collect(Collectors.toMap(Goods::getGoodsId, goods -> goods));
        Map<Long, SpecGoodsPrice> itemMap = new HashMap<>();
        if (itemIds.size() > 0) {
            itemMap = specGoodsPriceService.list(new QueryWrapper<SpecGoodsPrice>().in("item_id", itemIds))
                    .stream().collect(Collectors.toMap(SpecGoodsPrice::getItemId, item -> item));
        }
        for (Cart cart : carts) {
            GoodsSku goodsSku = new GoodsSku(goodsListMap.get(cart.getGoodsId()), itemMap.getOrDefault(cart.getItemId(), null));
            if (isInsert && cart.getGoodsNum() > goodsSku.getLimitNum()) {
                throw new ShopException(ResultEnum.NOT_ENOUGH_STOCK.getCode(),
                        cart.getGoodsName() + cart.getSpecKey() + "库存不足，剩余" + goodsSku.getLimitNum() + "件");
            }
            cart.setLimitNum(goodsSku.getLimitNum());//(2)
            cart.setIsDelivery(goodsListMap.get(cart.getGoodsId()).getIsDelivery());//(3)
        }
        List<OrderGoods> orderGoodsList = this.changeCartsToOrderGoods(carts, goodsListMap);//(4)
        List<Order> orderList = this.getOrderList(orderGoodsList);//(5)
//        this.delivery(orderList, goodsListMap, userAddress.getDistrict());//(6)
        this.userOrderPromotion(orderList);// (7)
        templateOrder.setOrderList(orderList);
        couponService.calculateCouponAmount(user, templateOrder);//（8）
        this.calculateMasterOrder(templateOrder);//(9)
        this.useIntegral(user, templateOrder);//(10)
        return templateOrder;
    }

    @Override
    public Order getOrderByGoodsSku(User user, Order templateOrder, GoodsSku goodsSku, Integer goodsNum, boolean isInsert) {
        this.checkOrderShoppingConfig(templateOrder);
        OrderGoods orderGoods = this.changeGoodsSkuToOrderGoods(goodsSku, goodsNum);
//        UserAddress userAddress = userService.getUserAddress(templateOrder.getAddressId());//用户地址
//        orderGoods.setIsDelivery((goodsService.isNotShippingInRegion(goodsSku, userAddress.getDistrict())) ? 1 : 0);
        int limitNum = goodsSku.getLimitNum();
        if (goodsNum > limitNum) {
            throw new ShopException(ResultEnum.NOT_ENOUGH_STOCK);
        }
        if (isInsert) {
            GoodsCategory goodsCategory = goodsCategoryService.getById(goodsSku.getCatId3());
            if(goodsCategory!=null) {
                orderGoods.setCommission(goodsCategory.getCommission());
            }{
                log.info(goodsSku.getGoodsId()+",没有佣金比例");
            }
//            templateOrder.setUserAddress(userAddress);
        }
        List<Order> orderList = new ArrayList<>();
        Order storeOrder = getOrderByOrderGoods(orderGoods);
//        storeOrder.setShippingPrice(goodsService.getFreightPriceInRegion(goodsSku, userAddress.getDistrict(), goodsNum));
        storeOrder.setOrderAmount(storeOrder.getOrderAmount());
        storeOrder.setTotalAmount(storeOrder.getTotalAmount());
        storeOrder.setMobile(user.getMobile());
        orderList.add(storeOrder);
        if (goodsSku.getIsAbleOrderProm()) {
            this.userOrderPromotion(orderList);
        }
        templateOrder.setOrderList(orderList);
        couponService.calculateCouponAmount(user, templateOrder);
        this.calculateMasterOrder(templateOrder);
        this.useIntegral(user, templateOrder);
        return templateOrder;
    }

    @Override
    public Order getOrderByOrderGoods(OrderGoods orderGoods) {
        List<OrderGoods> orderGoodsList = new ArrayList<>();
        orderGoodsList.add(orderGoods);
        Order storeOrder = new Order();
        storeOrder.setStoreId(orderGoods.getStoreId());
        storeOrder.setGoodsPrice(orderGoods.getGoodsFee());
        storeOrder.setOrderAmount(storeOrder.getGoodsPrice());
        storeOrder.setTotalAmount(storeOrder.getGoodsPrice());
        storeOrder.setPaidMoney(orderGoods.getPaidMoney());
        if (orderGoods.getPaidMoney().compareTo(BigDecimal.ZERO) > 0) {
            storeOrder.setOrderAmount(orderGoods.getPaidMoney());
            storeOrder.setTailAmount(storeOrder.getTotalAmount().subtract(storeOrder.getOrderAmount()));
        }
        storeOrder.setGoodsNum(orderGoods.getGoodsNum());
        storeOrder.setCutFee(orderGoods.getCutFee());
        storeOrder.setOrderGoods(orderGoodsList);
        if(!orderGoods.getPromType().equals(7)){
            //搭配购不记录订单表
            storeOrder.setPromType(orderGoods.getPromType());
            storeOrder.setPromId(orderGoods.getPromId());
        }
        return storeOrder;
    }

    @Override
    public void useIntegral(User user, Order order) {
        if (StringUtils.isEmpty(order.getIntegral()) || order.getIntegral() == 0) {
            return;
        }
        //兑换比例
        BigDecimal pointRate = new BigDecimal((String) configService.getConfigMap().get("shopping_point_rate"));
        //最低使用额度: 如果拥有的积分小于该值, 不可使用
        BigDecimal minUseLimitPoint = new BigDecimal((String) configService.getConfigMap().get("shopping_point_min_limit"));
        //最大使用限制: 最大使用积分比例, 例如: 为50时, 未50% , 那么积分支付抵扣金额不能超过应付金额的50%
        BigDecimal usePercentPoint = new BigDecimal((String) configService.getConfigMap().get("shopping_point_use_percent"))
                .divide(new BigDecimal(100), 2);
        BigDecimal integral = new BigDecimal(order.getIntegral());
        if (order.getIntegral() > user.getPayPoints()) {
            //如果我的积分比最大值小，那么就直接使用我的全部积分
            order.setIntegral(user.getPayPoints());
            integral = new BigDecimal(order.getIntegral());
        }
        if (minUseLimitPoint.compareTo(new BigDecimal(BigInteger.ZERO)) > 0 && (minUseLimitPoint.compareTo(integral) > 0)) {
            throw new ShopException(ResultEnum.USE_POINT_ERROR.getCode(), "您使用的积分必须大于" + minUseLimitPoint + "才可以使用");
        }
        BigDecimal orderAmountIntegral = order.getOrderAmount().multiply(pointRate);//订单全部用积分支付的积分数
        BigDecimal ableMaxIntegral = orderAmountIntegral.multiply(usePercentPoint);
        if (integral.compareTo(ableMaxIntegral) > 0) {
            order.setIntegral(ableMaxIntegral.intValue());
            integral = new BigDecimal(order.getIntegral());
        }
        if (integral.compareTo(orderAmountIntegral) > 0) {
            order.setIntegral(orderAmountIntegral.intValue());
            integral = orderAmountIntegral;
        }
        int surplusIntegral = integral.intValue();//剩余用户积分
        BigDecimal surplusIntegralMoney = integral.divide(pointRate, 2, BigDecimal.ROUND_DOWN);//剩余用户积分金额
        order.setIntegralMoney(surplusIntegralMoney);
        for (Order storeOrder : order.getOrderList()) {
            BigDecimal proportion = storeOrder.getOrderAmount()
                    .divide(order.getOrderAmount(), 3, BigDecimal.ROUND_DOWN);//每个商家订单应付金额占总应付金额比例
            BigDecimal storeIntegral = proportion.multiply(integral);//取整
            storeOrder.setIntegral(storeIntegral.intValue());
            storeOrder.setIntegralMoney((new BigDecimal(storeOrder.getIntegral())).divide(pointRate, 2, BigDecimal.ROUND_DOWN));
            storeOrder.setOrderAmount(storeOrder.getOrderAmount().subtract(storeOrder.getIntegralMoney()));// 每个商家减去积分支付抵消的
            surplusIntegral = surplusIntegral - storeOrder.getIntegral();
            surplusIntegralMoney = surplusIntegralMoney.subtract(storeOrder.getIntegralMoney());
        }
        if (surplusIntegral > 0) {
            Order firstStoreOrder = order.getOrderList().get(0);
            firstStoreOrder.setIntegral(firstStoreOrder.getIntegral() + surplusIntegral);
            firstStoreOrder.setIntegralMoney(firstStoreOrder.getIntegralMoney().add(surplusIntegralMoney));
            firstStoreOrder.setOrderAmount(firstStoreOrder.getOrderAmount().subtract(surplusIntegralMoney));
        }
        order.setOrderAmount(order.getOrderAmount().subtract(order.getIntegralMoney()));
    }

    @Override
    public void userOrderPromotion(List<Order> orders) {
        long now = System.currentTimeMillis() / 1000;
        for (Order order : orders) {
            order.setOrderPromAmount(BigDecimal.ZERO);
            order.setOrderPromId(0);
            order.setOrderPromTitle("");
            PromOrder promOrder = promOrderService.getOne(new QueryWrapper<PromOrder>().eq("store_id", order.getStoreId())
                    .le("money", order.getGoodsPrice()).lt("type", 2)
                    .gt("end_time", now).lt("start_time", now).eq("status", 1));
            if (promOrder != null) {
                if (promOrder.getType() == 0) {
                    BigDecimal expressionAmount = order.getGoodsPrice().multiply(new BigDecimal(promOrder.getExpression()))
                            .divide(new BigDecimal(100), 2, BigDecimal.ROUND_DOWN);
                    order.setOrderPromAmount(order.getGoodsPrice().subtract(expressionAmount));
                    order.setOrderAmount(order.getOrderAmount().subtract(order.getOrderPromAmount()));
                    order.setOrderPromId(promOrder.getId());
                    order.setOrderPromTitle(promOrder.getTitle());
                }
                if (promOrder.getType() == 1) {
                    order.setOrderPromAmount(new BigDecimal(promOrder.getExpression()));
                    order.setOrderAmount(order.getOrderAmount().subtract(order.getOrderPromAmount()));
                    order.setOrderPromId(promOrder.getId());
                    order.setOrderPromTitle(promOrder.getTitle());
                }
            }
        }

    }

    @Override
    public Bill getBillByCart(User user, Cart cart) {
        QueryWrapper<Cart> cartQueryWrapper = new QueryWrapper<>();
        cartQueryWrapper.eq("user_id", user.getUserId());
        if (null != cart.getSelected()) {
            cartQueryWrapper.eq("selected", cart.getSelected());
        }
        List<Cart> queryCarts = list(cartQueryWrapper);
        if (queryCarts.size() == 0) {
            return getBill(new ArrayList<>());
        }
        List<Cart> carts = sortByCombination(queryCarts);
        Set<Integer> storeIds = carts.stream().map(Cart::getStoreId).collect(Collectors.toSet());
        List<Store> storeList = sellerService.getStoreListByIds(storeIds);
        Map<Integer, Store> storeMap = storeList.stream().collect(Collectors.toMap(Store::getStoreId, store -> store));
        for (Cart cartItem : carts) {
            Store store = storeMap.get(cartItem.getStoreId());
            if (null == store.getCartList()) {
                List<Cart> cartList = new ArrayList<>();
                cartList.add(cartItem);
                store.setCartList(cartList);
            } else {
                store.getCartList().add(cartItem);
            }
        }
        return getBill(storeList);
    }

    private Bill getBill(List<Store> storeList) {
        Bill bill = new Bill();
        bill.setStoreCartList(storeList);
        List<Order> orderList = this.getOrderListByStoreCart(storeList);
        Order masterOrder = new Order();
        masterOrder.setOrderList(orderList);
        this.calculateMasterOrder(masterOrder);
        bill.setOrder(masterOrder);
        return bill;
    }

    private Cart changeGoodsSkuToCart(GoodsSku goodsSku) {
        Cart cart = new Cart();
        cart.setGoodsId(goodsSku.getGoodsId());
        cart.setGoodsSn(goodsSku.getGoodsSn());
        cart.setGoodsName(goodsSku.getGoodsName());
        cart.setMarketPrice(goodsSku.getMarketPrice());
        cart.setGoodsPrice(goodsSku.getShopPrice());
        cart.setMemberGoodsPrice(goodsSku.getShopPrice());
        cart.setSpecKey(goodsSku.getSpecKey());
        cart.setSpecKeyName(goodsSku.getSpecKeyName());
        cart.setBarCode(goodsSku.getBarCode());
        cart.setBarCode(goodsSku.getBarCode());
        cart.setSku(goodsSku.getSku());
        cart.setStoreId(goodsSku.getStoreId());
        cart.setItemId(goodsSku.getItemId());
        cart.setSelected(1);
        cart.setPromType(goodsSku.getPromType());
        cart.setPromId(goodsSku.getPromId());
        return cart;
    }

    @Override
    public Bill getBillByGoodsSku(User user, GoodsSku goodsSku, Integer goodsNum) {
        Cart cart = this.changeGoodsSkuToCart(goodsSku);
        cart.setGoodsNum(goodsNum);
        Store store = sellerService.getStoreById(cart.getStoreId());
        List<Cart> cartList = new ArrayList<>();
        cartList.add(cart);
        store.setCartList(cartList);
        List<Store> storeList = new ArrayList<>();
        storeList.add(store);
        return this.getBill(storeList);
    }

    private List<OrderGoods> changeCartsToOrderGoods(List<Cart> carts, Map<Integer, Goods> goodsListMap) {
        List<OrderGoods> orderGoodsList = new ArrayList<>();
        for (Cart cart : carts) {
            orderGoodsList.add(changeCartToOrderGoods(cart, goodsListMap.get(cart.getGoodsId())));
        }
        return orderGoodsList;
    }

    private List<OrderGoods> changeCartsToOrderGoods(List<Cart> carts) {
        List<OrderGoods> orderGoodsList = new ArrayList<>();
        for (Cart cart : carts) {
            if (cart.getSelected() == 1) {
                orderGoodsList.add(changeCartToOrderGoods(cart));
            }
        }
        return orderGoodsList;
    }

    private List<Order> getOrderList(List<OrderGoods> orderGoodsList) {
        List<Order> orderList = new ArrayList<>();
        Map<Integer, List<OrderGoods>> orderMap = new LinkedHashMap<>();
        for (OrderGoods orderGoods : orderGoodsList) {
            if (orderMap.containsKey(orderGoods.getStoreId())) {
                orderMap.get(orderGoods.getStoreId()).add(orderGoods);
            } else {
                List<OrderGoods> tmpOrderGoodsList = new ArrayList<>();
                tmpOrderGoodsList.add(orderGoods);
                orderMap.put(orderGoods.getStoreId(), tmpOrderGoodsList);
            }
        }
        for (Map.Entry<Integer, List<OrderGoods>> orderEntry : orderMap.entrySet()) {
            Order order = new Order();
            order.setStoreId(orderEntry.getKey());
            order.setGoodsPrice(new BigDecimal(BigInteger.ZERO));
            order.setOrderGoods(new ArrayList<>());
            order.setCutFee(new BigDecimal(BigInteger.ZERO));
            order.setShippingPrice(new BigDecimal(BigInteger.ZERO));
            order.setPaidMoney(new BigDecimal(BigInteger.ZERO));
            //后根据店铺订单商品数据生成店铺订单数据
            for (OrderGoods orderGoods : orderEntry.getValue()) {
                order.setGoodsPrice(order.getGoodsPrice().add(orderGoods.getGoodsFee()));
                order.setOrderAmount(order.getGoodsPrice());
                order.setTotalAmount(order.getGoodsPrice());
                order.setCutFee(order.getCutFee().add(orderGoods.getCutFee()));
                order.setPaidMoney(order.getPaidMoney().add(orderGoods.getPaidMoney()));
                order.getOrderGoods().add(orderGoods);
            }
            orderList.add(order);
        }
        return orderList;
    }

    private List<Order> getOrderListByStoreCart(List<Store> storeList) {
        List<Order> orderList = new ArrayList<>();
        for (Store store : storeList) {
            List<OrderGoods> orderGoodsList = this.changeCartsToOrderGoods(store.getCartList());
            List<Order> storeOrderList = this.getOrderList(orderGoodsList);
            if (!storeOrderList.isEmpty()) {
                orderList.add(storeOrderList.get(0));
            }
        }
        return orderList;
    }

    private OrderGoods changeCartToOrderGoods(Cart cart, Goods goods) {
        OrderGoods orderGoods = changeCartToOrderGoods(cart);
        orderGoods.setCostPrice(goods.getCostPrice());
        orderGoods.setGoodsName(goods.getGoodsName());
        orderGoods.setGoodsSn(goods.getGoodsSn());
        orderGoods.setGiveIntegral(goods.getGiveIntegral());
        orderGoods.setDistribut(goods.getDistribut());
        if (goods.getCommission() != null) {
            orderGoods.setCommission(goods.getCommission());
        }
        return orderGoods;
    }

    private OrderGoods changeCartToOrderGoods(Cart cart) {
        OrderGoods orderGoods = new OrderGoods();
        orderGoods.setStoreId(cart.getStoreId());
        orderGoods.setGoodsId(cart.getGoodsId());
        orderGoods.setSpecKey(cart.getSpecKey());
        orderGoods.setFinalPrice(cart.getMemberGoodsPrice());
        orderGoods.setGoodsPrice(cart.getGoodsPrice());
        orderGoods.setGoodsFee(cart.getGoodsFee());
        orderGoods.setMemberGoodsPrice(cart.getMemberGoodsPrice());
        orderGoods.setCutFee(cart.getCutFee());
        orderGoods.setGoodsNum(cart.getGoodsNum());
        orderGoods.setSpecKeyName(cart.getSpecKeyName());
        orderGoods.setBarCode(String.valueOf(Math.abs((new Random()).nextInt() * (shopConstant.getOrderGoodsBarCodeEnd()
                - shopConstant.getOrderGoodsBarCodeStart() + 1) + shopConstant.getOrderGoodsBarCodeStart())));//生成 [m,n] 的数字
        orderGoods.setStoreId(cart.getStoreId());
        orderGoods.setIsDelivery(cart.getIsDelivery());
        orderGoods.setLimitNum(cart.getLimitNum());
        orderGoods.setPaidMoney(new BigDecimal(BigInteger.ZERO));
        orderGoods.setPromId(cart.getPromId());
        orderGoods.setPromType(cart.getPromType());
        return orderGoods;
    }

    private void delivery(List<Order> orderList, Map<Integer, Goods> goodsListMap, Integer regionId) {
        for (Order storeOrder : orderList) {
            Map<Integer, Goods> storeGoodsMap = new HashMap<>();
            Map<Integer, Integer> goodsNumMap = new HashMap<>();
            for (OrderGoods orderGoods : storeOrder.getOrderGoods()) {
                if (storeGoodsMap.containsKey(orderGoods.getGoodsId())) {
                    Goods goods = storeGoodsMap.get(orderGoods.getGoodsId());
                    Goods tmpGoods = goodsListMap.get(orderGoods.getGoodsId());
                    goods.setVolume(tmpGoods.getVolume() + goods.getVolume());
                    goods.setWeight(tmpGoods.getWeight() + goods.getWeight());
                    goods.setVolume(tmpGoods.getVolume() + goods.getVolume());
                    storeGoodsMap.put(orderGoods.getGoodsId(), goods);
                } else {
                    storeGoodsMap.put(orderGoods.getGoodsId(), goodsListMap.get(orderGoods.getGoodsId()));
                }
                if (goodsNumMap.containsKey(orderGoods.getGoodsId())) {
                    Integer goodsNum = goodsNumMap.get(orderGoods.getGoodsId()) + orderGoods.getGoodsNum();
                    goodsNumMap.put(orderGoods.getGoodsId(), goodsNum);
                } else {
                    goodsNumMap.put(orderGoods.getGoodsId(), orderGoods.getGoodsNum());
                }
            }
            for (Map.Entry<Integer, Goods> goods : storeGoodsMap.entrySet()) {
                BigDecimal shippingPrice = goodsService.getFreightPriceInRegion(goods.getValue(), regionId, goodsNumMap.get(goods.getKey()));
                //店铺订单加上物流配送费
                storeOrder.setShippingPrice(storeOrder.getShippingPrice().add(shippingPrice));
                storeOrder.setOrderAmount(storeOrder.getOrderAmount().add(shippingPrice));
                storeOrder.setTotalAmount(storeOrder.getTotalAmount().add(shippingPrice));
            }
        }
    }

    @Override
    public OrderGoods changeGoodsSkuToOrderGoods(GoodsSku goodsSku, Integer goodsNum) {
        OrderGoods orderGoods = new OrderGoods();
        orderGoods.setStoreId(goodsSku.getStoreId());
        orderGoods.setGoodsId(goodsSku.getGoodsId());
        orderGoods.setSpecKey(goodsSku.getSpecKey());
        orderGoods.setFinalPrice(goodsSku.getShopPrice());
        orderGoods.setGoodsPrice(goodsSku.getShopPrice());
        orderGoods.setGoodsFee(goodsSku.getShopPrice().multiply(new BigDecimal(goodsNum)));
        orderGoods.setMemberGoodsPrice(goodsSku.getShopPrice());
        orderGoods.setCutFee(BigDecimal.ZERO);
        orderGoods.setGoodsNum(goodsNum);
        orderGoods.setSpecKeyName(goodsSku.getSpecKeyName());
        orderGoods.setBarCode(String.valueOf(Math.abs((new Random()).nextInt() * (shopConstant.getOrderGoodsBarCodeEnd()
                - shopConstant.getOrderGoodsBarCodeStart() + 1) + shopConstant.getOrderGoodsBarCodeStart())));//生成 [m,n] 的数字
        orderGoods.setStoreId(goodsSku.getStoreId());
        orderGoods.setLimitNum(goodsSku.getStoreCount());
        orderGoods.setCostPrice(goodsSku.getCostPrice());
        orderGoods.setGoodsName(goodsSku.getGoodsName());
        orderGoods.setGoodsSn(goodsSku.getGoodsSn());
        orderGoods.setPromType(goodsSku.getPromType());
        orderGoods.setPromId(goodsSku.getPromId());
        orderGoods.setGiveIntegral(goodsSku.getGiveIntegral());
        orderGoods.setDistribut(goodsSku.getDistribut());
        orderGoods.setPaidMoney(goodsSku.getPaidMoney().multiply(new BigDecimal(goodsNum)));
        return orderGoods;
    }

    @Override
    public int getCartGoodsCountByUser(User user) {
        return cartMapper.selectCartGoodsCountByUser(user);
    }

    @Override
    public void calculateMasterOrder(Order masterOrder) {
        masterOrder.setGoodsPrice(new BigDecimal(BigInteger.ZERO));
        masterOrder.setOrderAmount(new BigDecimal(BigInteger.ZERO));
        masterOrder.setTotalAmount(new BigDecimal(BigInteger.ZERO));
        masterOrder.setShippingPrice(new BigDecimal(BigInteger.ZERO));
        //数据库不存在的字段
        masterOrder.setCutFee(new BigDecimal(BigInteger.ZERO));
        masterOrder.setPaidMoney(new BigDecimal(BigInteger.ZERO));
        masterOrder.setCouponPrice(new BigDecimal(BigInteger.ZERO));
        for (Order storeOrder : masterOrder.getOrderList()) {
            masterOrder.setGoodsPrice(masterOrder.getGoodsPrice().add(storeOrder.getGoodsPrice()));
            masterOrder.setOrderAmount(masterOrder.getOrderAmount().add(storeOrder.getOrderAmount()));
            masterOrder.setTotalAmount(masterOrder.getTotalAmount().add(storeOrder.getTotalAmount()));
            if (storeOrder.getCutFee() != null) {
                masterOrder.setCutFee(masterOrder.getCutFee().add(storeOrder.getCutFee()));
            }
            masterOrder.setPaidMoney(masterOrder.getPaidMoney().add(storeOrder.getPaidMoney()));
            if (storeOrder.getTailAmount() != null) {
                masterOrder.setTailAmount(masterOrder.getTotalAmount().subtract(storeOrder.getTailAmount()));
            }
            if (storeOrder.getShippingPrice() != null) {
                masterOrder.setShippingPrice(masterOrder.getShippingPrice().add(storeOrder.getShippingPrice()));
            }
            if (storeOrder.getCouponPrice() != null) {
                masterOrder.setCouponPrice(masterOrder.getCouponPrice().add(storeOrder.getCouponPrice()));
            }
        }
    }

}
