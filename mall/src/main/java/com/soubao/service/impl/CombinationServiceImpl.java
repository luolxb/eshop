package com.soubao.service.impl;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.common.constant.ShopConstant;
import com.soubao.dao.CombinationMapper;
import com.soubao.entity.*;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.*;
import com.soubao.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 组合促销表 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2020-05-19
 */
@Slf4j
@Service
public class CombinationServiceImpl extends ServiceImpl<CombinationMapper, Combination> implements CombinationService {
    @Autowired
    private CartService cartService;
    @Autowired
    private ShopConstant shopConstant;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private CombinationMapper combinationMapper;
    @Autowired
    private CombinationGoodsService combinationGoodsService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private SpecGoodsPriceService specGoodsPriceService;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void addCart(User user, Combination combination, Integer num) {
        if (combination.getCombinationGoods().size() < 2) {
            throw new ShopException(ResultEnum.UN_LESS_TWO_GOODS);
        }
        if (null == combinationGoodsService.getMaster(combination.getCombinationGoods())) {
            throw new ShopException(ResultEnum.NO_HAVE_MASTER_GOODS);
        }
        int userCartCount = cartService.count((new QueryWrapper<Cart>()).select("id").eq("user_id", user.getUserId()));
        if ((userCartCount + combination.getCombinationGoods().size()) > shopConstant.getCartMostNum()) {
            throw new ShopException(ResultEnum.ADD_CART_TOO_MANY);
        }
        combinationGoodsService.withGoodsSku(combination.getCombinationGoods());
        checkAddCartStock(user, combination.getCombinationGoods(), num);
        List<Cart> masterCartList = cartService.list(new QueryWrapper<Cart>().eq("user_id", user.getUserId())
                .eq("combination_group_id", 0).eq("prom_type", 7).eq("prom_id", combination.getCombinationId()));
        if (masterCartList.size() > 0) {
            List<CombinationGoods> combinationGoodsAscList = combination.getCombinationGoods().stream()
                    .sorted(Comparator.comparing(CombinationGoods::getGoodsId)).collect(Collectors.toList());
            StringBuilder combinationKeyStr = new StringBuilder();
            Map<String, CombinationGoods> combinationGoodsMap = new HashMap<>();
            combinationGoodsAscList.forEach(combinationGoods -> {
                combinationGoodsMap.put(combinationGoods.getSkuSn(), combinationGoods);
                combinationKeyStr.append(combinationGoods.getSkuSn()).append(",");
            });
            combinationKeyStr.deleteCharAt(combinationKeyStr.length() - 1);
            for (Cart masterCart : masterCartList) {
                List<Cart> childCarts = cartService.list(new QueryWrapper<Cart>()
                        .select("goods_id,item_id")
                        .eq("combination_group_id", masterCart.getId()).or().eq("id", masterCart.getId())
                        .orderByAsc("goods_id"));
                if (childCarts.size() > 0) {
                    StringBuilder cartKeyStr = new StringBuilder();
                    childCarts.forEach(childCart -> cartKeyStr.append(childCart.getGoodsId()).append("-").append(childCart.getItemId()).append(","));
                    cartKeyStr.deleteCharAt(cartKeyStr.length() - 1);
                    if(combinationKeyStr.toString().equals(cartKeyStr.toString())){
                        //匹配到购物车的套餐和正在加入购物车的套餐是一样的
                        List<Cart> sameCartList = cartService.list(new QueryWrapper<Cart>().eq("combination_group_id", masterCart.getId())
                                .or().eq("id", masterCart.getId()));
                        List<Cart> updateCarts = new ArrayList<>();
                        for (Cart sameCart : sameCartList) {
                            updateCarts.add((new Cart()).setId(sameCart.getId()).setGoodsNum(sameCart.getGoodsNum() + num)
                                    .setGoodsPrice(combinationGoodsMap.get(sameCart.getGoodsId() + "-" + sameCart.getItemId()).getOriginalPrice())
                                    .setMemberGoodsPrice(combinationGoodsMap.get(sameCart.getGoodsId() + "-" + sameCart.getItemId()).getPrice()));
                        }
                        cartService.updateBatchById(updateCarts);
                        return;
                    }
                }
            }
        }
        Cart masterCart = combinationGoodsService.changeToCart(combinationGoodsService.getMaster(combination.getCombinationGoods()));
        masterCart.setUserId(user.getUserId());
        masterCart.setGoodsNum(num);
        masterCart.setAddTime(System.currentTimeMillis() / 1000);
        cartService.save(masterCart);
        List<Cart> newCartList = new ArrayList<>();
        for (CombinationGoods combinationGoods : combination.getCombinationGoods()) {
            if (!combinationGoods.getIsMaster()) {
                Cart newCart = combinationGoodsService.changeToCart(combinationGoods);
                newCart.setCombinationGroupId(masterCart.getId());
                newCart.setUserId(user.getUserId());
                newCart.setGoodsNum(num);
                newCart.setAddTime(System.currentTimeMillis() / 1000);
                newCartList.add(newCart);
            }
        }
        cartService.saveBatch(newCartList);
    }

