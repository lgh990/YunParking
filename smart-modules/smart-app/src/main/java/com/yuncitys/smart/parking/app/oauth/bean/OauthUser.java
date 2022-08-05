package com.yuncitys.smart.parking.app.oauth.bean;

import org.springframework.parking.core.GrantedAuthority;
import org.springframework.parking.core.userdetails.User;
import org.springframework.parking.core.userdetails.UserDetails;

import java.util.Collection;

/**
 *
 * @author smart
 * @create 2018/3/23.
 */
public class OauthUser extends User implements UserDetails {
    private static final long serialVersionUID = 3123152600328379950L;
    public String id;
    public String name;
    private String tenantId;
    private Integer isSettingPwd;
    private String userType;
    public OauthUser(String id, String username, String password, String name, String tenantId, Integer isSettingPwd, String userType, Collection<GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
        this.name = name;
        this.tenantId = tenantId;
        this.isSettingPwd = isSettingPwd;
        this.userType = userType;
    }

    public OauthUser(String id, String username, String name, String tenantId, String password, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled, Integer isSettingPwd, String userType, Collection<GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
        this.name = name;
        this.tenantId = tenantId;
        this.isSettingPwd = isSettingPwd;
        this.userType = userType;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIsSettingPwd() {
        return isSettingPwd;
    }

    public void setIsSettingPwd(Integer isSettingPwd) {
        this.isSettingPwd = isSettingPwd;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
