package com.smart.iot.parking.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
 * 注销审核记录表
 *
 * @author YUNCITYS
 * @email YUNCITYS@smart.iot.com
 * @version 2022-04-23 14:36:43
 */
@Table(name = "cancellation_audit_records")
public class CancellationAuditRecords implements Serializable {
	private static final long serialVersionUID = 1L;

	    //
    @Id
    private String id;

	    //业主电话
    @Column(name = "telephone")
    private String telephone;

	    //车位号码
    @Column(name = "space_num")
    private String spaceNum;

	    //停车场名称
    @Column(name = "parking_name")
    private String parkingName;

	    //audit_state审核标志（审核中 通过 拒绝）
    @Column(name = "status")
    private String status;

	    //备注
    @Column(name = "remake")
    private String remake;

	    //注销时间
    @Column(name = "cancellation_time")
    private Date cancellationTime;

	    //用户共享车位
    @Column(name = "user_space_id")
    private String userSpaceId;

	    //有效标志(y:有效；n：无效)
    @Column(name = "enabled_flag")
    private String enabledFlag;

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
	 * 设置：业主电话
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	/**
	 * 获取：业主电话
	 */
	public String getTelephone() {
		return telephone;
	}
	/**
	 * 设置：车位号码
	 */
	public void setSpaceNum(String spaceNum) {
		this.spaceNum = spaceNum;
	}
	/**
	 * 获取：车位号码
	 */
	public String getSpaceNum() {
		return spaceNum;
	}
	/**
	 * 设置：停车场名称
	 */
	public void setParkingName(String parkingName) {
		this.parkingName = parkingName;
	}
	/**
	 * 获取：停车场名称
	 */
	public String getParkingName() {
		return parkingName;
	}
	/**
	 * 设置：audit_state审核标志（审核中 通过 拒绝）
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * 获取：audit_state审核标志（审核中 通过 拒绝）
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * 设置：备注
	 */
	public void setRemake(String remake) {
		this.remake = remake;
	}
	/**
	 * 获取：备注
	 */
	public String getRemake() {
		return remake;
	}
	/**
	 * 设置：注销时间
	 */
	public void setCancellationTime(Date cancellationTime) {
		this.cancellationTime = cancellationTime;
	}
	/**
	 * 获取：注销时间
	 */
	public Date getCancellationTime() {
		return cancellationTime;
	}
	/**
	 * 设置：用户共享车位
	 */
	public void setUserSpaceId(String userSpaceId) {
		this.userSpaceId = userSpaceId;
	}
	/**
	 * 获取：用户共享车位
	 */
	public String getUserSpaceId() {
		return userSpaceId;
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
}
