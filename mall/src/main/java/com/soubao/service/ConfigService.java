package com.soubao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.Config;

import java.util.List;
import java.util.Map;

public interface ConfigService extends IService<Config> {

    /**
     * 保存配置
     * @param incType
     * @param configs
     */
    void saveConfigs(String incType, Map<String, String> configs);

    /**
     * 获取配置
     * @return
     */
    Map<Object, Object> getConfigMap();

    /**
     * 分组配置
     * @param configs
     * @return
     */
    Map<String, Map<String, String>> configListToMap(List<Config> configs);
}
