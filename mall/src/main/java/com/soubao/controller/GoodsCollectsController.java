package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.Goods;
import com.soubao.entity.GoodsCollect;
import com.soubao.entity.User;
import com.soubao.service.GoodsCollectService;
import com.soubao.service.GoodsService;
import com.soubao.service.impl.AuthenticationFacade;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Slf4j
@PreAuthorize("hasAnyRole('ROLE_USER')")
@RestController
@Api(value = "商品收藏控制器", tags = {"商品收藏相关接口"})
public class GoodsCollectsController {
    @Autowired
    private GoodsCollectService goodsCollectService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private AuthenticationFacade authenticationFacade;

    @ApiOperation(value = "用户获取商品收藏记录")
    @GetMapping("goods_collects")
    public GoodsCollect collects(@ApiParam("商品id") @RequestParam("goods_id") Integer goodsId) {
        User user = authenticationFacade.getPrincipal(User.class);
        return goodsCollectService.getOne(new QueryWrapper<GoodsCollect>().select("collect_id,add_time")
                .eq("goods_id", goodsId).eq("user_id", user.getUserId()));
    }

    @ApiOperation(value = "用户获取商品收藏记录列表")
    @GetMapping("goods_collect/page")
    public IPage<GoodsCollect> page(@ApiParam("商品类型,活动商品(PROM)") @RequestParam(value = "type", required = false) String type,
                                    @ApiParam("页码") @RequestParam(value = "p", defaultValue = "1") Integer page,
                                    @ApiParam("单页记录数") @RequestParam(value = "size", defaultValue = "16") Integer size) {
        User user = authenticationFacade.getPrincipal(User.class);
        QueryWrapper<GoodsCollect> wrapper = new QueryWrapper<>();
        wrapper.eq("gc.user_id", user.getUserId());
        if ("PROM".equals(type)) {
            wrapper.gt("gc.prom_type", 0);
        }
        return goodsCollectService.getCollectPage(new Page<>(page, size), wrapper);
    }

    @ApiOperation(value = "用户添加商品收藏记录")
    @PostMapping("goods_collects/{goods_id}")
    public SBApi addGoodsCollection(@PathVariable("goods_id") Integer goodsId, SBApi sbApi) {
        User user = authenticationFacade.getPrincipal(User.class);
        Goods goods = goodsService.getById(goodsId);
        goodsCollectService.addGoodsCollection(user, goods);
        sbApi.setMsg("已收藏");
        return sbApi;
    }

    @ApiOperation(value = "用户取消商品收藏记录")
    @DeleteMapping("goods_collects/{goods_id}")
    public SBApi removeGoodsCollections(@ApiParam("商品id") @PathVariable("goods_id") Set<Integer> goodsId, SBApi sbApi) {
        User user = authenticationFacade.getPrincipal(User.class);
        List<Goods> goodsList = goodsService.listByIds(goodsId);
        goodsCollectService.removeGoodsCollections(user, goodsList);
        sbApi.setMsg("已取消收藏");
        return sbApi;
    }
}
