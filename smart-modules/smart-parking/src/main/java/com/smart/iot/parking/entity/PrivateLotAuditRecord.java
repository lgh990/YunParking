package com.smart.iot.parking.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


/**
 * 业主审核记录表
 *
 * @author heyaohuan
 * @email
 *@version 2022-09-13 10:14:20
 */
@Table(name = "private_lot_audit_record")
public class PrivateLotAuditRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	    //id
    @Id
    private String pvRecordId;

	    //用户id
    @Column(name = "user_id")
    private String userId;

	    //法人代表名字
    @Column(name = "corporate_name")
    private String corporateName;

	    //电话号码
    @Column(name = "telephone")
    private String telephone;

	    //停车场名称
    @Column(name = "parking_name")
    private String parkingName;

	    //开始时间
    @Column(name = "begin_date")
    private String beginDate;

	    //结束时间
    @Column(name = "end_date")
    private String endDate;

	    //停车场地址
    @Column(name = "parking_address")
    private String parkingAddress;

	    //申请单位
    @Column(name = "applicant_unit")
    private String applicantUnit;

	    //字典parking_type 停车场类型(室内  路侧)
    @Column(name = "parking_type")
    private String parkingType;

	    //字典aera_type区层类型（地面 地下 地面地下都有）
    @Column(name = "area_type")
    private String areaType;

	    //出租时段（周一周二周三）
    @Column(name = "rental_period")
    private String rentalPeriod;

	    //占地面积（单位：平方米）
    @Column(name = "floor_space")
    private String floorSpace;

	    //车位数量
    @Column(name = "space_nums")
    private String spaceNums;

	    //图片
    @Column(name = "photo")
    private String photo;

	    //audit_state审核标志（审核中 通过 拒绝）
    @Column(name = "audit_flag")
    private String auditFlag;

	    //备注信息
    @Column(name = "remarks")
    private String remarks;

	    //字典enabled_flag 有效标志(y:有效；n：无效)
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
	 * 设置：id
	 */
	public void setPvRecordId(String pvRecordId) {
		this.pvRecordId = pvRecordId;
	}
	/**
	 * 获取：id
	 */
	public String getPvRecordId() {
		return pvRecordId;
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
	 * 设置：法人代表名字
	 */
	public void setCorporateName(String corporateName) {
		this.corporateName = corporateName;
	}
	/**
	 * 获取：法人代表名字
	 */
	public String getCorporateName() {
		return corporateName;
	}
	/**
	 * 设置：电话号码
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	/**
	 * 获取：电话号码
	 */
	public String getTelephone() {
		return telephone;
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
	 * 设置：开始时间
	 */
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	/**
	 * 获取：开始时间
	 */
	public String getBeginDate() {
		return beginDate;
	}
	/**
	 * 设置：结束时间
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	/**
	 * 获取：结束时间
	 */
	public String getEndDate() {
		return endDate;
	}
	/**
	 * 设置：停车场地址
	 */
	public void setParkingAddress(String parkingAddress) {
		this.parkingAddress = parkingAddress;
	}
	/**
	 * 获取：停车场地址
	 */
	public String getParkingAddress() {
		return parkingAddress;
	}
	/**
	 * 设置：申请单位
	 */
	public void setApplicantUnit(String applicantUnit) {
		this.applicantUnit = applicantUnit;
	}
	/**
	 * 获取：申请单位
	 */
	public String getApplicantUnit() {
		return applicantUnit;
	}
	/**
	 * 设置：字典parking_type 停车场类型(室内  路侧)
	 */
	public void setParkingType(String parkingType) {
		this.parkingType = parkingType;
	}
	/**
	 * 获取：字典parking_type 停车场类型(室内  路侧)
	 */
	public String getParkingType() {
		return parkingType;
	}
	/**
	 * 设置：字典aera_type区层类型（地面 地下 地面地下都有）
	 */
	public void setAreaType(String areaType) {
		this.areaType = areaType;
	}
	/**
	 * 获取：字典aera_type区层类型（地面 地下 地面地下都有）
	 */
	public String getAreaType() {
		return areaType;
	}
	/**
	 * 设置：出租时段（周一周二周三）
	 */
	public void setRentalPeriod(String rentalPeriod) {
		this.rentalPeriod = rentalPeriod;
	}
	/**
	 * 获取：出租时段（周一周二周三）
	 */
	public String getRentalPeriod() {
		return rentalPeriod;
	}
	/**
	 * 设置：占地面积（单位：平方米）
	 */
	public void setFloorSpace(String floorSpace) {
		this.floorSpace = floorSpace;
	}
	/**
	 * 获取：占地面积（单位：平方米）
	 */
	public String getFloorSpace() {
		return floorSpace;
	}
	/**
	 * 设置：车位数量
	 */
	public void setSpaceNums(String spaceNums) {
		this.spaceNums = spaceNums;
	}
	/**
	 * 获取：车位数量
	 */
	public String getSpaceNums() {
		return spaceNums;
	}
	/**
	 * 设置：图片
	 */
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	/**
	 * 获取：图片
	 */
	public String getPhoto() {
		return photo;
	}
	/**
	 * 设置：audit_state审核标志（审核中 通过 拒绝）
	 */
	public void setAuditFlag(String auditFlag) {
		this.auditFlag = auditFlag;
	}
	/**
	 * 获取：audit_state审核标志（审核中 通过 拒绝）
	 */
	public String getAuditFlag() {
		return auditFlag;
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
	 * 设置：字典enabled_flag 有效标志(y:有效；n：无效)
	 */
	public void setEnabledFlag(String enabledFlag) {
		this.enabledFlag = enabledFlag;
	}
	/**
	 * 获取：字典enabled_flag 有效标志(y:有效；n：无效)
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
