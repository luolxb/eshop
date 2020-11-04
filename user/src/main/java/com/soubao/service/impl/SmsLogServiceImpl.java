package com.soubao.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soubao.entity.SmsLog;
import com.soubao.dao.SmsLogMapper;
import com.soubao.entity.SmsTemplate;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.MallService;
import com.soubao.service.SmsLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.service.SmsTemplateService;
import com.soubao.utils.SmsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dyr
 * @since 2020-04-30
 */
@Slf4j
@Service
public class SmsLogServiceImpl extends ServiceImpl<SmsLogMapper, SmsLog> implements SmsLogService {

    @Autowired
    private SmsTemplateService smsTemplateService;

    @Override
    public void send(SmsLog smsLog, Map<Object, Object> configMap) {
        String regisSmsEnable = (String) configMap.get("sms_regis_sms_enable");
        if(StringUtils.isEmpty(regisSmsEnable) || (Integer.parseInt(regisSmsEnable) != 1)){
            throw new ShopException(ResultEnum.NO_USE_MSG_REG);
        }
        SmsTemplate smsTemplate = smsTemplateService.getOne((new QueryWrapper<SmsTemplate>()).eq("send_scene", smsLog.getScene()));
        if(smsTemplate == null){
            throw new ShopException(ResultEnum.NO_EXIST_SMS_TEMP);
        }
        SmsLog lastSmsLog = this.getOne((new QueryWrapper<SmsLog>().eq("mobile", smsLog.getMobile())
                .eq("status", 1)).orderByDesc("id").last("limit 1"));
        if(lastSmsLog != null && (System.currentTimeMillis() / 1000 - lastSmsLog.getAddTime())
                < Long.parseLong((String) configMap.get("sms_sms_time_out"))){
            throw new ShopException(-10084, configMap.get("sms_sms_time_out") + "秒内不允许重复发送");
        }
        smsLog.setAddTime(System.currentTimeMillis() / 1000);
        smsLog.setCode(String.valueOf(((int)(Math.random() * 8999) + 1000)));//生成 [m,n] 的数字
        //if(Integer.parseInt((String) configMap.get("sms_sms_platform")) == 1){
            //阿里云
            JSONObject sendSmsResult = SmsUtil.sendSmsByAli((String) configMap.get("sms_sms_appkey"),
                    (String) configMap.get("sms_sms_secretKey"), smsLog, smsTemplate);
            log.info("阿里云发短信：{}", sendSmsResult);
            if(sendSmsResult.getString("Message").equals("OK")){
                smsLog.setStatus(1);
                this.save(smsLog);
            }else{
                smsLog.setErrorMsg("发送失败:" + sendSmsResult.getString("Message")+",错误代码：" + sendSmsResult.getString("Code"));
                throw new ShopException(-10085, smsLog.getErrorMsg());
            }
//        }else{
//            //天瑞云
//            JSONObject sendSmsResult = SmsUtil.sendSmsByCloudSp((String) configMap.get("sms_sms_appkey"),
//                    (String) configMap.get("sms_sms_secretKey"), smsLog, smsTemplate);
//            if(StringUtils.isEmpty(sendSmsResult.getString("code")) || Integer.parseInt(sendSmsResult.getString("code")) != 0){
//                smsLog.setErrorMsg("发送失败:"+sendSmsResult.getString("msg")+",错误代码："+sendSmsResult.getString("code"));
//                this.save(smsLog);
//                throw new ShopException(-10085, smsLog.getErrorMsg());
//            }else{
//                smsLog.setStatus(1);
//                this.save(smsLog);
//            }
//        }

    }

    @Override
    public void verify(SmsLog smsLog, Map<Object, Object> configMap) {
        String regisSmsEnable = (String) configMap.get("sms_regis_sms_enable");
        if(StringUtils.isEmpty(regisSmsEnable) || (Integer.parseInt(regisSmsEnable) != 1)){
            //未开启功能不做校验验证码
            return ;
        }
        SmsLog lastSmsLog = this.getOne((new QueryWrapper<SmsLog>()).eq("mobile", smsLog.getMobile()).eq("scene", smsLog.getScene())
                .orderByDesc("id").last("limit 1"));
        if(lastSmsLog == null){
            throw new ShopException(ResultEnum.NO_VERIFY_CODE);
        }
        if((lastSmsLog.getAddTime() + Long.parseLong((String) configMap.get("sms_sms_time_out"))) < System.currentTimeMillis() / 1000){
            throw new ShopException(ResultEnum.VERIFY_CODE_TIME_OUT);
        }
        if(!lastSmsLog.getCode().equals(smsLog.getCode())){
            throw new ShopException(ResultEnum.VERIFY_CODE_ERROR);
        }
    }

}
