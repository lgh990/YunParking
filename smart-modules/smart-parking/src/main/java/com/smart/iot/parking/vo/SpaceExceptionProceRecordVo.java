package com.smart.iot.parking.vo;

import com.smart.iot.parking.entity.ParkingSpace;
import com.smart.iot.parking.entity.SpaceExceptionProceRecord;
import com.smart.iot.parking.feign.DictFeign;
import com.yuncitys.smart.merge.annonation.MergeField;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


/**
 * 车位异常处理记录
 *
 * @author Mr.AG
 * @email
 *@version 2022-08-28 19:04:42
 */
public class SpaceExceptionProceRecordVo implements Serializable {
	private static final long serialVersionUID = 1L;

	//主键
	private String id;

	    //终端类型
    private String devType;

	    //异常原因
    private String errorCause;

	    //开始时间
    @Column(name = "begin_time")
    private String beginTime;

	    //结束时间
    private String endTime;

	    //终端设备
    private String onerankdeSn;

    private String errorType;

	    //字典 feedback_type 反馈类型
    private String feedbackType;

    private String spaceId;

	    //处理状态 字典proce_status
    private String proceStatus;

	    //有效标志(y:有效；n：无效)
    private String enabledFlag;

	    //创建时间
    private Date crtTime;

	    //创建人id
    private String crtUser;

	    //创建人
    private String crtName;

	    //创建主机
    private String crtHost;

	    //最后更新时间
    private Date updTime;

	    //最后更新人id
    private String updUser;

	    //最后更新人
    private String updName;

	    //最后更新主机
    private String updHost;

    private String attr1;

    private String attr2;

    private String attr3;

    private String attr4;

    private String attr5;

    private String attr6;

    private String attr7;

    private String attr8;

	    //租户id
    private String tenantId;

    private String crtUserId;

	public ParkingSpace getParkingSpace() {
		return parkingSpace;
	}

	public void setParkingSpace(ParkingSpace parkingSpace) {
		this.parkingSpace = parkingSpace;
	}

	private ParkingSpace parkingSpace;

	public SpaceExceptionProceRecordVo(SpaceExceptionProceRecord spaceExceptionProceRecord) {
		this.id = spaceExceptionProceRecord.getId();
		this.devType = spaceExceptionProceRecord.getDevType();
		this.errorCause = spaceExceptionProceRecord.getErrorCause();
		this.beginTime = spaceExceptionProceRecord.getBeginTime();
		this.endTime = spaceExceptionProceRecord.getEndTime();
		this.onerankdeSn = spaceExceptionProceRecord.getOnerankdeSn();
		this.errorType = spaceExceptionProceRecord.getErrorType();
		this.feedbackType = spaceExceptionProceRecord.getFeedbackType();
		this.spaceId = spaceExceptionProceRecord.getSpaceId();
		this.proceStatus = spaceExceptionProceRecord.getProceStatus();
		this.enabledFlag = spaceExceptionProceRecord.getEnabledFlag();
		this.crtTime = spaceExceptionProceRecord.getCrtTime();
		this.crtUser = spaceExceptionProceRecord.getCrtUser();
		this.crtName = spaceExceptionProceRecord.getCrtName();
		this.crtHost = spaceExceptionProceRecord.getCrtHost();
		this.updTime = spaceExceptionProceRecord.getUpdTime();
		this.updUser = spaceExceptionProceRecord.getUpdUser();
		this.updName = spaceExceptionProceRecord.getUpdName();
		this.updHost = spaceExceptionProceRecord.getUpdHost();
		this.attr1 = spaceExceptionProceRecord.getAttr1();
		this.attr2 = spaceExceptionProceRecord.getAttr2();
		this.attr3 = spaceExceptionProceRecord.getAttr3();
		this.attr4 = spaceExceptionProceRecord.getAttr4();
		this.attr5 = spaceExceptionProceRecord.getAttr5();
		this.attr6 = spaceExceptionProceRecord.getAttr6();
		this.attr7 = spaceExceptionProceRecord.getAttr7();
		this.attr8 = spaceExceptionProceRecord.getAttr8();
		this.tenantId = spaceExceptionProceRecord.getTenantId();
		this.crtUserId = spaceExceptionProceRecord.getCrtUserId();
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
	 * 设置：终端类型
	 */
	public void setDevType(String devType) {
		this.devType = devType;
	}
	/**
	 * 获取：终端类型
	 */
	public String getDevType() {
		return devType;
	}
	/**
	 * 设置：异常原因
	 */
	public void setErrorCause(String errorCause) {
		this.errorCause = errorCause;
	}
	/**
	 * 获取：异常原因
	 */
	public String getErrorCause() {
		return errorCause;
	}
	/**
	 * 设置：开始时间
	 */
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	/**
	 * 获取：开始时间
	 */
	public String getBeginTime() {
		return beginTime;
	}
	/**
	 * 设置：结束时间
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	/**
	 * 获取：结束时间
	 */
	public String getEndTime() {
		return endTime;
	}
	/**
	 * 设置：终端设备
	 */
	public void setOnerankdeSn(String onerankdeSn) {
		this.onerankdeSn = onerankdeSn;
	}
	/**
	 * 获取：终端设备
	 */
	public String getOnerankdeSn() {
		return onerankdeSn;
	}
	/**
	 * 设置：字典异常类型error_type
	 */
	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}
	/**
	 * 获取：字典异常类型error_type
	 */
	public String getErrorType() {
		return errorType;
	}
	/**
	 * 设置：字典 feedback_type 反馈类型
	 */
	public void setFeedbackType(String feedbackType) {
		this.feedbackType = feedbackType;
	}
	/**
	 * 获取：字典 feedback_type 反馈类型
	 */
	public String getFeedbackType() {
		return feedbackType;
	}
	/**
	 * 设置：车位号
	 */
	public void setSpaceId(String spaceId) {
		this.spaceId = spaceId;
	}
	/**
	 * 获取：车位号
	 */
	public String getSpaceId() {
		return spaceId;
	}
	/**
	 * 设置：处理状态 字典proce_status
	 */
	public void setProceStatus(String proceStatus) {
		this.proceStatus = proceStatus;
	}
	/**
	 * 获取：处理状态 字典proce_status
	 */
	public String getProceStatus() {
		return proceStatus;
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
}
