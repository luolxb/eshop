package com.soubao.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class Seller implements UserDetails {

    /**
     * 卖家编号
     */
    private Integer sellerId;

    /**
     * 卖家用户名
     */
    private String sellerName;

    /**
     * 用户编号
     */
    private Integer userId;

    /**
     * 卖家组编号
     */
    private Integer groupId;

    /**
     * 店铺编号
     */
    private Integer storeId;

    /**
     * 是否管理员(0-不是 1-是)
     */
    private Integer isAdmin;

    /**
     * 卖家快捷操作
     */
    private String sellerQuicklink;

    /**
     * 最后登录时间
     */
    private Integer lastLoginTime;

    /**
     * 激活状态 0启用1关闭
     */
//    private Integer enable; 加这个oauth获取token错误，我也不知道这是什么鸡巴bug-->

    private Integer addTime;

    /**
     * 用于app 授权类似于session_id
     */
    private String token;

    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }

    @Override
    public String getUsername() {
        return sellerName;
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
