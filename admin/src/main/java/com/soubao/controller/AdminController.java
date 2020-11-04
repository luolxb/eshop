package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.Admin;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.impl.AuthenticationFacade;
import com.soubao.service.AdminService;
import com.soubao.vo.ModifyPasswordVo;
import com.soubao.common.vo.SBApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 前端控制器
 *
 * @author dyr
 * @since 2019-11-04
 */
@Slf4j
@RestController
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Value("${security.salt}")
    private String salt;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/credential")
    public Admin credential(@RequestParam("user_name") String userName) {
        return adminService.getOne(new QueryWrapper<Admin>().select("admin_id,user_name,password").eq("user_name", userName));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/current")
    public Admin getSelf() {
        return adminService.getById(authenticationFacade.getPrincipal(Admin.class).getAdminId());
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/{admin_id}")
    public Admin getAdmin(@PathVariable(value = "admin_id") Integer adminId) {
        return adminService.getOne(new QueryWrapper<Admin>().select("admin_id,user_name,email").eq("admin_id", adminId));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("open_teach")
    public SBApi updateOpenTeach(@RequestParam("open_teach") Integer openTeach) {
        adminService.update(new UpdateWrapper<Admin>().set("open_teach", openTeach)
                .eq("admin_id", getSelf().getAdminId()));
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/password")
    public SBApi password(@Validated @RequestBody ModifyPasswordVo vo) {
        Admin admin =
                adminService.getById((authenticationFacade.getPrincipal(Admin.class)).getAdminId());
        if (!passwordEncoder.matches(vo.getOldPassword(), admin.getPassword().replace(salt, ""))) {
            throw new ShopException(ResultEnum.OLD_PASSWORD_ERROR);
        }
        adminService.update(
                (new UpdateWrapper<Admin>())
                        .set("password", salt + passwordEncoder.encode(vo.getNewPassword())));
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping
    public SBApi updateBaseInfo(@RequestBody Admin updateAdmin) {

        if (adminService.count(
                (new QueryWrapper<Admin>())
                        .ne("admin_id", updateAdmin.getAdminId())
                        .eq("user_name", updateAdmin.getUserName()))
                > 0) {
            throw new ShopException(ResultEnum.SAME_USER_NAME);
        }
        if (!StringUtils.isEmpty(updateAdmin.getPassword())) {
            updateAdmin.setPassword(salt + passwordEncoder.encode(updateAdmin.getPassword()));
        }
        adminService.updateById(updateAdmin);
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping
    public SBApi addAdmin(@RequestBody Admin admin) {
        if (adminService.count((new QueryWrapper<Admin>()).eq("user_name", admin.getUserName()))
                > 0) {
            throw new ShopException(ResultEnum.SAME_USER_NAME);
        }
        admin.setAddTime((int) (System.currentTimeMillis() / 1000));
        admin.setPassword(salt + passwordEncoder.encode(admin.getPassword()));
        log.info(salt + passwordEncoder.encode(admin.getPassword()));
        adminService.save(admin);
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/page")
    public Page<Admin> adminPage(
            @RequestParam(value = "user_name", required = false) String userName,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
        adminQueryWrapper.select("admin_id,user_name,email,add_time");
        if (!StringUtils.isEmpty(userName)) {
            adminQueryWrapper.like("user_name", userName);
        }
        return (Page<Admin>) adminService.page((new Page<>(page, size)), adminQueryWrapper);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("/{admin_id}")
    public SBApi deleteAdmin(@PathVariable(value = "admin_id") Integer adminId) {
        if (adminId != 1) {
            adminService.removeById(adminId);
        }
        return new SBApi();
    }
}
