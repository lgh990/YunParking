package com.yuncitys.smart.parking.admin.vo;

import java.io.Serializable;

/**
 * @author smart
 * @create 2018/3/23.
 */
public class AuthUser implements Serializable{
    private static final long serialVersionUID = -6625521051532170772L;
    private String id;

    private String username;

    private String password;

    private String name;

    private String departId;

    private String tenantId;

    public AuthUser() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
