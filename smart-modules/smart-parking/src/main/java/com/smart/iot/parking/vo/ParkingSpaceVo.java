package com.smart.iot.parking.vo;

import com.smart.iot.parking.entity.AppUser;
import com.smart.iot.parking.entity.Parking;
import com.smart.iot.parking.entity.ParkingArea;
import com.smart.iot.parking.entity.ParkingSpace;
import com.smart.iot.parking.feign.DictFeign;
import com.yuncitys.smart.merge.annonation.MergeField;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


/**
 * 车位表
 *
 * @author Mr.AG
 * @email
 *@version 2022-08-08 19:30:58
 */
public class ParkingSpaceVo implements Serializable {
	private static final long serialVersionUID = 1L;

	    //车位id
	private String spaceId;

	    //
	@tk.mybatis.mapper.annotation.Version
	private Integer version;

	    //朝向：090180270=上右下左
    private Integer angle;

    private String lotType;

	private String lockl;

    private String spaceType;

    private String areaId;

    private String parkingId;

	    //车位号
    private String spaceNum;

    private String spaceStatus;

	    //0为不占用，1为占用上面，2为占用右边，3为占用下方，4为占用左边
    private String toward;

	    //横向坐标
    private String abscissa;

	    //纵向坐标
    private String ordinate;

	    //车位纬度
    private String latitude;

	    //车位经度
    private String longitude;

	    //缩放比例：100=1:1
    private Integer zoom;

	    //车位号
    private String berthNumber;

	    //业主id
    private String userId;

	    //有效标志(y:有效；n：无效)
    private String enabledFlag;
	//是否包含充电桩
	private String chargePile;

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

    private Parking parking;

	private ParkingArea parkingArea;

	public AppUser getAppUser() {
		return appUser;
	}

	public void setAppUser(AppUser appUser) {
		this.appUser = appUser;
	}

	private AppUser appUser;

	public Parking getParking() {
		return parking;
	}

	public void setParking(Parking parking) {
		this.parking = parking;
	}

	public ParkingArea getParkingArea() {
		return parkingArea;
	}

	public void setParkingArea(ParkingArea parkingArea) {
		this.parkingArea = parkingArea;
	}




