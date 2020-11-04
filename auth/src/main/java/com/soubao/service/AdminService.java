package com.soubao.service;

import com.soubao.entity.Admin;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dyr
 * @since 2020-03-13
 */
@FeignClient("admin")
public interface AdminService {

    @GetMapping("credential")
    Admin getByUsername(@RequestParam("user_name") String userName);
}