    @Override
    public void withCombinationGoods(List<Combination> records) {
        if (records.size() > 0) {
            Set<Integer> combinationIds = records.stream().map(Combination::getCombinationId).collect(Collectors.toSet());
            Map<Integer, List<CombinationGoods>> combinationGoodsMap = combinationGoodsService.list(new QueryWrapper<CombinationGoods>().in("combination_id", combinationIds))
                    .stream()
                    .collect(Collectors.groupingBy(CombinationGoods::getCombinationId));
            records.forEach(combination -> {
                if (combinationGoodsMap.containsKey(combination.getCombinationId())) {
                    combinationGoodsMap.get(combination.getCombinationId()).forEach(combinationGoods -> {
                        if (combinationGoods.getIsMaster()) {
                            combination.setGoodsName(combinationGoods.getGoodsName());
                        }
                    });
                }
            });
        }
    }

    @Override
    public void schedule() {
        List<Object> cList = redisUtil.lGet("combination", 0, -1);
        Set<Integer> combinationIds = new HashSet<>();
        for (Object o : cList) {
            Combination combination = (Combination)o;
            if (combination.getEndTime() <= System.currentTimeMillis() / 1000) {
                combinationIds.add(combination.getCombinationId());
                redisUtil.lRemove("combination", 1, o);
            }
        }
        if (combinationIds.size() > 0) {
            update(new UpdateWrapper<Combination>().set("is_on_sale", 0).in("combination_id", combinationIds));
            List<CombinationGoods> combinationGoods = combinationGoodsService.list(new QueryWrapper<CombinationGoods>().in("combination_id", combinationIds));
            combinationGoodsService.recoveryPromTypes(combinationGoods);
        }

    }

