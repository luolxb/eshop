package com.soubao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.*;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-08-29
 */
public interface CartService extends IService<Cart> {
    //购买者添加商品到购物车
    boolean addCart(User user, Cart cart);

    //批量更新购物车列表
    boolean updateCartList(User user, List<Cart> cartList);

    //购物车列表插入订单数据
    Order getPreCreateOrder(User user, Order templateOrder);

    //购物车列表插入订单数据
    Order addAndGetOrderByGoodsSku(User user, Order templateOrder, GoodsSku goodsSku, Integer goodsNum);

    //预生成购物车订单
    Order getOrderByCart(User user, Order templateOrder, boolean isInsert);

    //预生成立即购买商品的订单
    Order getOrderByGoodsSku(User user, Order templateOrder, GoodsSku goodsSku, Integer goodsNum, boolean isInsert);

    //获取店铺购物车列表账单
    Bill getBillByCart(User user, Cart cart);

    //获取立即购买商品账单
    Bill getBillByGoodsSku(User user, GoodsSku goodsSku, Integer goodsNum);

    //获取用户购物车商品数量
    int getCartGoodsCountByUser(User user);

    /**
     * 提交订单前检查
     * @param user
     * @param requestOrder
     */
    void checkPreOrder(User user, Order requestOrder);

    /**
     * 根据goodsSku生成订单商品
     * @param goodsSku
     * @param goodsNum
     * @return
     */
    OrderGoods changeGoodsSkuToOrderGoods(GoodsSku goodsSku, Integer goodsNum);

    /**
     * 根据订单商品生成订单
     * @param orderGoods
     * @return
     */
    Order getOrderByOrderGoods(OrderGoods orderGoods);

    /**
     * 计算主订单价格
     * @param masterOrder
     * @return
     */
    void calculateMasterOrder(Order masterOrder);

    /**
     * 用户的订单使用积分
     * @param user
     * @param order
     */
    void useIntegral(User user, Order order);

    /**
     * 使用订单优惠
     * @param orders
     */
    void userOrderPromotion(List<Order> orders);

    /**
     * 检查下单的配置
     * @param requestOrder
     */
    void checkOrderShoppingConfig(Order requestOrder);

    List<Cart> sortByCombination(List<Cart> carts);

    void delete(List<Cart> carts);

    Order placeAndGetOrder(Order preOrder);
}
