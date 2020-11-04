package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.entity.Config;
import com.soubao.service.ConfigService;
import com.soubao.dao.ConfigMapper;
import com.soubao.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("configService")
@Slf4j
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void saveConfigs(String incType, Map<String, String> configs) {
        List<Config> configList = list((new QueryWrapper<Config>()).eq("inc_type", incType));
        List<Config> newConfigList = new ArrayList<>();
        if(configList.size() > 0){
            List<Config> updateConfigList = new ArrayList<>();
            Map<String, Config> configsMap = configList.stream().collect(Collectors.toMap(Config::getName, config -> config));
            for (Map.Entry<String, String> config : configs.entrySet()) {
                if(configsMap.containsKey(config.getKey())){
                    Config tempConfig = configsMap.get(config.getKey());
                    if(!tempConfig.getValue().equals(config.getValue())){
                        tempConfig.setValue(config.getValue());
                        updateConfigList.add(tempConfig);
                    }
                }else{
                    Config newConfig = new Config();
                    newConfig.setIncType(incType);
                    newConfig.setName(config.getKey());
                    newConfig.setValue(config.getValue());
                    newConfigList.add(newConfig);
                }
            }
            if(updateConfigList.size() > 0){
                updateBatchById(updateConfigList);
            }
        }else{
            for (Map.Entry<String, String> config : configs.entrySet()) {
                Config newConfig = new Config();
                newConfig.setIncType(incType);
                newConfig.setName(config.getKey());
                newConfig.setValue(config.getValue());
                newConfigList.add(newConfig);
            }
        }
        if(newConfigList.size() > 0){
            saveBatch(newConfigList);
        }
        redisUtil.del("config");
    }

    @Override
    public Map<Object, Object> getConfigMap() {
        if(!redisUtil.hasKey("config")){
            List<Config> configs = list();
            Map<String, Object> tempConfigMap = new HashMap<>();
            for (Config config : configs){
                tempConfigMap.put(config.getIncType() + "_" + config.getName(), config.getValue());
            }
            redisUtil.hmset("config", tempConfigMap);
        }
        return redisUtil.hmget("config");
    }

    @Override
    public Map<String, Map<String, String>> configListToMap(List<Config> configs){
        Map<String, Map<String, String>> configsMap = new HashMap<>();
        for (Config config : configs){
            if(configsMap.containsKey(config.getIncType())){
                configsMap.get(config.getIncType()).put(config.getName(), config.getValue());
            }else{
                Map<String, String> configMap = new HashMap<>();
                configMap.put(config.getName(), config.getValue());
                configsMap.put(config.getIncType(), configMap);
            }
        }
        return configsMap;
    }
}
