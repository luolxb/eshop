package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.Shipping;
import com.soubao.entity.StoreShipping;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.ShippingService;
import com.soubao.service.StoreShippingService;
import com.soubao.common.vo.SBApi;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author dyr
 * @since 2019-12-10
 */
@RestController
@RequestMapping("/shipping")
public class ShippingController {
    @Autowired
    private ShippingService shippingService;

    @Autowired
    private StoreShippingService storeShippingService;

    @GetMapping("/list")
    public List<Shipping> getShippingList(@RequestParam(value = "store_id", required = false) Integer storeId) {
        QueryWrapper<Shipping> shippingQueryWrapper = new QueryWrapper<>();
        if (null != storeId) {
            List<StoreShipping> storeShippingList = storeShippingService.list(new QueryWrapper<StoreShipping>()
                    .eq("store_id", storeId));
            Set<Integer> shippingIds = storeShippingList.stream().map(StoreShipping::getShippingId).collect(Collectors.toSet());
            shippingQueryWrapper.in("shipping_id", shippingIds);
        }
        return shippingService.list(shippingQueryWrapper);
    }

    @GetMapping("/page")
    public IPage<Shipping> getShipping(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                       @RequestParam(value = "size", defaultValue = "10") Integer size,
                                       @RequestParam(value = "shipping_name", required = false) String shippingName,
                                       @RequestParam(value = "shipping_code", required = false) String shippingCode) {
        QueryWrapper<Shipping> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(shippingName)) {
            queryWrapper.like("shipping_name", shippingName);
        }
        if (StringUtils.isNotEmpty(shippingCode)) {
            queryWrapper.like("shipping_code", shippingCode);
        }
        return shippingService.page(new Page<>(page, size), queryWrapper);
    }

    @GetMapping
    public Shipping getOne(@RequestParam(value = "shipping_id") Integer shippingId) {
        return shippingService.getById(shippingId);
    }

    @PutMapping
    public SBApi update(@Valid @RequestBody Shipping shipping) {
        String escape = HtmlUtils.htmlEscape(shipping.getHtmlEncode());
        shipping.setTemplateHtml(escape);
        shippingService.updateById(shipping);
        return new SBApi();
    }

    @DeleteMapping
    public SBApi delete(@RequestParam("ids") Set<Integer> ids) {
        shippingService.removeByIds(ids);
        return new SBApi();
    }

    @PostMapping
    public SBApi save(@Valid @RequestBody Shipping shipping) {
        if (StringUtils.isNotEmpty(shipping.getHtmlEncode())) {
            String escape = HtmlUtils.htmlEscape(shipping.getHtmlEncode());
            shipping.setTemplateHtml(escape);
        } else {
            throw new ShopException(ResultEnum.SHIPPING_TEMPLATE_ERROR);
        }
        shippingService.save(shipping);
        return new SBApi();
    }

    @GetMapping("list/print")
    public List<Shipping> shippingPrint(@RequestParam("ids") Set<Integer> ids) {
        return shippingService.shippingPrint(ids);
    }
}
