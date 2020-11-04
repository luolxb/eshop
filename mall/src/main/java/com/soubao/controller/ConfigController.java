package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soubao.entity.Config;
import com.soubao.service.ConfigService;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(value = "配置控制器", tags = {"配置相关接口"})
@RestController
public class ConfigController {
    @Autowired
    private ConfigService configService;

    @ApiOperation("获取所有配置")
    @GetMapping("config")
    public Map<Object, Object> configs() {
        return configService.getConfigMap();
    }

    @ApiOperation("获取分组配置")
    @GetMapping("configs")
    public Map<String, Map<String, String>> configs(@ApiParam("配置类型") @RequestParam(value = "inc_type", required = false) String incType,
                                                    @ApiParam("配置名称") @RequestParam(value = "name", required = false) String name) {
        QueryWrapper<Config> queryWrapper = new QueryWrapper<Config>().orderByDesc("inc_type");
        if (StringUtils.isNotEmpty(incType)) {
            queryWrapper.eq("inc_type", incType);
        }
        if (StringUtils.isNotEmpty(name)) {
            queryWrapper.eq("name", name);
        }
        List<Config> configs = configService.list(queryWrapper);
        return configService.configListToMap(configs);
    }

    @ApiOperation("修改配置")
    @PutMapping("configs/{inc_type}")
    public SBApi updateConfig(@PathVariable("inc_type") String incType,
                              @RequestBody Map<String, String> configs) {
        configService.saveConfigs(incType, configs);
        return new SBApi();
    }

    @ApiOperation("获取系统时间")
    @GetMapping("system_time")
    public Long getSystemTime() {
        return System.currentTimeMillis() / 1000;
    }

}
