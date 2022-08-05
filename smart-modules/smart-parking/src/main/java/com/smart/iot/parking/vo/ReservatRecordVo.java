package com.smart.iot.parking.vo;

import com.smart.iot.parking.entity.*;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


/**
 * 导航记录表
 *
 * @author heyaohuan
 * @email 
 *@version 2022-09-11 18:49:28
 */
public class ReservatRecordVo implements Serializable {
	private static final long serialVersionUID = 1L;

	//车牌id
	@Id
	private String id;

	private String plaId;

	//用户id
	private String userId;

	//车位id
	private String spaceId;

	//停车场id
	private String parkingId;

	//开始时间
	private String beginDate;
	//预定时间
	private String reservatDate;

	//保留时间
	private String retentionTime;
	//结束时间
	private String endDate;

	//字典proce_status
	private String reservatState;

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

	private Plate plate;

	private AppUser appUser;

	private ParkingSpace parkingSpace;

	private Parking parking;

	public ReservatRecordVo(ReservatRecord reservatRecord) {
		this.id = reservatRecord.getId();
		this.plaId = reservatRecord.getPlaId();
		this.userId = reservatRecord.getUserId();
		this.spaceId = reservatRecord.getSpaceId();
		this.parkingId = reservatRecord.getParkingId();
		this.beginDate = reservatRecord.getBeginDate();
		this.reservatDate = reservatRecord.getReservatDate();
		this.retentionTime = reservatRecord.getRetentionTime();
		this.endDate = reservatRecord.getEndDate();
		this.reservatState = reservatRecord.getReservatState();
		this.enabledFlag = reservatRecord.getEnabledFlag();
		this.crtTime = reservatRecord.getCrtTime();
		this.crtUser = reservatRecord.getCrtUser();
		this.crtName = reservatRecord.getCrtName();
		this.crtHost = reservatRecord.getCrtHost();
		this.updTime = reservatRecord.getUpdTime();
		this.updUser = reservatRecord.getUpdUser();
		this.updName = reservatRecord.getUpdName();
		this.updHost = reservatRecord.getUpdHost();
		this.attr1 = reservatRecord.getAttr1();
		this.attr2 = reservatRecord.getAttr2();
		this.attr3 = reservatRecord.getAttr3();
		this.attr4 = reservatRecord.getAttr4();
		this.attr5 = reservatRecord.getAttr5();
		this.attr6 = reservatRecord.getAttr6();
		this.attr7 = reservatRecord.getAttr7();
		this.attr8 = reservatRecord.getAttr8();
	}

	public Plate getPlate() {
		return plate;
	}

	public void setPlate(Plate plate) {
		this.plate = plate;
	}

	public AppUser getAppUser() {
		return appUser;
	}

	public void setAppUser(AppUser appUser) {
		this.appUser = appUser;
	}

	public ParkingSpace getParkingSpace() {
		return parkingSpace;
	}

	public void setParkingSpace(ParkingSpace parkingSpace) {
		this.parkingSpace = parkingSpace;
	}

	public Parking getParking() {
		return parking;
	}

	public void setParking(Parking parking) {
		this.parking = parking;
	}


	/**
	 * 设置：预定时间
	 */
	public void setReservatDate(String reservatDate) {
		this.reservatDate = reservatDate;
	}
	/**
	 * 获取：预定时间
	 */
	public String getReservatDate() {
		return reservatDate;
	}
	/**
	 * 设置：保留时间
	 */
	public void setRetentionTime(String retentionTime) {
		this.retentionTime = retentionTime;
	}
	/**
	 * 获取：保留时间
	 */
	public String getRetentionTime() {
		return retentionTime;
	}

	/**
	 * 设置：车牌id
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 获取：车牌id
	 */
	public String getId() {
		return id;
	}
	/**
	 * 设置：车牌号
	 */
	public void setPlaId(String plaId) {
		this.plaId = plaId;
	}
	/**
	 * 获取：车牌号
	 */
	public String getPlaId() {
		return plaId;
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
	 * 设置：字典proce_status
	 */
	public void setReservatState(String reservatState) {
		this.reservatState = reservatState;
	}
	/**
	 * 获取：字典proce_status
	 */
	public String getReservatState() {
		return reservatState;
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
