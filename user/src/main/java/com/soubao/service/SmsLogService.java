package com.soubao.service;

import com.soubao.entity.SmsLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dyr
 * @since 2020-04-30
 */
public interface SmsLogService extends IService<SmsLog> {

    void send(SmsLog smsLog, Map<Object, Object> configMap);

    void verify(SmsLog smsLog, Map<Object, Object> configMap);
}