    @Override
    public void deleteTask() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -3);
        long time = calendar.getTimeInMillis() / 1000;
        List<Combination> combinationList = list(new QueryWrapper<Combination>().eq("is_deleted", 1).le("end_time", time));
        if (combinationList.size() > 0) {
            Set<Integer> ids = combinationList.stream().map(Combination::getCombinationId).collect(Collectors.toSet());
            removeByIds(ids);
            combinationGoodsService.remove(new QueryWrapper<CombinationGoods>().in("combination_id", ids));
        }
    }

    @Override
    public void isMaster(Combination combination) {
        boolean isMaster = false;
        for (CombinationGoods combinationGood : combination.getCombinationGoods()) {
            if (combinationGood.getIsMaster()) {
                isMaster = true;
            }
        }
        if (!isMaster) {
            throw new ShopException(ResultEnum.NO_HAVE_MASTER_GOODS);
        }
    }

    private void checkAddCartStock(User user, List<CombinationGoods> combinationGoodsList, Integer num) {
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("goods_id,item_id,sum(goods_num) as goods_num");
        for (CombinationGoods combinationGoods : combinationGoodsList) {
            if (combinationGoods.getItemId() > 0) {
                queryWrapper.or(i -> i.eq("goods_id", combinationGoods.getGoodsId()).eq("item_id", combinationGoods.getItemId()));
            } else {
                queryWrapper.or(i -> i.eq("goods_id", combinationGoods.getGoodsId()));
            }
        }
        queryWrapper.eq("user_id", user.getUserId()).groupBy("goods_id,item_id");
        List<Cart> carts = cartService.list(queryWrapper);
        Map<Integer, Integer> cartGoodsNumMap = new HashMap<>();//购物车商品sku总数
        Map<Integer, Integer> cartItemNumMap = new HashMap<>();//购物车item,sku总数
        for (Cart cart : carts) {
            if (cart.getItemId() > 0) {
                cartGoodsNumMap.put(cart.getGoodsId(), cart.getGoodsNum());
            } else {
                cartItemNumMap.put(cart.getGoodsId(), cart.getGoodsNum());
            }
        }
        StringBuilder stockErrorMsg = new StringBuilder();
        for (CombinationGoods combinationGoods : combinationGoodsList) {
            if (combinationGoods.getItemId() > 0) {
                if (cartItemNumMap.getOrDefault(combinationGoods.getItemId().intValue(), 0) + num >
                        combinationGoods.getGoodsSku().getStoreCount()) {
                    stockErrorMsg.append(combinationGoods.getGoodsName()).append(" ").append(combinationGoods.getKeyName()).append("商品库存不足;");
                }
            } else {
                if (cartGoodsNumMap.getOrDefault(combinationGoods.getGoodsId(), 0) + num >
                        combinationGoods.getGoodsSku().getStoreCount()) {
                    stockErrorMsg.append(combinationGoods.getGoodsName()).append("商品库存不足;");
                }
            }
        }
        if (stockErrorMsg.length() > 0) {
            throw new ShopException(ResultEnum.NOT_ENOUGH_STOCK.getCode(), stockErrorMsg.toString());
        }
    }

    @Override
    public void withStore(List<Combination> records) {
        if (records.size() > 0) {
            Set<Integer> storeIds = records.stream().map(Combination::getStoreId).collect(Collectors.toSet());
            Map<Integer, Store> storeMap = sellerService.getStoreListByIds(storeIds).stream().collect(Collectors.toMap(Store::getStoreId, store -> store));
            records.forEach(combination -> {
                if (storeMap.containsKey(combination.getStoreId())) {
                    combination.setStoreName(storeMap.get(combination.getStoreId()).getStoreName());
                }
            });
        }
    }

    @Override
    public IPage<Combination> getCombinationPage(Page<Combination> combinationPage, QueryWrapper<Combination> wrapper) {
        return combinationMapper.selectCombinationPage(combinationPage, wrapper);
    }

    @Override
    public Combination getCombination(Integer combinationId) {
        Combination combination = getById(combinationId);
        List<CombinationGoods> combinationGoodsList = combinationGoodsService.list(new QueryWrapper<CombinationGoods>().in("combination_id", combinationId));
        withGoods(combinationGoodsList);
        combination.setCombinationGoods(combinationGoodsList);
        return combination;
    }

    @PostConstruct
    public void setCombinationToRedis() {
        redisUtil.del("combination");
        QueryWrapper<Combination> wrapper = new QueryWrapper<>();
        wrapper.eq("is_deleted", 0).eq("is_on_sale", 1);
        List<Combination> list = this.list(wrapper);
        for (Combination combination : list) {
            redisUtil.lSet("combination", combination);
        }
    }

    private void withGoods(List<CombinationGoods> combinationGoodsList) {
        if (combinationGoodsList.size() > 0) {
            Set<Integer> goodsIds = combinationGoodsList
                    .stream()
                    .filter(combinationGoods -> combinationGoods.getGoodsId() > 0)
                    .map(CombinationGoods::getGoodsId)
                    .collect(Collectors.toSet());
            Set<Long> itemIds = combinationGoodsList
                    .stream()
                    .filter(combinationGoods -> combinationGoods.getItemId() > 0)
                    .map(CombinationGoods::getItemId)
                    .collect(Collectors.toSet());
            Map<Integer, Goods> goodsMap = goodsService
                    .list(new QueryWrapper<Goods>().select("goods_id,store_count,cost_price").in("goods_id", goodsIds))
                    .stream()
                    .collect(Collectors.toMap(Goods::getGoodsId, goods -> goods));
            Map<Long, SpecGoodsPrice> specGoodsMap = new HashMap<>();
            if (itemIds.size() > 0) {
                specGoodsMap = specGoodsPriceService.list(new QueryWrapper<SpecGoodsPrice>().select("item_id,store_count,cost").in("item_id", itemIds))
                        .stream()
                        .collect(Collectors.toMap(SpecGoodsPrice::getItemId, specGoodsPrice -> specGoodsPrice));
            }
            for (CombinationGoods combinationGoods : combinationGoodsList) {
                Long itemId = combinationGoods.getItemId();
                Integer goodsId = combinationGoods.getGoodsId();
                if (itemId > 0) {
                    if (specGoodsMap.containsKey(itemId)) {
                        combinationGoods.setCostPrice(specGoodsMap.get(itemId).getCost());
                        combinationGoods.setGoodsCount(specGoodsMap.get(itemId).getStoreCount());
                        combinationGoods.setGoodsImg(specGoodsMap.get(itemId).getSpecImg());
                    }
                } else {
                    if (goodsMap.containsKey(goodsId)) {
                        combinationGoods.setCostPrice(goodsMap.get(goodsId).getCostPrice());
                        combinationGoods.setGoodsCount(goodsMap.get(goodsId).getStoreCount());
                        combinationGoods.setGoodsImg(goodsMap.get(goodsId).getOriginalImg());
                    }
                }
            }
        }
    }
}
