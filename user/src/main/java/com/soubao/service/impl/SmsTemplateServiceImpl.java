package com.soubao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.common.constant.SendSceneMap;
import com.soubao.dao.SmsTemplateMapper;
import com.soubao.entity.SmsTemplate;
import com.soubao.service.SmsTemplateService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 短信模板配置表 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-10-18
 */
@Service("smsTemplateService")
public class SmsTemplateServiceImpl extends ServiceImpl<SmsTemplateMapper, SmsTemplate> implements SmsTemplateService {

    @Override
    public List<Map<String, String>> getSendScene() {
        Map<String, String> map = SendSceneMap.getSendScenes();
        List<Map<String, String>> mapList = new ArrayList<>();
        Set<String> smsTemplateSet = list().stream().map(SmsTemplate::getSendScene).collect(Collectors.toSet());
        map.forEach((key, val) -> {
            if (!smsTemplateSet.contains(key)) {
                Map<String, String> hashMap = new HashMap<>();
                hashMap.put("value", key);
                hashMap.put("label", val);
                mapList.add(hashMap);
            }
        });
        return mapList;
    }
}
