package com.smart.iot.parking.vo;

import com.smart.iot.parking.entity.Parking;
import com.smart.iot.parking.entity.ParkingArea;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


/**
 * 区层
 *
 * @author Mr.AG
 * @email 
 *@version 2022-08-30 17:36:01
 */
public class ParkingAreaVo implements Serializable {
	private static final long serialVersionUID = 1L;

	    //创建人
	private String areaId;

	    //背景图片
    private String bgImg;

	    //改变次数
    private Integer changeTimes;

	    //室内布局地图数据
    private String mapData;

	    //区/层名称
    private String areaName;

    private String parkingId;

	    //area_type 区层类型（地下  地上）
    private String areaType;

	    //字典enabled_flag 是否显示背景图片
    private String bgimgFlag;

	    //宽数量
    private Integer height;

	    //高数量
    private Integer width;

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

	    //租户id
    private String tenantId;

	    //
    private String crtUserId;


	private Parking parking;

	public Parking getParking() {
		return parking;
	}

	public void setParking(Parking parking) {
		this.parking = parking;
	}



	public ParkingAreaVo(ParkingArea parkingArea) {
		this.areaId = parkingArea.getAreaId();
		this.bgImg = parkingArea.getBgImg();
		this.changeTimes = parkingArea.getChangeTimes();
		this.mapData = parkingArea.getMapData();
		this.areaName = parkingArea.getAreaName();
		this.parkingId = parkingArea.getParkingId();
		this.areaType = parkingArea.getAreaType();
		this.bgimgFlag = parkingArea.getBgimgFlag();
		this.height = parkingArea.getHeight();
		this.width = parkingArea.getWidth();
		this.enabledFlag = parkingArea.getEnabledFlag();
		this.crtTime = parkingArea.getCrtTime();
		this.crtUser = parkingArea.getCrtUser();
		this.crtName = parkingArea.getCrtName();
		this.crtHost = parkingArea.getCrtHost();
		this.updTime = parkingArea.getUpdTime();
		this.updUser = parkingArea.getUpdUser();
		this.updName = parkingArea.getUpdName();
		this.updHost = parkingArea.getUpdHost();
		this.attr1 = parkingArea.getAttr1();
		this.attr2 = parkingArea.getAttr2();
		this.attr3 = parkingArea.getAttr3();
		this.attr4 = parkingArea.getAttr4();
		this.attr5 = parkingArea.getAttr5();
		this.attr6 = parkingArea.getAttr6();
		this.attr7 = parkingArea.getAttr7();
		this.attr8 = parkingArea.getAttr8();
		this.tenantId = parkingArea.getTenantId();
		this.crtUserId = parkingArea.getCrtUserId();
	}

	/**
	 * 设置：创建人
	 */
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	/**
	 * 获取：创建人
	 */
	public String getAreaId() {
		return areaId;
	}
	/**
	 * 设置：背景图片
	 */
	public void setBgImg(String bgImg) {
		this.bgImg = bgImg;
	}
	/**
	 * 获取：背景图片
	 */
	public String getBgImg() {
		return bgImg;
	}
	/**
	 * 设置：改变次数
	 */
	public void setChangeTimes(Integer changeTimes) {
		this.changeTimes = changeTimes;
	}
	/**
	 * 获取：改变次数
	 */
	public Integer getChangeTimes() {
		return changeTimes;
	}
	/**
	 * 设置：室内布局地图数据
	 */
	public void setMapData(String mapData) {
		this.mapData = mapData;
	}
	/**
	 * 获取：室内布局地图数据
	 */
	public String getMapData() {
		return mapData;
	}
	/**
	 * 设置：区/层名称
	 */
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	/**
	 * 获取：区/层名称
	 */
	public String getAreaName() {
		return areaName;
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
	 * 设置：area_type 区层类型（地下  地上）
	 */
	public void setAreaType(String areaType) {
		this.areaType = areaType;
	}
	/**
	 * 获取：area_type 区层类型（地下  地上）
	 */
	public String getAreaType() {
		return areaType;
	}
	/**
	 * 设置：字典enabled_flag 是否显示背景图片
	 */
	public void setBgimgFlag(String bgimgFlag) {
		this.bgimgFlag = bgimgFlag;
	}
	/**
	 * 获取：字典enabled_flag 是否显示背景图片
	 */
	public String getBgimgFlag() {
		return bgimgFlag;
	}
	/**
	 * 设置：宽数量
	 */
	public void setHeight(Integer height) {
		this.height = height;
	}
	/**
	 * 获取：宽数量
	 */
	public Integer getHeight() {
		return height;
	}
	/**
	 * 设置：高数量
	 */
	public void setWidth(Integer width) {
		this.width = width;
	}
	/**
	 * 获取：高数量
	 */
	public Integer getWidth() {
		return width;
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
