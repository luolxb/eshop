package com.soubao.service;

import com.soubao.entity.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-08-29
 */
@FeignClient(name = "user")
public interface UserService {

    @GetMapping("/address")
    UserAddress getUserAddress(@RequestParam(value = "id") Integer addressId);

    @PutMapping("/store")
    void updateStore(@RequestBody UserStore userStore);

    @GetMapping("/store")
    UserStore getUserStore();

    @GetMapping("/current")
    User getUserCurrent();

    @GetMapping("/list")
    List<User> listByIds(@RequestParam(value = "user_ids") Set<Integer> ids);

}
