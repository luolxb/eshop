package com.soubao.service;

import com.soubao.common.vo.SBApi;
import com.soubao.entity.User;
import com.soubao.entity.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Set;

@FeignClient(name = "user")
public interface UserService {
    @GetMapping("/seller/user")
    User getUserById(@RequestParam(value = "user_id") Integer userId);

    @GetMapping("/credential")
    User getUserByMobileOrEmail(@RequestParam(value = "mobile", required = false) String mobile,
                                @RequestParam(value = "email", required = false) String email);

    @GetMapping("/get/user_id")
    Integer getUserIdByMobileOrEmail(@RequestParam(value = "mobile", required = false) String mobile,
                                @RequestParam(value = "email", required = false) String email);

    @PostMapping
    void addUser(@RequestBody User user);

    @PostMapping("in-seller/add")
    Integer addInnerSeller(@RequestBody UserInfo user) ;

    @PostMapping
    void addSeller(@RequestBody User user) ;

    @PutMapping("/store_member")
    void updateStoreMember(@RequestParam("user_id") Integer userId,
                           @RequestParam(value = "store_id", defaultValue = "1") Integer storeId);

    @PutMapping("admin/user")
    String updateUser(@RequestBody User user);

    @GetMapping("list")
    List<User> users(@RequestParam("user_ids") Set<Integer> userIds);

    @GetMapping("store_member/count/group")
    Map<Integer, Integer> getStoreMemberCountGroup(@RequestParam("store_ids")Set<Integer> storeIds);

    @GetMapping("store_member/ids")
    Map<Integer, Set<Integer>> getUserIdsByIsStoreMember(@RequestParam("store_ids")Set<Integer> storeIds);

    @GetMapping("ids")
    Set<Integer> userIdsByMobile(@RequestParam(value = "mobile") String mobile);

    @GetMapping("ids")
    Set<Integer> userIdsByNickname(@RequestParam(value = "nickname") String nickname);

    @GetMapping("check")
    Integer checkUserByNickname(@RequestParam(value = "nickname") String nickname);

    @GetMapping("check")
    Integer checkUserByMobile(@RequestParam(value = "mobile") String mobile);
}
