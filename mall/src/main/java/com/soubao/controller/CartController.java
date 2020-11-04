package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soubao.entity.*;
import com.soubao.common.exception.ResultEnum;
import com.soubao.service.*;
import com.soubao.service.impl.AuthenticationFacade;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author dyr
 * @since 2019-08-20
 */
@RestController
@Slf4j
@PreAuthorize("hasAnyRole('USER')")
@RequestMapping("/cart")
@Api(value = "购物车控制器", tags = {"购物车相关接口"})
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private AuthenticationFacade authenticationFacade;
    @Autowired
    private UserService userService;


    @ApiOperation(value = "添加购物车")
    @PostMapping
    public SBApi addCart(@RequestBody @Valid Cart cart) {
        SBApi sbApi = new SBApi();
        User user = authenticationFacade.getPrincipal(User.class);
        if (cartService.addCart(user, cart)) {
            sbApi.setMsg("加入购物车成功");
        } else {
            sbApi.setStatus(ResultEnum.UNKNOWN_ERROR.getCode());
            sbApi.setMsg("加入购物车失败");
        }
        return sbApi;
    }

    @ApiOperation(value = "删除购物车")
    @DeleteMapping("/{ids}")
    public SBApi deleteCart(@PathVariable(value = "ids") Set<Integer> cartId) {
        User user = authenticationFacade.getPrincipal(User.class);
        List<Cart> carts = cartService.list(new QueryWrapper<Cart>().eq("user_id", user.getUserId()).in("id", cartId));
        cartService.delete(carts);
        return new SBApi();
    }

    @ApiOperation(value = "获取购物车,账单")
    @GetMapping("bill")
    public Bill cartList(Cart cart,
                         @RequestParam(value = "goods_id", required = false) Integer goodsId,
                         @RequestParam(value = "item_id", required = false) Integer itemId,
                         @RequestParam(value = "goods_num", required = false) Integer goodsNum) {
        User user = authenticationFacade.getPrincipal(User.class);
        Bill bill;
        if(goodsId != null){
            GoodsSku goodsSku = goodsService.getGoodsSku(goodsId, itemId);
            goodsSku.useGoodsProm();
            bill = cartService.getBillByGoodsSku(user, goodsSku, goodsNum);
        }else{
            bill = cartService.getBillByCart(user, cart);
        }
        return bill;
    }

    @PutMapping("bill")
    @ApiOperation(value = "修改购物车,账单")
    public SBApi updateCartList(@RequestBody List<Cart> cartList, SBApi sbApi) {
        User user = authenticationFacade.getPrincipal(User.class);
        if (cartService.updateCartList(user, cartList)) {
            sbApi.setMsg("更新购物车成功");
        } else {
            sbApi.setStatus(ResultEnum.UNKNOWN_ERROR.getCode());
            sbApi.setMsg("更新购物车失败");
        }
        return sbApi;
    }

    @GetMapping("order")
    @ApiOperation(value = "获取购物车订单")
    public Order getCartOrder(@Validated({com.soubao.validation.group.order.Cart.class}) Order order,
                              @RequestParam(value = "goods_id", required = false) Integer goodsId,
                              @RequestParam(value = "item_id", required = false) Integer itemId,
                              @RequestParam(value = "goods_num", required = false) Integer goodsNum) {
        User user = userService.getUserCurrent();
        Order cartOrder;
        if (goodsId != null) {
            GoodsSku goodsSku = goodsService.getGoodsSku(goodsId, itemId);
            goodsSku.useGoodsProm();
            cartOrder = cartService.getOrderByGoodsSku(user, order, goodsSku, goodsNum, false);
        }  else {
            cartOrder = cartService.getOrderByCart(user, order, false);
        }
        return cartOrder;
    }

    @PostMapping("order")
    @ApiOperation(value = "提交购物车订单")
    public SBApi addCartOrder(@RequestBody Order requestOrder,
                              @RequestParam(value = "goods_id", required = false) Integer goodsId,
                              @RequestParam(value = "item_id", required = false) Integer itemId,
                              @RequestParam(value = "goods_num", required = false) Integer goodsNum) {
        User user = userService.getUserCurrent();
        Order preOrder;
        if (goodsId != null) {
            GoodsSku goodsSku = goodsService.getGoodsSku(goodsId, itemId);
            goodsSku.useGoodsProm();
            preOrder = cartService.addAndGetOrderByGoodsSku(user, requestOrder, goodsSku, goodsNum);
        } else {
            preOrder = cartService.getPreCreateOrder(user, requestOrder);
            cartService.remove((new QueryWrapper<Cart>()).eq("user_id", user.getUserId()).eq("selected", 1));
        }
        Order masterOrder = cartService.placeAndGetOrder(preOrder);
        return SBApi.builder().status(ResultEnum.SUCCESS.getCode()).result(masterOrder).build();
    }

    @GetMapping("/goods_count")
    @ApiOperation(value = "获取购物车商品总数")
    public Integer goodsCount() {
        User user = authenticationFacade.getPrincipal(User.class);
        return cartService.getCartGoodsCountByUser(user);
    }

}
