package com.yuncitys.smart.parking.admin.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


/**
 *
 *
 * @author Mr.AG
 * @email
 *@version 2022-02-04 19:06:43
 */
@Table(name = "base_depart")
public class Depart implements Serializable {
	private static final long serialVersionUID = 1L;

	    //主键
    @Id
    private String id;

	    //组织名称
    @Column(name = "name")
    private String name;

	    //编码
    @Column(name = "code")
    private String code;

	    //路劲
    @Column(name = "path")
    private String path;

	    //部门类型
    @Column(name = "type")
    private String type;


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

	//
	@Column(name = "parent_id")
	private String parentId;

	//
	@Column(name = "attr1")
	private String attr1;

	//
	@Column(name = "attr2")
	private String attr2;

	//
	@Column(name = "attr3")
	private String attr3;

	//
	@Column(name = "attr4")
	private String attr4;

	@Column(name = "tenant_id")
	private String tenantId;

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	/**
	 * 设置：主键
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 获取：主键
	 */
	public String getId() {
		return id;
	}
	/**
	 * 设置：组织名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：组织名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：上级节点
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	/**
	 * 获取：上级节点
	 */
	public String getParentId() {
		return parentId;
	}
	/**
	 * 设置：编码
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 获取：编码
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 设置：路劲
	 */
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * 获取：路劲
	 */
	public String getPath() {
		return path;
	}
	/**
	 * 设置：部门类型
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 获取：部门类型
	 */
	public String getType() {
		return type;
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

	public String getAttr1() {
		return attr1;
	}

	public void setAttr1(String attr1) {
		this.attr1 = attr1;
	}

	public String getAttr2() {
		return attr2;
	}

	public void setAttr2(String attr2) {
		this.attr2 = attr2;
	}

	public String getAttr3() {
		return attr3;
	}

	public void setAttr3(String attr3) {
		this.attr3 = attr3;
	}

	public String getAttr4() {
		return attr4;
	}

	public void setAttr4(String attr4) {
		this.attr4 = attr4;
	}

}
