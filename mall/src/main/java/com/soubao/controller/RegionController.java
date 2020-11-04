package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soubao.entity.Ad;
import com.soubao.entity.Region;
import com.soubao.service.RegionService;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Api(value = "地区控制器", tags = {"地区相关接口"})
@RequestMapping("/region")
public class RegionController {
    @Autowired
    private RegionService regionService;

    @GetMapping("/list")
    @ApiOperation(value = "地区列表")
    public List<Region> regions(@ApiParam("上级id,为0表示获取一级地区") @RequestParam(value = "parent_id", required = false) Integer parenId,
                                @ApiParam("主键组合,用户获取省/市/区") @RequestParam(value = "ids", required = false) Set<Integer> ids,
                                @ApiParam("地区名称，模糊查询") @RequestParam(value = "name", required = false) String name) {
        QueryWrapper<Region> regionQueryWrapper = new QueryWrapper<>();
        regionQueryWrapper.orderByAsc("level");
        if (parenId != null) {
            regionQueryWrapper.eq("parent_id", parenId);
        }
        if (ids != null) {
            regionQueryWrapper.in("id", ids);
        }
        if (StringUtils.isNotEmpty(name)) {
            regionQueryWrapper.eq("name", name);
        }
        return regionService.list(regionQueryWrapper);
    }

    @GetMapping("/parent/list")
    @ApiOperation(value = "地区列表")
    public List<Region> regions(@ApiParam("主键组合,用户获取省/市/区") @RequestParam(value = "id") Set<Integer> id) {
        List<Region> regionsLevel3 = regionService.list(
                (new QueryWrapper<Region>()).in("id", id).eq("level", 3));
        Set<Integer> level2Id = regionsLevel3.stream().map(Region::getParentId).collect(Collectors.toSet());
        level2Id.addAll(id);
        List<Region> regionsLevel2and1 = regionService.list((new QueryWrapper<Region>()).in("id", level2Id)
                .eq("level", 2).or().eq("level", 1));
        regionsLevel2and1.addAll(regionsLevel3);
        return regionsLevel2and1;
    }

    @PostMapping
    @ApiOperation(value = "新增地区")
    public SBApi add(@ApiParam("地区对象") @Valid @RequestBody Region region) {
        regionService.save(region);
        return new SBApi();
    }

    @DeleteMapping
    @ApiOperation(value = "删除地区")
    public SBApi remove(@ApiParam("地区ID") @RequestParam("id") Integer id) {
        regionService.removeAddress(id);
        return new SBApi();
    }

    @GetMapping("parent_name_full")
    @ApiOperation(value = "获取完整的上级地址名称")
    public List<String> parentNameFull(@RequestParam("parent_id") Integer parentId) {
        LinkedList<String> parentNameFull = new LinkedList<>();
        Region region = regionService.getById(parentId);
        parentNameFull.addFirst(region.getName());
        while (region.getLevel() != 1) {
            region = regionService.getById(region.getParentId());
            parentNameFull.addFirst(region.getName());
        }
        return parentNameFull;
    }

    @GetMapping("address")
    String getAddress(@RequestParam(value = "country", required = false) Integer country,
                      @RequestParam(value = "province", required = false) Integer province,
                      @RequestParam(value = "city", required = false) Integer city,
                      @RequestParam(value = "district", required = false) Integer district,
                      @RequestParam(value = "twon", required = false) Integer twon) {
        Region countryS = null;
        if (country != null) {
            countryS = regionService.getById(country);
        }
        Region provinceS = null;
        if (province != null) {
            provinceS = regionService.getById(province);
        }
        Region cityS = null;
        if (city != null) {
            cityS = regionService.getById(city);
        }
        Region districtS = null;
        if (district != null) {
            districtS = regionService.getById(district);
        }

        Region twonS = null;
        if (twon != null) {
            twonS = regionService.getById(twon);
        }

        return (countryS == null ? "" : countryS.getName()) +
                (provinceS == null ? "" : provinceS.getName()) +
                (cityS == null ? "" : cityS.getName()) +
                (districtS == null ? "" : districtS.getName()) +
                (twonS == null ? "" : twonS.getName());
    }

}