	public ParkingSpaceVo(ParkingSpace parkingSpace) {
		this.spaceId = parkingSpace.getSpaceId();
		this.version = parkingSpace.getVersion();
		this.angle = parkingSpace.getAngle();
		this.lotType = parkingSpace.getLotType();
		this.lockl =parkingSpace.getLockl();
		this.spaceType = parkingSpace.getSpaceType();
		this.areaId = parkingSpace.getAreaId();
		this.parkingId = parkingSpace.getParkingId();
		this.spaceNum = parkingSpace.getSpaceNum();
		this.spaceStatus = parkingSpace.getSpaceStatus();
		this.toward = parkingSpace.getToward();
		this.abscissa = parkingSpace.getAbscissa();
		this.ordinate = parkingSpace.getOrdinate();
		this.latitude = parkingSpace.getLatitude();
		this.longitude = parkingSpace.getLongitude();
		this.zoom = parkingSpace.getZoom();
		this.berthNumber = parkingSpace.getBerthNumber();
		this.userId = parkingSpace.getUserId();
		this.enabledFlag = parkingSpace.getEnabledFlag();
		this.chargePile = parkingSpace.getChargePile();
		this.crtTime = parkingSpace.getCrtTime();
		this.crtUser = parkingSpace.getCrtUser();
		this.crtName = parkingSpace.getCrtName();
		this.crtHost = parkingSpace.getCrtHost();
		this.updTime = parkingSpace.getUpdTime();
		this.updUser = parkingSpace.getUpdUser();
		this.updName = parkingSpace.getUpdName();
		this.updHost = parkingSpace.getUpdHost();
		this.attr1 = parkingSpace.getAttr1();
		this.attr2 = parkingSpace.getAttr2();
		this.attr3 = parkingSpace.getAttr3();
		this.attr4 = parkingSpace.getAttr4();
		this.attr5 = parkingSpace.getAttr5();
		this.attr6 = parkingSpace.getAttr6();
		this.attr7 = parkingSpace.getAttr7();
		this.attr8 = parkingSpace.getAttr8();
		this.tenantId = parkingSpace.getTenantId();
		this.crtUserId = parkingSpace.getCrtUserId();
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
	 * 设置：
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}
	/**
	 * 获取：
	 */
	public Integer getVersion() {
		return version;
	}
	/**
	 * 设置：朝向：090180270=上右下左
	 */
	public void setAngle(Integer angle) {
		this.angle = angle;
	}
	/**
	 * 获取：朝向：090180270=上右下左
	 */
	public Integer getAngle() {
		return angle;
	}
	/**
	 * 设置：1有0无车
	 */
	public void setLotType(String lotType) {
		this.lotType = lotType;
	}
	/**
	 * 获取：1有0无车
	 */
	public String getLotType() {
		return lotType;
	}
	/**
	 * 设置：车位锁状态（on 开  off 关）
	 */
	public void setLockl(String lockl) {
		this.lockl = lockl;
	}
	/**
	 * 获取：车位锁状态（on 开  off 关）
	 */
	public String getLockl() {
		return lockl;
	}
	/**
	 * 设置：车位类型（0、普通，1、会员vip，2、临时，3、私人）
	 */
	public void setSpaceType(String spaceType) {
		this.spaceType = spaceType;
	}
	/**
	 * 获取：车位类型（0、普通，1、会员vip，2、临时，3、私人）
	 */
	public String getSpaceType() {
		return spaceType;
	}
	/**
	 * 设置：区层
	 */
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	/**
	 * 获取：区层
	 */
	public String getAreaId() {
		return areaId;
	}
	/**
	 * 设置：停车场
	 */
	public void setParkingId(String parkingId) {
		this.parkingId = parkingId;
	}
	/**
	 * 获取：停车场
	 */
	public String getParkingId() {
		return parkingId;
	}
	/**
	 * 设置：车位号
	 */
	public void setSpaceNum(String spaceNum) {
		this.spaceNum = spaceNum;
	}
	/**
	 * 获取：车位号
	 */
	public String getSpaceNum() {
		return spaceNum;
	}
	/**
	 * 设置：车位状态：0=待预定/待租；1=已预定/已出租；2=地磁上报异常；3=手持机上报异常 4通讯异常
	 */
	public void setSpaceStatus(String spaceStatus) {
		this.spaceStatus = spaceStatus;
	}
	/**
	 * 获取：车位状态：0=待预定/待租；1=已预定/已出租；2=地磁上报异常；3=手持机上报异常 4通讯异常
	 */
	public String getSpaceStatus() {
		return spaceStatus;
	}
	/**
	 * 设置：0为不占用，1为占用上面，2为占用右边，3为占用下方，4为占用左边
	 */
	public void setToward(String toward) {
		this.toward = toward;
	}
	/**
	 * 获取：0为不占用，1为占用上面，2为占用右边，3为占用下方，4为占用左边
	 */
	public String getToward() {
		return toward;
	}
	/**
	 * 设置：横向坐标
	 */
	public void setAbscissa(String abscissa) {
		this.abscissa = abscissa;
	}
	/**
	 * 获取：横向坐标
	 */
	public String getAbscissa() {
		return abscissa;
	}
	/**
	 * 设置：纵向坐标
	 */
	public void setOrdinate(String ordinate) {
		this.ordinate = ordinate;
	}
	/**
	 * 获取：纵向坐标
	 */
	public String getOrdinate() {
		return ordinate;
	}
	/**
	 * 设置：车位纬度
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	/**
	 * 获取：车位纬度
	 */
	public String getLatitude() {
		return latitude;
	}
	/**
	 * 设置：车位经度
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	/**
	 * 获取：车位经度
	 */
	public String getLongitude() {
		return longitude;
	}
	/**
	 * 设置：缩放比例：100=1:1
	 */
	public void setZoom(Integer zoom) {
		this.zoom = zoom;
	}
	/**
	 * 获取：缩放比例：100=1:1
	 */
	public Integer getZoom() {
		return zoom;
	}
	/**
	 * 设置：车位号
	 */
	public void setBerthNumber(String berthNumber) {
		this.berthNumber = berthNumber;
	}
	/**
	 * 获取：车位号
	 */
	public String getBerthNumber() {
		return berthNumber;
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
	 * 设置：有效标志(y:有效；n：无效)
	 */
	public void setChargePile(String chargePile) {
		this.chargePile = chargePile;
	}
	/**
	 * 获取：有效标志(y:有效；n：无效)
	 */
	public String getChargePile() {
		return chargePile;
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
