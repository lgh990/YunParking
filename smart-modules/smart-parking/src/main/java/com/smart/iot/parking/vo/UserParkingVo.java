package com.smart.iot.parking.vo;

import com.smart.iot.parking.entity.AppUser;
import com.smart.iot.parking.entity.Parking;
import com.smart.iot.parking.entity.UserParking;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;


/**
 * 用户与停车场关联
 *
 * @author Mr.AG
 * @email
 *@version 2022-08-07 17:30:12
 */
public class UserParkingVo implements Serializable {

	    //关联关系id
	private String fpId;

	    //关联停车场编号
    private String parkingId;

	    //关联人员编号
    private String userId;

	    //有效标志(y:有效；n：无效)
    private String enabledFlag;

	    //所属系统
    private Integer dataOwnedSys;

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

	    //
    private String crtUserId;

	public Parking getParking() {
		return parking;
	}

	public void setParking(Parking parking) {
		this.parking = parking;
	}



	private Parking parking;

	public Map getUser() {
		return user;
	}

	public void setUser(Map user) {
		this.user = user;
	}

	private Map user;

	public UserParkingVo(UserParking userParking) {
		this.fpId = userParking.getFpId();
		this.parkingId = userParking.getParkingId();
		this.userId = userParking.getUserId();
		this.enabledFlag = userParking.getEnabledFlag();
		this.dataOwnedSys = userParking.getDataOwnedSys();
		this.crtTime = userParking.getCrtTime();
		this.crtUser = userParking.getCrtUser();
		this.crtName = userParking.getCrtName();
		this.crtHost = userParking.getCrtHost();
		this.updTime = userParking.getUpdTime();
		this.updUser = userParking.getUpdUser();
		this.updName = userParking.getUpdName();
		this.updHost = userParking.getUpdHost();
		this.attr1 = userParking.getAttr1();
		this.attr2 = userParking.getAttr2();
		this.attr3 = userParking.getAttr3();
		this.attr4 = userParking.getAttr4();
		this.attr5 = userParking.getAttr5();
		this.attr6 = userParking.getAttr6();
		this.attr7 = userParking.getAttr7();
		this.attr8 = userParking.getAttr8();
		this.tenantId = userParking.getTenantId();
		this.crtUserId = userParking.getCrtUserId();
	}


	/**
	 * 设置：关联关系id
	 */
	public void setFpId(String fpId) {
		this.fpId = fpId;
	}
	/**
	 * 获取：关联关系id
	 */
	public String getFpId() {
		return fpId;
	}
	/**
	 * 设置：关联停车场编号
	 */
	public void setParkingId(String parkingId) {
		this.parkingId = parkingId;
	}
	/**
	 * 获取：关联停车场编号
	 */
	public String getParkingId() {
		return parkingId;
	}
	/**
	 * 设置：关联人员编号
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * 获取：关联人员编号
	 */
	public String getUserId() {
		return userId;
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
	 * 设置：所属系统
	 */
	public void setDataOwnedSys(Integer dataOwnedSys) {
		this.dataOwnedSys = dataOwnedSys;
	}
	/**
	 * 获取：所属系统
	 */
	public Integer getDataOwnedSys() {
		return dataOwnedSys;
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
