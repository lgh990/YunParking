package com.yuncitys.smart.parking.dict.entity;

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
 *@version 2022-01-30 19:45:55
 */
@Table(name = "dict_value")
public class DictValue implements Serializable {
	private static final long serialVersionUID = 1L;

	//主键
    @Id
    private String id;
	private Integer orderNum;

	@Column(name = "tenant_id")
	private String tenantId;

	//编码
    @Column(name = "code")
    private String code;

	@Column(name="value")
	private String value;

	    //默认显示
    @Column(name = "label_default")
    private String labelDefault;

	    //英文显示
    @Column(name = "label_en_US")
    private String labelEnUs;

	    //中文显示
    @Column(name = "label_zh_CH")
    private String labelZhCh;

	    //值
    @Column(name = "label_attr1")
    private String labelAttr1;

	    //值
    @Column(name = "label_attr2")
    private String labelAttr2;

	    //值
    @Column(name = "label_attr3")
    private String labelAttr3;

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

	@Column(name="type_id")
	private String typeId;

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
	 * 设置：默认显示
	 */
	public void setLabelDefault(String labelDefault) {
		this.labelDefault = labelDefault;
	}
	/**
	 * 获取：默认显示
	 */
	public String getLabelDefault() {
		return labelDefault;
	}
	/**
	 * 设置：英文显示
	 */
	public void setLabelEnUs(String labelEnUs) {
		this.labelEnUs = labelEnUs;
	}
	/**
	 * 获取：英文显示
	 */
	public String getLabelEnUs() {
		return labelEnUs;
	}
	/**
	 * 设置：中文显示
	 */
	public void setLabelZhCh(String labelZhCh) {
		this.labelZhCh = labelZhCh;
	}
	/**
	 * 获取：中文显示
	 */
	public String getLabelZhCh() {
		return labelZhCh;
	}
	/**
	 * 设置：值
	 */
	public void setLabelAttr1(String labelAttr1) {
		this.labelAttr1 = labelAttr1;
	}
	/**
	 * 获取：值
	 */
	public String getLabelAttr1() {
		return labelAttr1;
	}
	/**
	 * 设置：值
	 */
	public void setLabelAttr2(String labelAttr2) {
		this.labelAttr2 = labelAttr2;
	}
	/**
	 * 获取：值
	 */
	public String getLabelAttr2() {
		return labelAttr2;
	}
	/**
	 * 设置：值
	 */
	public void setLabelAttr3(String labelAttr3) {
		this.labelAttr3 = labelAttr3;
	}
	/**
	 * 获取：值
	 */
	public String getLabelAttr3() {
		return labelAttr3;
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
	/**
	 * 设置：
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	/**
	 * 获取：
	 */
	public String getParentId() {
		return parentId;
	}
	/**
	 * 设置：
	 */
	public void setAttr1(String attr1) {
		this.attr1 = attr1;
	}
	/**
	 * 获取：
	 */
	public String getAttr1() {
		return attr1;
	}
	/**
	 * 设置：
	 */
	public void setAttr2(String attr2) {
		this.attr2 = attr2;
	}
	/**
	 * 获取：
	 */
	public String getAttr2() {
		return attr2;
	}
	/**
	 * 设置：
	 */
	public void setAttr3(String attr3) {
		this.attr3 = attr3;
	}
	/**
	 * 获取：
	 */
	public String getAttr3() {
		return attr3;
	}
	/**
	 * 设置：
	 */
	public void setAttr4(String attr4) {
		this.attr4 = attr4;
	}
	/**
	 * 获取：
	 */
	public String getAttr4() {
		return attr4;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
}
