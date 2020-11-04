package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.dto.CombinationCartDto;
import com.soubao.entity.Cart;
import com.soubao.entity.Combination;
import com.soubao.entity.CombinationGoods;
import com.soubao.entity.User;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.CombinationGoodsService;
import com.soubao.service.CombinationService;
import com.soubao.service.impl.AuthenticationFacade;
import com.soubao.common.utils.RedisUtil;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 组合促销表 前端控制器
 * </p>
 *
 * @author dyr
 * @since 2020-05-19
 */
@RestController
@RequestMapping("/combination")
public class CombinationController {
    @Autowired
    private AuthenticationFacade authenticationFacade;
    @Autowired
    private CombinationService combinationService;
    @Autowired
    private CombinationGoodsService combinationGoodsService;
    @Autowired
    private RedisUtil redisUtil;

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation(value = "添加搭配购到购物车")
    @PostMapping("/cart")
    public SBApi addCart(@RequestBody @Validated CombinationCartDto combinationCartDto) {
        User user = authenticationFacade.getPrincipal(User.class);
        if (combinationCartDto.getCartList() == null || combinationCartDto.getCartList().size() == 0) {
            throw new ShopException(ResultEnum.ADD_CART_NO_GOODS);
        }
        Combination combination = combinationService.getById(combinationCartDto.getCombinationId());
        if (null == combination) throw new ShopException(ResultEnum.ADD_CART_NO_GOODS);
        QueryWrapper<CombinationGoods> combinationGoodsQueryWrapper = new QueryWrapper<>();
        for (Cart cart : combinationCartDto.getCartList()) {
            if (cart.getItemId() > 0) {
                combinationGoodsQueryWrapper.or(i -> i.eq("goods_id", cart.getGoodsId()).eq("item_id", cart.getItemId()));
            } else {
                combinationGoodsQueryWrapper.or(i -> i.eq("goods_id", cart.getGoodsId()));
            }
        }
        combinationGoodsQueryWrapper.and(i -> i.eq("combination_id", combinationCartDto.getCombinationId()));
        combination.setCombinationGoods(combinationGoodsService.list(combinationGoodsQueryWrapper));
        if (null == combination.getCombinationGoods()) throw new ShopException(ResultEnum.ADD_CART_NO_GOODS);
        combinationService.addCart(user, combination, combinationCartDto.getGoodsNum());
        return new SBApi();
    }

    @GetMapping("page")
    public IPage<Combination> getPage(@RequestParam(value = "store_id", required = false) Integer storeId,
                                      @RequestParam(value = "page", defaultValue = "1") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<Combination> combinationQueryWrapper = new QueryWrapper<>();
        if (null != storeId) {
            combinationQueryWrapper.eq("store_id", storeId);
        }
        combinationQueryWrapper.eq("is_deleted", 0);
        combinationQueryWrapper.orderByDesc("combination_id");
        IPage<Combination> combinationIPage = combinationService.getCombinationPage(new Page<>(page, size), combinationQueryWrapper);
        combinationService.withStore(combinationIPage.getRecords());
        combinationService.withCombinationGoods(combinationIPage.getRecords());
        return combinationIPage;
    }

    @PostMapping
    public SBApi addCombination(@Valid @RequestBody Combination combination) {
        combinationService.isMaster(combination);
        combinationService.save(combination);
        combination.getCombinationGoods().forEach(combinationGoods -> {
            combinationGoods.setCombinationId(combination.getCombinationId());
        });
        combinationGoodsService.saveBatch(combination.getCombinationGoods());
        combinationGoodsService.setGoodsPromType(combination.getCombinationGoods());
        return new SBApi();
    }

    @PutMapping("info")
    public SBApi updateCombinationInfo(@Valid @RequestBody Combination combination) {
        combinationService.isMaster(combination);
        List<CombinationGoods> combinationGoodsList = combinationGoodsService.list(new QueryWrapper<CombinationGoods>().eq("combination_id", combination.getCombinationId()));
        combinationGoodsService.recoveryPromTypes(combinationGoodsList);//将商品恢复成普通商品
        combination.setStatus(0);
        combination.setIsOnSale(0);
        combination.setIsDeleted(0);
        combinationService.updateById(combination);
        combinationGoodsService.remove(new QueryWrapper<CombinationGoods>().eq("combination_id", combination.getCombinationId()));
        combinationGoodsService.saveBatch(combination.getCombinationGoods());
        combinationGoodsService.setGoodsPromType(combination.getCombinationGoods());
        return new SBApi();
    }

    @PutMapping("status")
    public SBApi combinationStatus(@RequestParam(value = "id", required = false) Integer combinationId,
                                   @RequestParam(value = "status", required = false) Integer status) {
        combinationService.update(new UpdateWrapper<Combination>().set("status", status)
                .eq("combination_id", combinationId));
        if (status == 2) {
            List<CombinationGoods> combinationGoodsList = combinationGoodsService.list(new QueryWrapper<CombinationGoods>().eq("combination_id", combinationId));
            combinationGoodsService.recoveryPromTypes(combinationGoodsList);
        }
        return new SBApi();
    }

    @GetMapping("/{id}")
    public Combination combination(@PathVariable("id") Integer combinationId) {
        return combinationService.getCombination(combinationId);
    }

    @DeleteMapping("{id}")
    public SBApi deleteCombination(@PathVariable("id") Integer id) {
        Combination combination = combinationService.getById(id);
        redisUtil.lRemove("combination", 1, combination);
        combination.setIsDeleted(1);
        combinationService.updateById(combination);
        List<CombinationGoods> combinationGoodsList = combinationGoodsService.list(new QueryWrapper<CombinationGoods>()
                .eq("combination_id", id));
        combinationGoodsService.recoveryPromTypes(combinationGoodsList);
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('SELLER, ADMIN')")
    @PutMapping("is_on_sale")
    public SBApi isOnSale(@RequestParam(value = "store_id", required = false) Integer storeId,
                          @RequestParam(value = "combination_id", required = false) Integer id,
                          @RequestParam(value = "isOnSale", required = false) Integer isOnSale) {
        Combination combination = combinationService.getById(id);
        if (null != storeId) {
            if (combination.getIsOnSale() == 2) {
                //该套餐已被强制下架，请联系管理员
                throw new ShopException(ResultEnum.COMBINATION_CANNOT_ON_SALE);
            }
        }
        redisUtil.lRemove("combination", 1, combination);
        combination.setIsOnSale(isOnSale);
        combinationService.updateById(combination);
        if (isOnSale == 1) {
            redisUtil.lSet("combination", combination);
        }
        if (isOnSale == 2) {
            List<CombinationGoods> combinationGoodsList = combinationGoodsService.list(new QueryWrapper<CombinationGoods>()
                    .eq("combination_id", id));
            combinationGoodsService.recoveryPromTypes(combinationGoodsList);
        }
        return new SBApi();
    }
}
