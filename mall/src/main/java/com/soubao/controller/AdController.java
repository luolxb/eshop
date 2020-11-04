package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.Ad;
import com.soubao.service.AdService;
import com.soubao.common.utils.TimeUtil;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "广告接口")
@RestController
@RequestMapping("ad")
public class AdController {
    @Autowired
    private AdService adService;

    @GetMapping("page")
    @ApiOperation("广告分页")
    public IPage<Ad> ads(@ApiParam(name = "start_pid", value = "广告起始id") @RequestParam(value = "start_pid", required = false) Integer startPid,
                         @ApiParam(name = "end_pid", value = "广告截止id") @RequestParam(value = "end_pid", required = false) Integer endPid,
                         @ApiParam(name = "pid", value = "广告位置id") @RequestParam(value = "pid", required = false) Integer pid,
                         @ApiParam(name = "page", value = "页码") @RequestParam(value = "page", defaultValue = "1") Integer page,
                         @ApiParam(name = "size", value = "单页记录数") @RequestParam(value = "size", defaultValue = "1") Integer size) {
        Long hourTimestamp = TimeUtil.getTimestampForHour();
        QueryWrapper<Ad> adQueryWrapper = new QueryWrapper<>();
        adQueryWrapper.select("pid,ad_link,ad_code,media_type").eq("enabled", 1)
                .lt("start_time", hourTimestamp).gt("end_time", hourTimestamp);
        if (pid != null) {
            adQueryWrapper.eq("pid", pid);
        }
        if (startPid != null && endPid != null && endPid > startPid) {
            adQueryWrapper.gt("pid", startPid).lt("pid", endPid);
        }
        return adService.page(new Page<>(page, size), adQueryWrapper);
    }

    @GetMapping("page/admin")
    @ApiOperation("管理后台广告分页")
    public IPage<Ad> ads(@ApiParam(name = "pid", value = "广告id") @RequestParam(value = "pid", required = false) Integer pid,
                         @ApiParam(name = "ad_name", value = "广告名称") @RequestParam(value = "ad_name", required = false) String adName,
                         @ApiParam(name = "page", value = "页码") @RequestParam(value = "page", defaultValue = "1") Integer page,
                         @ApiParam(name = "size", value = "单页记录数") @RequestParam(value = "size", defaultValue = "1") Integer size) {
        QueryWrapper<Ad> adQueryWrapper = new QueryWrapper<>();
        if (pid != null && pid > 0) {
            adQueryWrapper.eq("pid", pid);
        }
        if (!StringUtils.isEmpty(adName)) {
            adQueryWrapper.like("ad_name", adName);
        }
        IPage<Ad> pageAd = adService.page(new Page<>(page, size), adQueryWrapper.orderByDesc("pid"));
        adService.withADPosition(pageAd.getRecords());
        return pageAd;
    }

    @GetMapping("list")
    @ApiOperation("广告列表")
    public List<Ad> ads(@ApiParam("广告起始id") @RequestParam(value = "start_pid", required = false) Integer startPid,
                        @ApiParam("广告截止id") @RequestParam(value = "end_pid", required = false) Integer endPid,
                        @ApiParam("广告位置id") @RequestParam(value = "pid", required = false) Integer pid,
                        @ApiParam("单页记录数") @RequestParam(value = "size", defaultValue = "1") Integer size) {
        QueryWrapper<Ad> adQueryWrapper = new QueryWrapper<>();
        Long hourTimestamp = TimeUtil.getTimestampForHour();
        adQueryWrapper.select("pid,ad_link,ad_code,media_type,bgcolor,target").eq("enabled", 1)
                .lt("start_time", hourTimestamp).gt("end_time", hourTimestamp).last("limit " + size);
        if (pid != null) {
            adQueryWrapper.eq("pid", pid);
        }
        if (startPid != null && endPid != null && endPid > startPid) {
            adQueryWrapper.gt("pid", startPid).lt("pid", endPid);
        }
        return adService.list(adQueryWrapper);
    }

    @GetMapping
    @ApiOperation("获取某个广告")
    public Ad getAd(@ApiParam("广告id") @RequestParam("ad_id") Integer adId) {
        return adService.getById(adId);
    }

    @PutMapping
    @ApiOperation("更新广告")
    public SBApi updateTarget(@ApiParam("广告对象") @Valid @RequestBody Ad ad) {
        adService.updateAd(ad);
        return new SBApi();
    }
}
