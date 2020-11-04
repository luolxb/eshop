package com.soubao.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

/**
 * 服务类
 *
 * @author dyr
 * @since 2019-08-15
 */
@FeignClient("mall")
public interface MallService {
    @GetMapping("config")
    Map<Object, Object> config();
}
