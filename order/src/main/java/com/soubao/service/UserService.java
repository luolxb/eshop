package com.soubao.service;

import com.soubao.common.vo.SBApi;
import com.soubao.entity.AccountLog;
import com.soubao.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@FeignClient(name = "user")
public interface UserService {

    @GetMapping("/admin/user")
    User getById(@RequestParam(value = "user_id") Integer userId);

    @GetMapping("/user_1")
    User getById_1(@RequestParam(value = "user_id") Integer userId);

    @GetMapping("list")
    List<User> usersByIds(@RequestParam(value = "user_ids") Set<Integer> userIds);

    @GetMapping("/ids")
    Set<Integer> userIdsByName(@RequestParam(value = "nickname") String nickname);

    @PostMapping("/account_log")
    String addAccountLog(@RequestBody AccountLog accountLog);

    @GetMapping("/user")
    User getUserInfoById(@RequestParam(value = "user_id") Integer userId);


    @GetMapping("/user1")
    User getUserInfoById1(@RequestParam(value = "user_id") Integer userId);

    @GetMapping("/current")
    User getUserCurrent();

    @PutMapping
    SBApi user(@RequestBody User RequestUser);

    @PutMapping("/update")
    SBApi userUpdate(@RequestBody User requestUser);
}
