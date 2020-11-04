package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.Goods;
import com.soubao.entity.User;
import com.soubao.entity.UserDistribution;
import com.soubao.service.GoodsService;
import com.soubao.service.UserDistributionService;
import com.soubao.service.UserService;
import com.soubao.service.impl.AuthenticationFacade;
import com.soubao.common.vo.SBApi;
import com.soubao.vo.UserDistributionGoodsVo;
import com.soubao.vo.UserDistributionSurveyVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @since 2019-09-10
 */
@Api(value = "用户分销商品控制器", tags = {"用户分销商品相关接口"})
@PreAuthorize("hasAnyRole('ROLE_USER')")
@RequestMapping("/distribution/goods")
@RestController
public class DistributionGoodsController {
    @Autowired
    private UserDistributionService userDistributionService;
    @Autowired
    private AuthenticationFacade authenticationFacade;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private UserService userService;

    @ApiOperation("用户分销的商品分页")
    @GetMapping("/page")
    public Page<UserDistribution> page(@ApiParam("用户id") @RequestParam("user_id") Integer userId,
                                       @ApiParam("页码") @RequestParam(value = "p", defaultValue = "1") Integer page,
                                       @ApiParam("单页记录数") @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return (Page<UserDistribution>) userDistributionService.page(new Page<>(page, size), new QueryWrapper<UserDistribution>()
                .eq("user_id", userId)
                .orderByDesc("sales_num"));
    }

    @ApiOperation("获取用户已添加分销的商品列表")
    @GetMapping("/page/added")
    public Page<UserDistributionGoodsVo> distributeGoods(
            @ApiParam("页码") @RequestParam(value = "p", defaultValue = "1") Integer page,
            @ApiParam("单页记录数") @RequestParam(value = "size", defaultValue = "20") Integer size) {
        UserDistribution userDistribution = new UserDistribution();
        userDistribution.setUserId((authenticationFacade.getPrincipal(User.class)).getUserId());
        return (Page<UserDistributionGoodsVo>) userDistributionService.selectGoodsPage(new Page<>(page, size), userDistribution);
    }

    @ApiOperation("获取用户未添加分销的商品列表")
    @GetMapping("/page/not_added")
    public IPage<Goods> distributeGoods(@ApiParam("商品名称") @RequestParam(value = "goods_name", required = false) String goodsName,
                                        @ApiParam("商品id") @RequestParam(value = "goods_id", required = false) Set<Integer> goodsIds,
                                        @ApiParam("商品品牌id") @RequestParam(value = "brand_id", required = false) Set<Integer> brandId,
                                        @ApiParam("商品分类id") @RequestParam(value = "cat_id", required = false) Set<Integer> catId,
                                        @ApiParam("排序字段") @RequestParam(value = "order_by", defaultValue = "sort") String orderBy,
                                        @ApiParam("是否升序") @RequestParam(value = "asc", defaultValue = "false") boolean isAsc,
                                        @ApiParam("页码") @RequestParam(value = "p", defaultValue = "1") Integer page,
                                        @ApiParam("单页记录数") @RequestParam(value = "size", defaultValue = "20") Integer size) {
        QueryWrapper<Goods> goodsQueryWrapper = new QueryWrapper<>();
        goodsQueryWrapper.eq("is_on_sale", 1).eq("goods_state", 1).gt("distribut", 0);
        if (goodsIds != null){
            goodsQueryWrapper.notIn("goods_id", goodsIds);
        }
        if (isAsc) {
            goodsQueryWrapper.orderByAsc(orderBy);
        } else {
            goodsQueryWrapper.orderByDesc(orderBy);
        }
        if (catId != null && catId.size() > 0) {
            goodsQueryWrapper.and(i -> i.in("cat_id1", catId).or().in("cat_id2", catId).or().in("cat_id3", catId));
        }
        if (brandId != null && brandId.size() > 0) {
            goodsQueryWrapper.in("brand_id", brandId);
        }
        if (goodsName != null) {
            goodsQueryWrapper.like("goods_name", goodsName);
        }
        return goodsService.page(new Page<>(page, size), goodsQueryWrapper);
    }

    @ApiOperation("用户分销商品统计概况")
    @GetMapping("survey")
    public UserDistributionSurveyVo survey() {
        User user = userService.getUserCurrent();
        UserDistributionSurveyVo vo = new UserDistributionSurveyVo();
        vo.setHadAddGoodsNum(userDistributionService.count(new QueryWrapper<UserDistribution>().eq("user_id", user.getUserId())));
        vo.setNoAddGoodsNum(goodsService.getNotAddGoodsCount(user));
        return vo;
    }

    @ApiOperation("用户删除分销商品")
    @DeleteMapping
    public SBApi delDistributionGoods(@ApiParam("商品id组") @RequestParam("goods_id") Set<Integer> goodsIds) {
        userDistributionService.remove(new QueryWrapper<UserDistribution>().in("goods_id", goodsIds)
                .eq("user_id", (authenticationFacade.getPrincipal(User.class)).getUserId()));
        return new SBApi();
    }

    @ApiOperation("用户添加分销商品")
    @PostMapping("add")
    public SBApi addDistributionGoodsList(@ApiParam("商品id组") @RequestParam("goods_id") Set<Integer> goodsIds) {
        User user = authenticationFacade.getPrincipal(User.class);
        List<UserDistribution> userDistributions = userDistributionService.list((new QueryWrapper<UserDistribution>())
                .select("goods_id")
                .eq("user_id", user.getUserId())
                .in("goods_id", goodsIds));
        Set<Integer> hadGoodsIds = userDistributions.stream().map(UserDistribution::getGoodsId).collect(Collectors.toSet());
        goodsIds.removeAll(hadGoodsIds);//删除已添加的
        userDistributionService.addDistributionGoodsList(user, goodsService.list(new QueryWrapper<Goods>()
                .gt("distribut", 0)
                .in("goods_id", goodsIds)));
        return new SBApi();
    }

}
