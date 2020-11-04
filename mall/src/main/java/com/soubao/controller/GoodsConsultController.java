package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.GoodsConsult;
import com.soubao.entity.User;
import com.soubao.service.GoodsConsultService;
import com.soubao.common.vo.SBApi;
import com.soubao.service.impl.AuthenticationFacade;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author dyr
 * @since 2019-11-27
 */
@RestController
@RequestMapping("/consult")
public class    GoodsConsultController {
    @Autowired
    private GoodsConsultService goodsConsultService;
    @Autowired
    private AuthenticationFacade authenticationFacade;

    @GetMapping("/report")
    public Map<String, Object> getReport(
            @RequestParam(value = "store_id", required = false) Integer storeId) {
        Map<String, Object> report = new HashMap<>();
        QueryWrapper<GoodsConsult> baseQueryWrapper = new QueryWrapper<>();
        if (null != storeId) {
            baseQueryWrapper.eq("store_id", storeId);
        }
        report.put("wait_reply_consult", goodsConsultService.count(baseQueryWrapper.clone().eq("status", 0)
                .eq("parent_id", 0)));
        return report;
    }

    @GetMapping("page")
    public IPage<GoodsConsult> goodsConsultPage(@RequestParam(value = "store_id", required = false) Integer storeId,
                                                @RequestParam(value = "content", required = false) String content,
                                                @RequestParam(value = "username", required = false) String username,
                                                @RequestParam(value = "p", defaultValue = "1") Integer p,
                                                @RequestParam(value = "size", defaultValue = "10") Integer size) {


        QueryWrapper<GoodsConsult> queryWrapper = new QueryWrapper<>();
        if (null != storeId) {
            queryWrapper.eq("c.store_id", storeId).eq("c.parent_id", 0);
        }
        if (null != content) {
            queryWrapper.like("c.content", content);
        }
        if (null != username) {
            queryWrapper.like("c.username", username);
        }
        queryWrapper.apply("1=1");
        queryWrapper.orderByDesc("c.add_time");
        return goodsConsultService.selectConsultPage(new Page<>(p, size), queryWrapper);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @ApiOperation("用户咨询分页")
    @GetMapping("/user/page")
    public IPage<GoodsConsult> userGoodsConsultPage(@ApiParam("页码") @RequestParam(value = "p", defaultValue = "1") Integer pageIndex,
                                                    @ApiParam("单页记录数") @RequestParam(value = "size", defaultValue = "12") Integer size) {
        User user = authenticationFacade.getPrincipal(User.class);
        IPage<GoodsConsult> goodsConsultIPage = goodsConsultService.userConsultPage(new Page<>(pageIndex, size), user);
        goodsConsultService.withChildren(goodsConsultIPage.getRecords());
        return goodsConsultIPage;
    }

    @DeleteMapping
    public SBApi deleteConsult(@RequestParam("ids") Set<Integer> ids) {
        goodsConsultService.removeByIds(ids);
        return new SBApi();
    }

    @PutMapping
    public SBApi updateConsult(@RequestParam("ids") Set<Integer> ids,
                               @RequestParam("is_show") Integer isShow) {
        UpdateWrapper<GoodsConsult> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("is_show", isShow).in("id", ids);
        goodsConsultService.update(updateWrapper);
        return new SBApi();
    }

    @GetMapping("/list")
    public List<GoodsConsult> getConsults(@RequestParam("id") Integer id) {
        QueryWrapper<GoodsConsult> goodsConsultQueryWrapper = new QueryWrapper<>();
        goodsConsultQueryWrapper.eq("id", id);
        goodsConsultQueryWrapper.or().eq("parent_id", id);
        goodsConsultQueryWrapper.orderByAsc("add_time");
        List<GoodsConsult> goodsConsultList = goodsConsultService.list(goodsConsultQueryWrapper);
        goodsConsultService.withUser(goodsConsultList);
        return goodsConsultList;
    }

    @PostMapping
    public SBApi saveComment(@RequestParam("id") Integer id,
                             @RequestParam("content") String content) {
        GoodsConsult goodsConsult = new GoodsConsult();
        GoodsConsult consult = goodsConsultService.getById(id);
        Integer goodsId = consult.getGoodsId();
        goodsConsult.setAddTime(System.currentTimeMillis() / 1000);
        goodsConsult.setContent(content);
        goodsConsult.setGoodsId(goodsId);
        goodsConsult.setParentId(id);
        goodsConsult.setConsultType(consult.getConsultType());
        goodsConsult.setStoreId(consult.getStoreId());
        goodsConsultService.save(goodsConsult);
        consult.setStatus(1);
        goodsConsultService.updateById(consult);
        return new SBApi();
    }
}
