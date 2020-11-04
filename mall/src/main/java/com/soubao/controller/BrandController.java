package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.Brand;
import com.soubao.entity.BrandType;
import com.soubao.service.BrandService;
import com.soubao.service.BrandTypeService;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("brand")
@Api(value = "品牌控制器", tags = {"品牌相关接口"})
public class BrandController {
    @Autowired
    private BrandService brandService;
    @Autowired
    private BrandTypeService brandTypeService;

    @GetMapping("page")
    @ApiOperation(value = "获取品牌分页")
    public IPage<Brand> brandPage(
            @ApiParam("热门") @RequestParam(value = "is_hot", required = false) Integer isHot,
            @ApiParam("0正常 1审核中 2审核失败 审核状态") @RequestParam(value = "status", required = false) Integer status,
            @ApiParam("品牌名称") @RequestParam(value = "name", required = false) String name,
            @ApiParam("店铺id") @RequestParam(value = "store_id", required = false) Integer storeId,
            @RequestParam(value = "p", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<Brand> brandQueryWrapper = new QueryWrapper<>();
        if (isHot != null){
            brandQueryWrapper.eq("is_hot", isHot);
        }
        if(status != null){
            brandQueryWrapper.eq("status", status);
        }
        if(!StringUtils.isEmpty(name)){
            brandQueryWrapper.like("name", "%" + name + "%");
        }
        if(storeId != null){
            brandQueryWrapper.eq("store_id", storeId);
        }
        brandQueryWrapper.eq("status", 0).orderByDesc("sort").orderByAsc("id");
//        brandService.getBrandPage(new Page<>(page, size), brandQueryWrapper);
        return brandService.getBrandPage(new Page<>(page, size), brandQueryWrapper);
    }

    @ApiOperation(value = "获取品牌列表")
    @GetMapping("list")
    public List<Brand> list(@ApiParam("商品分类id") @RequestParam(value = "cat_id", required = false) Set<Integer> catIds,
                            @ApiParam("店铺id") @RequestParam(value = "store_id", required = false) Set<Integer> storeId) {
        QueryWrapper<Brand> queryWrapper = new QueryWrapper<>();
        if (catIds != null) {
            queryWrapper.and(i -> i.in("cat_id1", catIds).or().in("cat_id2", catIds).or().in("cat_id3", catIds));
        }
        if (storeId != null) {
            queryWrapper.in("store_id", storeId);
        }
        return brandService.list(queryWrapper);
    }

    @GetMapping
    public Brand brand(@RequestParam("id")Integer id){
        return brandService.getById(id);
    }

    @PostMapping
    public SBApi add(@Valid @RequestBody Brand brand){
        brandService.save(brand);
        return new SBApi();
    }

    @PutMapping
    public SBApi update(@Valid @RequestBody Brand brand){
        brandService.updateById(brand);
        return new SBApi();
    }

    @DeleteMapping
    public SBApi remove(@RequestParam("ids")Set<Integer> ids){
        brandService.removeBrandWithIds(ids);
        return new SBApi();
    }

    @GetMapping("types")
    public List<BrandType> brandTypes(@RequestParam("type_id") Integer typeId){
        return brandTypeService.list((new QueryWrapper<BrandType>()).eq("type_id", typeId));
    }

    @GetMapping("count")
    public Integer brandTypes(@RequestParam(value = "store_id", required = false) Integer storeId,
                              @RequestParam(value = "status", required = false) Set<Integer> status){
        QueryWrapper<Brand> brandQueryWrapper = new QueryWrapper<>();
        brandQueryWrapper.gt("store_id", 0);
        if(storeId != null){
            brandQueryWrapper.eq("store_id", 0);
        }
        if(status != null){
            brandQueryWrapper.in("status", status);
        }
        return brandService.count(brandQueryWrapper);
    }
}
