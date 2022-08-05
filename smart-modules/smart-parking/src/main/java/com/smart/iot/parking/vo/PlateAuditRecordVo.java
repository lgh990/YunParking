package com.smart.iot.parking.vo;

import com.smart.iot.parking.entity.AppUser;
import com.smart.iot.parking.entity.PlateAuditRecord;
import com.smart.iot.parking.feign.DictFeign;
import com.yuncitys.smart.merge.annonation.MergeField;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


/**
 * 车牌审核记录表
 *
 * @author Mr.AG
 * @email
 *@version 2022-08-08 14:45:26
 */
public class PlateAuditRecordVo implements Serializable {
	private static final long serialVersionUID = 1L;


	private String recordId;

    private String userId;

	    //车牌号码
    private String carNumber;

	    //字典car_type车型（大车小车）
    private String carType;

	    //车牌图片
    private String carNumberPhoto;

    private String result;

	    //备注信息
    private String remarks;

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

	    //
    private String attr1;

	    //
    private String attr2;

	    //
    private String attr3;

	    //
    private String attr4;

	    //
    private String attr5;

	    //
    private String attr6;

	    //
    private String attr7;

	    //
    private String attr8;

	    //租户id
    private String tenantId;

	public AppUser getAppUser() {
		return appUser;
	}

	public void setAppUser(AppUser appUser) {
		this.appUser = appUser;
	}

	private AppUser appUser;

	public PlateAuditRecordVo(PlateAuditRecord plateAuditRecord) {
		this.recordId = plateAuditRecord.getRecordId();
		this.userId = plateAuditRecord.getUserId();
		this.carNumber = plateAuditRecord.getCarNumber();
		this.carType = plateAuditRecord.getCarType();
		this.carNumberPhoto = plateAuditRecord.getCarNumberPhoto();
		this.result = plateAuditRecord.getResult();
		this.remarks = plateAuditRecord.getRemarks();
		this.enabledFlag = plateAuditRecord.getEnabledFlag();
		this.crtTime = plateAuditRecord.getCrtTime();
		this.crtUser = plateAuditRecord.getCrtUser();
		this.crtName = plateAuditRecord.getCrtName();
		this.crtHost = plateAuditRecord.getCrtHost();
		this.updTime = plateAuditRecord.getUpdTime();
		this.updUser = plateAuditRecord.getUpdUser();
		this.updName = plateAuditRecord.getUpdName();
		this.updHost = plateAuditRecord.getUpdHost();
		this.attr1 = plateAuditRecord.getAttr1();
		this.attr2 = plateAuditRecord.getAttr2();
		this.attr3 = plateAuditRecord.getAttr3();
		this.attr4 = plateAuditRecord.getAttr4();
		this.attr5 = plateAuditRecord.getAttr5();
		this.attr6 = plateAuditRecord.getAttr6();
		this.attr7 = plateAuditRecord.getAttr7();
		this.attr8 = plateAuditRecord.getAttr8();
		this.tenantId = plateAuditRecord.getTenantId();
	}


	/**
	 * 设置：
	 */
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	/**
	 * 获取：
	 */
	public String getRecordId() {
		return recordId;
	}
	/**
	 * 设置：用户id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * 获取：用户id
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * 设置：车牌号码
	 */
	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}
	/**
	 * 获取：车牌号码
	 */
	public String getCarNumber() {
		return carNumber;
	}
	/**
	 * 设置：字典car_type车型（大车小车）
	 */
	public void setCarType(String carType) {
		this.carType = carType;
	}
	/**
	 * 获取：字典car_type车型（大车小车）
	 */
	public String getCarType() {
		return carType;
	}
	/**
	 * 设置：车牌图片
	 */
	public void setCarNumberPhoto(String carNumberPhoto) {
		this.carNumberPhoto = carNumberPhoto;
	}
	/**
	 * 获取：车牌图片
	 */
	public String getCarNumberPhoto() {
		return carNumberPhoto;
	}
	/**
	 * 设置：字典audit_state
	 */
	public void setResult(String result) {
		this.result = result;
	}
	/**
	 * 获取：字典audit_state
	 */
	public String getResult() {
		return result;
	}
	/**
	 * 设置：备注信息
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	/**
	 * 获取：备注信息
	 */
	public String getRemarks() {
		return remarks;
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
