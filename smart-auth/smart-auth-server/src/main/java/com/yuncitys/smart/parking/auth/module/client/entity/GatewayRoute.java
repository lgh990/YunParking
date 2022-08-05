package com.yuncitys.smart.parking.auth.module.client.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
 *
 *
 * @author Mr.AG
 * @email 
 *@version 2022-02-25 12:04:28
 */
@Table(name = "gateway_route")
public class GatewayRoute implements Serializable {
	private static final long serialVersionUID = 1L;

	    //
    @Id
    private String id;

	    //映射路劲
    @Column(name = "path")
    private String path;

	    //映射服务
    @Column(name = "service_id")
    private String serviceId;

	    //映射外连接
    @Column(name = "url")
    private String url;

	    //是否重试
    @Column(name = "retryable")
    private Boolean retryable;

	    //是否启用
    @Column(name = "enabled")
    private Boolean enabled;

	    //是否忽略前缀
    @Column(name = "strip_prefix")
    private Boolean stripPrefix;

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


	/**
	 * 设置：
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public String getId() {
		return id;
	}
	/**
	 * 设置：映射路劲
	 */
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * 获取：映射路劲
	 */
	public String getPath() {
		return path;
	}
	/**
	 * 设置：映射服务
	 */
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	/**
	 * 获取：映射服务
	 */
	public String getServiceId() {
		return serviceId;
	}
	/**
	 * 设置：映射外连接
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * 获取：映射外连接
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * 设置：是否重试
	 */
	public void setRetryable(Boolean retryable) {
		this.retryable = retryable;
	}
	/**
	 * 获取：是否重试
	 */
	public Boolean getRetryable() {
		return retryable;
	}
	/**
	 * 设置：是否启用
	 */
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	/**
	 * 获取：是否启用
	 */
	public Boolean getEnabled() {
		return enabled;
	}
	/**
	 * 设置：是否忽略前缀
	 */
	public void setStripPrefix(Boolean stripPrefix) {
		this.stripPrefix = stripPrefix;
	}
	/**
	 * 获取：是否忽略前缀
	 */
	public Boolean getStripPrefix() {
		return stripPrefix;
	}
	/**
	 * 设置：创建人
	 */
	public void setCrtUserName(String crtUserName) {
		this.crtUserName = crtUserName;
	}
	/**
	 * 获取：创建人
	 */
	public String getCrtUserName() {
		return crtUserName;
	}
	/**
	 * 设置：创建人ID
	 */
	public void setCrtUserId(String crtUserId) {
		this.crtUserId = crtUserId;
	}
	/**
	 * 获取：创建人ID
	 */
	public String getCrtUserId() {
		return crtUserId;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCrtTime(Date crtTime) {
		this.crtTime = crtTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCrtTime() {
		return crtTime;
	}
	/**
	 * 设置：最后更新人
	 */
	public void setUpdUserName(String updUserName) {
		this.updUserName = updUserName;
	}
	/**
	 * 获取：最后更新人
	 */
	public String getUpdUserName() {
		return updUserName;
	}
	/**
	 * 设置：最后更新人ID
	 */
	public void setUpdUserId(String updUserId) {
		this.updUserId = updUserId;
	}
	/**
	 * 获取：最后更新人ID
	 */
	public String getUpdUserId() {
		return updUserId;
	}
	/**
	 * 设置：最后更新时间
	 */
	public void setUpdTime(Date updTime) {
		this.updTime = updTime;
	}
	/**
	 * 获取：最后更新时间
	 */
	public Date getUpdTime() {
		return updTime;
	}
}
