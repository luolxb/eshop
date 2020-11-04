package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soubao.entity.GoodsAttr;
import com.soubao.entity.GoodsAttribute;
import com.soubao.service.GoodsAttrService;
import com.soubao.service.GoodsAttributeService;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "商品属性")
@RestController
@RequestMapping("/goods_attribute")
public class GoodsAttributeController {
    @Autowired
    private GoodsAttributeService goodsAttributeService;
    @Autowired
    private GoodsAttrService goodsAttrService;

    @GetMapping(value = "/list")
    @ApiOperation(value = "获取商品属性", notes = "获取商品属性列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "p", value = "页码", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "size", value = "条数", paramType = "query", defaultValue = "10"),
    })
    @ApiResponse(code = 400, message = "商品属性列表", response = GoodsAttribute.class)
    public List<GoodsAttribute> getGoodsAttributes(@RequestParam(value = "type_id", required = false) Integer typeId){
        QueryWrapper<GoodsAttribute> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(typeId)) {
            queryWrapper.eq("type_id", typeId).orderByAsc("attr_input_type");
        }
        return goodsAttributeService.list(queryWrapper);
    }

    @DeleteMapping("/{attr_id}")
    @ApiOperation(value = "删除商品属性", notes = "删除商品属性")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "attr_id", value = "主键", paramType = "path"),
    })
    @ApiResponse(code = 400, message = "返回消息体", response = SBApi.class)
    public SBApi deleteGoodsAttribute(@PathVariable(value = "attr_id") Integer attrId, SBApi sbApi){
        goodsAttributeService.removeById(attrId);
        goodsAttrService.remove((new QueryWrapper<GoodsAttr>()).eq("attr_id", attrId));
        return sbApi;
    }
}
