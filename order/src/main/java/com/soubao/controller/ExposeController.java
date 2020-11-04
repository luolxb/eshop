package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.Expose;
import com.soubao.entity.Goods;
import com.soubao.entity.User;
import com.soubao.service.ExposeService;
import com.soubao.service.MallService;
import com.soubao.common.vo.SBApi;
import com.soubao.service.impl.AuthenticationFacade;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 举报表 前端控制器
 * </p>
 *
 * @author dyr
 * @since 2020-03-02
 */
@RestController
@RequestMapping("expose")
public class ExposeController {

    @Autowired
    private ExposeService exposeService;
    @Autowired
    private MallService mallService;
    @Autowired
    private AuthenticationFacade authenticationFacade;

    @GetMapping("page")
    public IPage<Expose> getExposePage(@RequestParam(value = "state", required = false) String state,
                                       @RequestParam(value = "p", defaultValue = "1") Integer p,
                                       @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<Expose> queryWrapper = new QueryWrapper<>();
        if (state != null) {
            queryWrapper.eq("expose_state", state);
        }
        return exposeService.page(new Page<>(p, size), queryWrapper);
    }

    @GetMapping
    public Expose getExpose(@RequestParam(value = "expose_id") Integer exposeId) {
        return exposeService.getById(exposeId);
    }

    @PutMapping
    public SBApi updateExpose(@RequestBody Expose expose) {
        expose.setExposeState(2);
        exposeService.updateById(expose);
        if (expose.getExposeHandleType() == 2) {//恶意举报,该用户的所有未处理举报将被取消
            exposeService.update(new UpdateWrapper<Expose>()
                    .set("expose_handle_type", 2)
                    .set("expose_state", 2)
                    .eq("expose_user_id", expose.getExposeUserId())
                    .eq("expose_state", 1));
        }
        if (expose.getExposeHandleType() == 3) {//有效举报,商品将违规下架
            Goods goods = mallService.goods(expose.getExposeGoodsId());
            goods.setIsOnSale(2);
            mallService.updateGoods(goods);
        }
        return new SBApi();
    }

    @GetMapping("count")
    public Integer getCount(@RequestParam(value = "expose_state", required = false) Integer status) {
        QueryWrapper<Expose> wrapper = new QueryWrapper<>();
        if (status != null) {
            wrapper.eq("expose_state", status);
        }
        return exposeService.count(wrapper);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @ApiOperation("用户举报信息分页")
    @GetMapping("/user/page")
    public IPage<Expose> userExposePage(@ApiParam("页码") @RequestParam(value = "p", defaultValue = "1") Integer pageIndex,
                                        @ApiParam("单页记录数") @RequestParam(value = "size", defaultValue = "12") Integer size) {
        User user = authenticationFacade.getPrincipal(User.class);
        QueryWrapper<Expose> wrapper = new QueryWrapper<>();
        wrapper.eq("expose_user_id", user.getUserId());
        return exposeService.page(new Page<>(pageIndex, size), wrapper);
    }

}
