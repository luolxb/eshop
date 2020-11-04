package com.soubao.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author dyr
 * @since 2020-03-13
 */
@Getter
@Setter
public class Admin implements UserDetails {

    /** 用户id */
    private Integer adminId;

    /** 用户名 */
    private String userName;

    private String email;

    private String password;

    private Integer addTime;

    private Integer lastLogin;

    private String lastIp;

    private String navList;

    private String langType;

    /** 支付密码 */
    private String paypwd;

    /** 角色id */
    private Integer roleId;

    /** suppliers_id */
    private Integer suppliersId;

    /** 0没有城市分站，1有 */
    private Integer siteId;

    private Boolean openTeach;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return authorities;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
