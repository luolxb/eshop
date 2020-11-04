package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.*;
import com.soubao.service.FreightConfigService;
import com.soubao.service.FreightRegionService;
import com.soubao.service.FreightTemplateService;
import com.soubao.service.GoodsService;
import com.soubao.service.impl.AuthenticationFacade;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@ApiModel(value = "运费模板接口", description = "运费模板相关接口")
@RestController
@Slf4j
@RequestMapping("/freight_template")
public class FreightTemplateController {
    @Autowired
    private FreightTemplateService freightTemplateService;
    @Autowired
    private FreightRegionService freightRegionService;
    @Autowired
    private FreightConfigService freightConfigService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private AuthenticationFacade authenticationFacade;

    @ApiOperation(value = "获取运费模板列表分页")
    @GetMapping("page")
    public IPage<FreightTemplate> getPage(@ApiParam("店铺id") @RequestParam("store_id") Integer storeId,
                                          @ApiParam("页码") @RequestParam(value = "p", defaultValue = "1") Integer page,
                                          @ApiParam("单页记录数") @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return freightTemplateService.getPageWithConfig(new Page<>(page, size), storeId);
    }

    @PreAuthorize("hasAnyRole('ROLE_SELLER')")
    @ApiOperation("获取店铺运费模板列表")
    @GetMapping("list")
    public List<FreightTemplate> list(){
        Seller seller = authenticationFacade.getPrincipal(Seller.class);
        return freightTemplateService.list(new QueryWrapper<FreightTemplate>().eq("store_id", seller.getStoreId()));
    }

    @GetMapping("/{template_id}")
    public FreightTemplate getFreightTemplate(@PathVariable(value = "template_id") Integer templateId){
        return freightTemplateService.getOneWithConfigById(templateId);
    }

    @ApiOperation(value = "删除运费模板")
    @DeleteMapping("/{template_id}")
    public SBApi deleteFreightTemplate(@ApiParam("主键") @PathVariable(value = "template_id") Integer templateId){
        goodsService.update((new UpdateWrapper<Goods>()).eq("template_id", templateId)
                .set("template_id", 0).set("is_free_shipping", 1));
        freightRegionService.remove((new QueryWrapper<FreightRegion>()).eq("template_id", templateId));
        freightConfigService.remove((new QueryWrapper<FreightConfig>()).eq("template_id", templateId));
        freightTemplateService.removeById(templateId);
        return new SBApi();
    }

    @ApiOperation(value = "添加运费模板")
    @PostMapping
    public SBApi addFreightTemplate(@Valid @RequestBody FreightTemplate freightTemplate){
        freightTemplateService.save(freightTemplate);
        for (FreightConfig freightConfig : freightTemplate.getFreightConfigList()) {
            freightConfig.setTemplateId(freightTemplate.getTemplateId());
        }
        freightConfigService.saveBatch(freightTemplate.getFreightConfigList());
        List<FreightRegion> freightRegionList = new ArrayList<>();
        for(FreightConfig freightConfig : freightTemplate.getFreightConfigList()){
            for (FreightRegion freightRegion : freightConfig.getFreightRegionList()){
                freightRegion.setConfigId(freightConfig.getConfigId());
                freightRegion.setTemplateId(freightTemplate.getTemplateId());
                freightRegionList.add(freightRegion);
            }
        }
        freightRegionService.saveBatch(freightRegionList);
        return new SBApi();
    }

    @ApiOperation(value = "修改运费模板")
    @PutMapping
    public SBApi updateFreightTemplate(@Valid @RequestBody FreightTemplate freightTemplate){
        freightTemplateService.updateWithConfigAndRegion(freightTemplate);
        return new SBApi();
    }
}
