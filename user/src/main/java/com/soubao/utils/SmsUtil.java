package com.soubao.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.soubao.common.utils.HttpClientUtil;
import com.soubao.entity.SmsLog;
import com.soubao.entity.SmsTemplate;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class SmsUtil {

    public static JSONObject sendSmsByCloudSp(String accesskey, String secret, SmsLog smsLog, SmsTemplate smsTemplate) {
        String requestUrl = "http://api.1cloudsp.com/api/v2/send";
        Map<String, String> requestUrlParam = new HashMap<>();
        requestUrlParam.put("accesskey", accesskey);
        requestUrlParam.put("secret", secret);
        requestUrlParam.put("sign", smsTemplate.getSmsSign());
        requestUrlParam.put("templateId", smsTemplate.getSmsTplCode());
        requestUrlParam.put("mobile", smsLog.getMobile());
        //URLEncoder.encode("先生##9:40##快递公司##1234567", "utf-8")
        requestUrlParam.put("content",  URLEncoder.encode(smsLog.getCode()));
        return JSON.parseObject(HttpClientUtil.doGet(requestUrl, requestUrlParam));
    }

    public static JSONObject sendSmsByAli(String accesskey, String secret, SmsLog smsLog, SmsTemplate smsTemplate) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accesskey, secret);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", smsLog.getMobile());
        request.putQueryParameter("SignName", smsTemplate.getSmsSign());
        request.putQueryParameter("TemplateCode", smsTemplate.getSmsTplCode());
        request.putQueryParameter("TemplateParam", JSON.toJSONString(SmsLog.builder().code(smsLog.getCode()).build()));
        JSONObject result = null;
        try {
            CommonResponse response = client.getCommonResponse(request);
            result = JSON.parseObject(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return result;
    }
}
