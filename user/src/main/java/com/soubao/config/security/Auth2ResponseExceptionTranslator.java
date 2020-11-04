package com.soubao.config.security;

import com.soubao.common.exception.ResultEnum;
import com.soubao.common.vo.SBApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

@Slf4j
public class Auth2ResponseExceptionTranslator implements WebResponseExceptionTranslator {

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) {
        log.error("Auth2异常");
        Throwable throwable = e.getCause();
        SBApi sbApi = new SBApi();
        if (throwable instanceof InvalidTokenException) {
            sbApi.setStatus(ResultEnum.INVALID_TOKEN.getCode());
            sbApi.setMsg(ResultEnum.INVALID_TOKEN.getMsg());
            log.info("token失效");
            return new ResponseEntity(sbApi, HttpStatus.OK);
        }
        sbApi.setStatus(ResultEnum.UNKNOWN_ERROR.getCode());
        sbApi.setMsg(ResultEnum.UNKNOWN_ERROR.getMsg());
        return new ResponseEntity(sbApi, HttpStatus.METHOD_NOT_ALLOWED);
    }
}