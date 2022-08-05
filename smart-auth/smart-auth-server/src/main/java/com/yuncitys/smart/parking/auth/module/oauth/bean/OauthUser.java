package com.yuncitys.smart.parking.auth.module.oauth.bean;

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
    private String departId;
    private String tenantId;

    public OauthUser(String id, String username, String password, String name, String departId, String tenantId, Collection<GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
        this.name = name;
        this.departId = departId;
        this.tenantId = tenantId;
    }

    public OauthUser(String id, String username, String name, String departId, String tenantId, String password, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled, Collection<GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
        this.name = name;
        this.departId = departId;
        this.tenantId = tenantId;
    }

    public String getDepartId() {
        return departId;
    }

    public void setDepartId(String departId) {
        this.departId = departId;
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
}
