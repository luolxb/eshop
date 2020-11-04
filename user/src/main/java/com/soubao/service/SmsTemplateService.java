package com.soubao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.SmsTemplate;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 短信模板配置表 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-10-18
 */
public interface SmsTemplateService extends IService<SmsTemplate> {
    List<Map<String, String>> getSendScene();
}
