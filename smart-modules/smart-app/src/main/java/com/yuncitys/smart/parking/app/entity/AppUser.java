package com.yuncitys.smart.parking.app.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 *
 *
 * @author heyaohuan
 * @email
 *@version 2022-09-10 17:05:22
 */
@Table(name = "app_user")
@ApiModel("APP用户")
public class AppUser implements Serializable {
	private static final long serialVersionUID = 1L;

	    //
    @Id
	@ApiModelProperty("APP用户id")
    private String id;

	    //手机号
    @Column(name = "mobile")
	@ApiModelProperty("手机号")
    private String mobile;

	    //姓名
    @Column(name = "name")
	@ApiModelProperty("姓名")
    @Size(max=255)
    private String name;

	    //密码
    @Column(name = "password")
	@ApiModelProperty("密码")
    private String password;

	    //密码
    @Column(name = "money")
	@ApiModelProperty("金额")
    private BigDecimal money;

	    //性别
    @Column(name = "sex")
	@ApiModelProperty("性别")
    private String sex;

	    //年龄
    @Column(name = "age")
	@ApiModelProperty("年龄")
    private String age;

	    //职业
    @Column(name = "profession")
    @ApiModelProperty("职业")
    private String profession;

	    //公司
    @Column(name = "company")
    private String company;
	//公司
	@Column(name = "vip_order_num")
	private String vipOrderNum;

	//职位
    @Column(name = "position")
    private String position;

	    //有效标志(y:有效；n：无效)
    @Column(name = "enabled_flag")
    private String enabledFlag;

	    //开启免密支付标志 字典enabled_flag（y已开启，n未开启）
    @Column(name = "nosecret_pay_flag")
    private String nosecretPayFlag;

	    //登陆类型(pwd:密码登陆，sms：短信登陆)
    @Column(name = "auth_type")
    private String authType;

	    //字典user_type	用户类型(common,vip)
    @Column(name = "user_type")
    private String userType;

	    //
    @Column(name = "is_deleted")
    private Integer isDeleted;

	    //用户是否设置密码（0：未设置，1：已设置）
    @Column(name = "is_setting_pwd")
    private Integer isSettingPwd;

	    //创建时间
    @Column(name = "crt_time")
    private Date crtTime;

	    //创建人id
    @Column(name = "crt_user")
    private String crtUser;

	    //创建人
    @Column(name = "crt_name")
    private String crtName;

	    //创建主机
    @Column(name = "crt_host")
    private String crtHost;

	    //最后更新时间
    @Column(name = "upd_time")
    private Date updTime;

	    //最后更新人id
    @Column(name = "upd_user")
    private String updUser;

	    //最后更新人
    @Column(name = "upd_name")
    private String updName;

	    //最后更新主机
    @Column(name = "upd_host")
    private String updHost;

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

	    //
    @Column(name = "attr5")
    private String attr5;

	    //
    @Column(name = "attr6")
    private String attr6;

	    //
    @Column(name = "attr7")
    private String attr7;

	    //
    @Column(name = "attr8")
    private String attr8;

	    //租户id
    @Column(name = "tenant_id")
    private String tenantId;

