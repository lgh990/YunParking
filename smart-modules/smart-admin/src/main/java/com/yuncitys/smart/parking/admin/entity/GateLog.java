package com.yuncitys.smart.parking.admin.entity;

import com.yuncitys.smart.parking.common.audit.AceAudit;
import com.yuncitys.smart.parking.common.audit.CrtTime;
import com.yuncitys.smart.parking.common.audit.CrtUserId;
import com.yuncitys.smart.parking.common.audit.CrtUserName;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "gate_log")
@AceAudit
public class GateLog {
    @Id
    private Integer id;

    private String menu;

    private String opt;

    private String uri;

    @CrtTime
    @Column(name = "crt_time")
    private Date crtTime;
    @CrtUserId
    @Column(name = "crt_user")
    private String crtUser;
    @CrtUserName
    @Column(name = "crt_name")
    private String crtName;

    @Column(name = "crt_host")
    private String crtHost;

    @Column(name = "tenant_id")
    private String tenantId;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return menu
     */
    public String getMenu() {
        return menu;
    }

    /**
     * @param menu
     */
    public void setMenu(String menu) {
        this.menu = menu;
    }


    /**
     * @return uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * @param uri
     */
    public void setUri(String uri) {
        this.uri = uri;
    }
    /**
     * @return crt_time
     */
    public Date getCrtTime() {
        return crtTime;
    }

    /**
     * @param crtTime
     */
    public void setCrtTime(Date crtTime) {
        this.crtTime = crtTime;
    }

    /**
     * @return crt_user
     */
    public String getCrtUser() {
        return crtUser;
    }

    /**
     * @param crtUser
     */
    public void setCrtUser(String crtUser) {
        this.crtUser = crtUser;
    }

    /**
     * @return crt_name
     */
    public String getCrtName() {
        return crtName;
    }

    /**
     * @param crtName
     */
    public void setCrtName(String crtName) {
        this.crtName = crtName;
    }

    /**
     * @return crt_host
     */
    public String getCrtHost() {
        return crtHost;
    }

    /**
     * @param crtHost
     */
    public void setCrtHost(String crtHost) {
        this.crtHost = crtHost;
    }

    public String getOpt() {
        return opt;
    }

    public void setOpt(String opt) {
        this.opt = opt;
    }
}
