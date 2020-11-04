package com.soubao.config.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Configuration
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            // 这里直接拿到我们抛出的异常信息
            String message = Util.toString(response.body().asReader());
            // 拦截oauth登录失败的信息
            JSONObject jsonObject = JSON.parseObject(message);
            if(!StringUtils.isEmpty(jsonObject) && jsonObject.containsKey("error_description")){
                message = String.valueOf(jsonObject.get("error_description"));
                return new ShopException(ResultEnum.LOGIN_ERROR.getCode(), message);
            }
            return new RuntimeException(message);
        } catch (IOException ignored) {
        }
        return decode(methodKey, response);
    }
}