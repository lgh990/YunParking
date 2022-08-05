package com.smart.iot.parking.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import com.smart.iot.parking.biz.ParkingBiz;
import com.smart.iot.parking.biz.ParkingSpaceBiz;
import com.yuncitys.smart.merge.annonation.MergeField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 车位注销申请记录
 *
 * @author heyaohuan
 * @email
 *@version 2022-09-26 11:04:43
 */
@Table(name = "parking_space_cancelled")
@ApiModel("")
public class ParkingSpaceCancelled implements Serializable {
	private static final long serialVersionUID = 1L;

	    //
    @Id
    private String id;

	    //停车场id
    @Column(name = "parking_id")
    @ApiModelProperty("停车场id")
    private String parkingId;

	    //车位id
    @Column(name = "space_id")
    @ApiModelProperty("车位id")
    private String spaceId;

	    //业主id
    @Column(name = "user_id")
    @ApiModelProperty("业主id")
    private String userId;

	    //注销申请时间
    @Column(name = "cancel_time")
    @ApiModelProperty("注销申请时间")
    private Date cancelTime;

	    //
    @Column(name = "is_deleted")
    @ApiModelProperty("")
    private Integer isDeleted;

	    //注销申请记录状态（0：新建，1：注销成功，2：拒绝）
    @Column(name = "status")
    @ApiModelProperty("注销申请记录状态（0：新建，1：注销成功，2：拒绝）")
    private  int status;

	    //有效标志(y:有效；n：无效)
    @Column(name = "enabled_flag")
    @ApiModelProperty("有效标志(y:有效；n：无效)")
    private String enabledFlag;

	    //创建时间
    @Column(name = "crt_time")
    @ApiModelProperty("创建时间")
    private Date crtTime;

	    //创建人id
    @Column(name = "crt_user")
    @ApiModelProperty("创建人id")
    private String crtUser;

	    //创建人
    @Column(name = "crt_name")
    @ApiModelProperty("创建人")
    private String crtName;

	    //创建主机
    @Column(name = "crt_host")
    @ApiModelProperty("创建主机")
    private String crtHost;

	    //最后更新时间
    @Column(name = "upd_time")
    @ApiModelProperty("最后更新时间")
    private Date updTime;

	    //最后更新人id
    @Column(name = "upd_user")
    @ApiModelProperty("最后更新人id")
    private String updUser;

	    //最后更新人
    @Column(name = "upd_name")
    @ApiModelProperty("最后更新人")
    private String updName;

	    //最后更新主机
    @Column(name = "upd_host")
    @ApiModelProperty("最后更新主机")
    private String updHost;

	    //
    @Column(name = "attr1")
    @ApiModelProperty("")
    private String attr1;

	    //
    @Column(name = "attr2")
    @ApiModelProperty("")
    private String attr2;

	    //
    @Column(name = "attr3")
    @ApiModelProperty("")
    private String attr3;

	    //
    @Column(name = "attr4")
    @ApiModelProperty("")
    private String attr4;

	    //
    @Column(name = "attr5")
    @ApiModelProperty("")
    private String attr5;

	    //
    @Column(name = "attr6")
    @ApiModelProperty("")
    private String attr6;

	    //
    @Column(name = "attr7")
    @ApiModelProperty("")
    private String attr7;

	    //
    @Column(name = "attr8")
    @ApiModelProperty("")
    private String attr8;

	    //租户id
    @Column(name = "tenant_id")
    @ApiModelProperty("租户id")
    private String tenantId;


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
	 * 设置：停车场id
	 */
	public void setParkingId(String parkingId) {
		this.parkingId = parkingId;
	}
	/**
	 * 获取：停车场id
	 */
	public String getParkingId() {
		return parkingId;
	}
	/**
	 * 设置：车位id
	 */
	public void setSpaceId(String spaceId) {
		this.spaceId = spaceId;
	}
	/**
	 * 获取：车位id
	 */
	public String getSpaceId() {
		return spaceId;
	}
	/**
	 * 设置：业主id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * 获取：业主id
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * 设置：注销申请时间
	 */
	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
	}
	/**
	 * 获取：注销申请时间
	 */
	public Date getCancelTime() {
		return cancelTime;
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
	 * 设置：注销申请记录状态（0：新建，1：注销成功，2：拒绝）
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * 获取：注销申请记录状态（0：新建，1：注销成功，2：拒绝）
	 */
	public int getStatus() {
		return status;
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
}
