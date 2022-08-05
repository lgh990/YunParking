package com.yuncitys.smart.parking.auth.module.oauth.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
 * @author Mr.AG
 *@version 2022-03-28 13:02:20
 * @email
 */
@Table(name = "oauth_client_details")
public class OauthClientDetails implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    @Id
    private String clientId;

    //
    @Column(name = "resource_ids")
    private String resourceIds;

    //
    @Column(name = "client_secret")
    private String clientSecret;

    //
    @Column(name = "scope")
    private String scope;

    //
    @Column(name = "authorized_grant_types")
    private String authorizedGrantTypes;

    //
    @Column(name = "web_server_redirect_uri")
    private String webServerRedirectUri;

    //
    @Column(name = "authorities")
    private String authorities;

    //
    @Column(name = "access_token_validity")
    private Integer accessTokenValidity;

    //
    @Column(name = "refresh_token_validity")
    private Integer refreshTokenValidity;

    //
    @Column(name = "additional_information")
    private String additionalInformation;

    //
    @Column(name = "autoapprove")
    private String autoapprove;

    //创建人
    @Column(name = "crt_user_name")
    private String crtUserName;

    //创建人ID
    @Column(name = "crt_user_id")
    private String crtUserId;

    //创建时间
    @Column(name = "crt_time")
    private Date crtTime;

    //最后更新人
    @Column(name = "upd_user_name")
    private String updUserName;

    //最后更新人ID
    @Column(name = "upd_user_id")
    private String updUserId;

    //最后更新时间
    @Column(name = "upd_time")
    private Date updTime;
    private String description;

    @Column(name = "is_deleted")
    private String isDeleted;

    @Column(name = "is_disabled")
    private String isDisabled;

    /**
     * 设置：
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
     * 获取：
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * 设置：
     */
    public void setResourceIds(String resourceIds) {
        this.resourceIds = resourceIds;
    }

    /**
     * 获取：
     */
    public String getResourceIds() {
        return resourceIds;
    }

    /**
     * 设置：
     */
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    /**
     * 获取：
     */
    public String getClientSecret() {
        return clientSecret;
    }

    /**
     * 设置：
     */
    public void setScope(String scope) {
        this.scope = scope;
    }

    /**
     * 获取：
     */
    public String getScope() {
        return scope;
    }

    /**
     * 设置：
     */
    public void setAuthorizedGrantTypes(String authorizedGrantTypes) {
        this.authorizedGrantTypes = authorizedGrantTypes;
    }

    /**
     * 获取：
     */
    public String getAuthorizedGrantTypes() {
        return authorizedGrantTypes;
    }

    /**
     * 设置：
     */
    public void setWebServerRedirectUri(String webServerRedirectUri) {
        this.webServerRedirectUri = webServerRedirectUri;
    }

    /**
     * 获取：
     */
    public String getWebServerRedirectUri() {
        return webServerRedirectUri;
    }

    /**
     * 设置：
     */
    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }

    /**
     * 获取：
     */
    public String getAuthorities() {
        return authorities;
    }

    /**
     * 设置：
     */
    public void setAccessTokenValidity(Integer accessTokenValidity) {
        this.accessTokenValidity = accessTokenValidity;
    }

    /**
     * 获取：
     */
    public Integer getAccessTokenValidity() {
        return accessTokenValidity;
    }

    /**
     * 设置：
     */
    public void setRefreshTokenValidity(Integer refreshTokenValidity) {
        this.refreshTokenValidity = refreshTokenValidity;
    }

    /**
     * 获取：
     */
    public Integer getRefreshTokenValidity() {
        return refreshTokenValidity;
    }

    /**
     * 设置：
     */
    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    /**
     * 获取：
     */
    public String getAdditionalInformation() {
        return additionalInformation;
    }

    /**
     * 设置：
     */
    public void setAutoapprove(String autoapprove) {
        this.autoapprove = autoapprove;
    }

    /**
     * 获取：
     */
    public String getAutoapprove() {
        return autoapprove;
    }

    public String getCrtUserName() {
        return crtUserName;
    }

    public void setCrtUserName(String crtUserName) {
        this.crtUserName = crtUserName;
    }

    public String getCrtUserId() {
        return crtUserId;
    }

    public void setCrtUserId(String crtUserId) {
        this.crtUserId = crtUserId;
    }

    public Date getCrtTime() {
        return crtTime;
    }

    public void setCrtTime(Date crtTime) {
        this.crtTime = crtTime;
    }

    public String getUpdUserName() {
        return updUserName;
    }

    public void setUpdUserName(String updUserName) {
        this.updUserName = updUserName;
    }

    public String getUpdUserId() {
        return updUserId;
    }

    public void setUpdUserId(String updUserId) {
        this.updUserId = updUserId;
    }

    public Date getUpdTime() {
        return updTime;
    }

    public void setUpdTime(Date updTime) {
        this.updTime = updTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(String isDisabled) {
        this.isDisabled = isDisabled;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }
}