	    //
    @Column(name = "crt_user_id")
    private String crtUserId;


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
	 * 设置：手机号
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	/**
	 * 获取：手机号
	 */
	public String getMobile() {
		return mobile;
	}
	/**
	 * 设置：姓名
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：姓名
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * 获取：密码
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * 设置：密码
	 */
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	/**
	 * 获取：密码
	 */
	public BigDecimal getMoney() {
		return money;
	}
	/**
	 * 设置：性别
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}
	/**
	 * 获取：性别
	 */
	public String getSex() {
		return sex;
	}
	/**
	 * 设置：年龄
	 */
	public void setAge(String age) {
		this.age = age;
	}
	/**
	 * 获取：年龄
	 */
	public String getAge() {
		return age;
	}
	/**
	 * 设置：职业
	 */
	public void setProfession(String profession) {
		this.profession = profession;
	}
	/**
	 * 获取：职业
	 */
	public String getProfession() {
		return profession;
	}
	/**
	 * 设置：公司
	 */
	public void setCompany(String company) {
		this.company = company;
	}
	/**
	 * 获取：公司
	 */
	public String getCompany() {
		return company;
	}
	/**
	 * 设置：职位
	 */
	public void setPosition(String position) {
		this.position = position;
	}
	/**
	 * 获取：职位
	 */
	public String getPosition() {
		return position;
	}
	/**
	 * 设置：有效标志(y:有效；n：无效)
	 */
	public void setEnabledFlag(String enabledFlag) {
		this.enabledFlag = enabledFlag;
	}
	/**
	 * 获取：有效标志(y:有效；n：无效)
	 */
	public String getEnabledFlag() {
		return enabledFlag;
	}
	/**
	 * 设置：开启免密支付标志 字典enabled_flag（y已开启，n未开启）
	 */
	public void setNosecretPayFlag(String nosecretPayFlag) {
		this.nosecretPayFlag = nosecretPayFlag;
	}
	/**
	 * 获取：开启免密支付标志 字典enabled_flag（y已开启，n未开启）
	 */
	public String getNosecretPayFlag() {
		return nosecretPayFlag;
	}
	/**
	 * 设置：登陆类型(pwd:密码登陆，sms：短信登陆)
	 */
	public void setAuthType(String authType) {
		this.authType = authType;
	}
	/**
	 * 获取：登陆类型(pwd:密码登陆，sms：短信登陆)
	 */
	public String getAuthType() {
		return authType;
	}
	/**
	 * 设置：字典user_type	用户类型(common,vip)
	 */
	public void setUserType(String userType) {
		this.userType = userType;
	}
	/**
	 * 获取：字典user_type	用户类型(common,vip)
	 */
	public String getUserType() {
		return userType;
	}
	/**
	 * 设置：
	 */
	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}
	/**
	 * 获取：
	 */
	public Integer getIsDeleted() {
		return isDeleted;
	}
	/**
	 * 设置：用户是否设置密码（0：未设置，1：已设置）
	 */
	public void setIsSettingPwd(Integer isSettingPwd) {
		this.isSettingPwd = isSettingPwd;
	}
	/**
	 * 获取：用户是否设置密码（0：未设置，1：已设置）
	 */
	public Integer getIsSettingPwd() {
		return isSettingPwd;
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
	 * 设置：创建人id
	 */
	public void setCrtUser(String crtUser) {
		this.crtUser = crtUser;
	}
	/**
	 * 获取：创建人id
	 */
	public String getCrtUser() {
		return crtUser;
	}
	/**
	 * 设置：创建人
	 */
	public void setCrtName(String crtName) {
		this.crtName = crtName;
	}
	/**
	 * 获取：创建人
	 */
	public String getCrtName() {
		return crtName;
	}
	/**
	 * 设置：创建主机
	 */
	public void setCrtHost(String crtHost) {
		this.crtHost = crtHost;
	}
	/**
	 * 获取：创建主机
	 */
	public String getCrtHost() {
		return crtHost;
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
	 * 设置：最后更新人id
	 */
	public void setUpdUser(String updUser) {
		this.updUser = updUser;
	}
	/**
	 * 获取：最后更新人id
	 */
	public String getUpdUser() {
		return updUser;
	}
	/**
	 * 设置：最后更新人
	 */
	public void setUpdName(String updName) {
		this.updName = updName;
	}
	/**
	 * 获取：最后更新人
	 */
	public String getUpdName() {
		return updName;
	}
	/**
	 * 设置：最后更新主机
	 */
	public void setUpdHost(String updHost) {
		this.updHost = updHost;
	}
	/**
	 * 获取：最后更新主机
	 */
	public String getUpdHost() {
		return updHost;
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
	/**
	 * 设置：
	 */
	public void setAttr5(String attr5) {
		this.attr5 = attr5;
	}
	/**
	 * 获取：
	 */
	public String getAttr5() {
		return attr5;
	}
	/**
	 * 设置：
	 */
	public void setAttr6(String attr6) {
		this.attr6 = attr6;
	}
	/**
	 * 获取：
	 */
	public String getAttr6() {
		return attr6;
	}
	/**
	 * 设置：
	 */
	public void setAttr7(String attr7) {
		this.attr7 = attr7;
	}
	/**
	 * 获取：
	 */
	public String getAttr7() {
		return attr7;
	}
	/**
	 * 设置：
	 */
	public void setAttr8(String attr8) {
		this.attr8 = attr8;
	}
	/**
	 * 获取：
	 */
	public String getAttr8() {
		return attr8;
	}
	/**
	 * 设置：租户id
	 */
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	/**
	 * 获取：租户id
	 */
	public String getTenantId() {
		return tenantId;
	}
	/**
	 * 设置：
	 */
	public void setCrtUserId(String crtUserId) {
		this.crtUserId = crtUserId;
	}
	/**
	 * 获取：
	 */
	public String getCrtUserId() {
		return crtUserId;
	}

	public String getVipOrderNum() {
		return vipOrderNum;
	}

	public void setVipOrderNum(String vipOrderNum) {
		this.vipOrderNum = vipOrderNum;
	}
}
