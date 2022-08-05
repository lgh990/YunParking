package com.smart.iot.parking.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
 * 罚单表
 *
 * @author YUNCITYS
 * @email YUNCITYS@smart.iot.com
 * @version 2022-04-10 14:00:01
 */
@Table(name = "inspectiondetail")
public class Inspectiondetail implements Serializable {
	private static final long serialVersionUID = 1L;

	    //主键
    @Id
    private String id;

	    //有效标志(Y:有效；N：无效)
    @Column(name = "ENABLED_FLAG")
    private String enabledFlag;

	    //创建人
    @Column(name = "CREATED_BY")
    private String createdBy;

	    //创建时间
    @Column(name = "CREATED_DATE")
    private Date createdDate;

	    //修改人
    @Column(name = "LAST_UPDATE_BY")
    private String lastUpdateBy;

	    //修改时间
    @Column(name = "LAST_UPDATE_DATE")
    private Date lastUpdateDate;

	    //所属系统
    @Column(name = "DATA_OWNED_SYS")
    private Integer dataOwnedSys;

	    //违章地址
    @Column(name = "ADDR")
    private String addr;

	    //车颜色
    @Column(name = "CAR_COLOR")
    private String carColor;

	    //车类型
    @Column(name = "CAR_TYPE")
    private String carType;

	    //罚单号
    @Column(name = "DETAILSN")
    private String detailsn;

	    //细节照片
    @Column(name = "DETAIL_IMG")
    private String detailImg;

	    //巡检员
    @Column(name = "INSP_ID")
    private Integer inspId;

	    //纬度
    @Column(name = "LATITUDE")
    private String latitude;

	    //经度
    @Column(name = "LONGITUDE")
    private String longitude;

	    //停车场
    @Column(name = "PARKING_ID")
    private Integer parkingId;

	    //车牌颜色
    @Column(name = "PLATECOLOR")
    private String platecolor;

	    //车牌号码
    @Column(name = "PLATENUM")
    private String platenum;

	    //场景照片
    @Column(name = "SCENE_IMG")
    private String sceneImg;

	    //车位
    @Column(name = "SPACE_ID")
    private Integer spaceId;

	    //处理状态(0未处理 1已缴费 2已补缴)
    @Column(name = "STATUS")
    private String status;

	    //罚单照片
    @Column(name = "TICKETS_IMG")
    private String ticketsImg;

	    //补缴用户
    @Column(name = "USER_ID")
    private Integer userId;

	    //备注
    @Column(name = "REMARKS")
    private String remarks;

	    //
    @Column(name = "LM_ID")
    private Integer lmId;

	    //
    @Column(name = "ATTRIBUTE1")
    private Integer attribute1;

	    //
    @Column(name = "ATTRIBUTE2")
    private Integer attribute2;

	    //
    @Column(name = "ATTRIBUTE3")
    private String attribute3;

	    //
    @Column(name = "ATTRIBUTE4")
    private String attribute4;


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
	 * 设置：有效标志(Y:有效；N：无效)
	 */
	public void setEnabledFlag(String enabledFlag) {
		this.enabledFlag = enabledFlag;
	}
	/**
	 * 获取：有效标志(Y:有效；N：无效)
	 */
	public String getEnabledFlag() {
		return enabledFlag;
	}
	/**
	 * 设置：创建人
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	/**
	 * 获取：创建人
	 */
	public String getCreatedBy() {
		return createdBy;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreatedDate() {
		return createdDate;
	}
	/**
	 * 设置：修改人
	 */
	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}
	/**
	 * 获取：修改人
	 */
	public String getLastUpdateBy() {
		return lastUpdateBy;
	}
	/**
	 * 设置：修改时间
	 */
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getLastUpdateDate() {
		return lastUpdateDate;
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
	 * 设置：违章地址
	 */
	public void setAddr(String addr) {
		this.addr = addr;
	}
	/**
	 * 获取：违章地址
	 */
	public String getAddr() {
		return addr;
	}
	/**
	 * 设置：车颜色
	 */
	public void setCarColor(String carColor) {
		this.carColor = carColor;
	}
	/**
	 * 获取：车颜色
	 */
	public String getCarColor() {
		return carColor;
	}
	/**
	 * 设置：车类型
	 */
	public void setCarType(String carType) {
		this.carType = carType;
	}
	/**
	 * 获取：车类型
	 */
	public String getCarType() {
		return carType;
	}
	/**
	 * 设置：罚单号
	 */
	public void setDetailsn(String detailsn) {
		this.detailsn = detailsn;
	}
	/**
	 * 获取：罚单号
	 */
	public String getDetailsn() {
		return detailsn;
	}
	/**
	 * 设置：细节照片
	 */
	public void setDetailImg(String detailImg) {
		this.detailImg = detailImg;
	}
	/**
	 * 获取：细节照片
	 */
	public String getDetailImg() {
		return detailImg;
	}
	/**
	 * 设置：巡检员
	 */
	public void setInspId(Integer inspId) {
		this.inspId = inspId;
	}
	/**
	 * 获取：巡检员
	 */
	public Integer getInspId() {
		return inspId;
	}
	/**
	 * 设置：纬度
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	/**
	 * 获取：纬度
	 */
	public String getLatitude() {
		return latitude;
	}
	/**
	 * 设置：经度
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	/**
	 * 获取：经度
	 */
	public String getLongitude() {
		return longitude;
	}
	/**
	 * 设置：停车场
	 */
	public void setParkingId(Integer parkingId) {
		this.parkingId = parkingId;
	}
	/**
	 * 获取：停车场
	 */
	public Integer getParkingId() {
		return parkingId;
	}
	/**
	 * 设置：车牌颜色
	 */
	public void setPlatecolor(String platecolor) {
		this.platecolor = platecolor;
	}
	/**
	 * 获取：车牌颜色
	 */
	public String getPlatecolor() {
		return platecolor;
	}
	/**
	 * 设置：车牌号码
	 */
	public void setPlatenum(String platenum) {
		this.platenum = platenum;
	}
	/**
	 * 获取：车牌号码
	 */
	public String getPlatenum() {
		return platenum;
	}
	/**
	 * 设置：场景照片
	 */
	public void setSceneImg(String sceneImg) {
		this.sceneImg = sceneImg;
	}
	/**
	 * 获取：场景照片
	 */
	public String getSceneImg() {
		return sceneImg;
	}
	/**
	 * 设置：车位
	 */
	public void setSpaceId(Integer spaceId) {
		this.spaceId = spaceId;
	}
	/**
	 * 获取：车位
	 */
	public Integer getSpaceId() {
		return spaceId;
	}
	/**
	 * 设置：处理状态(0未处理 1已缴费 2已补缴)
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * 获取：处理状态(0未处理 1已缴费 2已补缴)
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * 设置：罚单照片
	 */
	public void setTicketsImg(String ticketsImg) {
		this.ticketsImg = ticketsImg;
	}
	/**
	 * 获取：罚单照片
	 */
	public String getTicketsImg() {
		return ticketsImg;
	}
	/**
	 * 设置：补缴用户
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	/**
	 * 获取：补缴用户
	 */
	public Integer getUserId() {
		return userId;
	}
	/**
	 * 设置：备注
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	/**
	 * 获取：备注
	 */
	public String getRemarks() {
		return remarks;
	}
	/**
	 * 设置：
	 */
	public void setLmId(Integer lmId) {
		this.lmId = lmId;
	}
	/**
	 * 获取：
	 */
	public Integer getLmId() {
		return lmId;
	}
	/**
	 * 设置：
	 */
	public void setAttribute1(Integer attribute1) {
		this.attribute1 = attribute1;
	}
	/**
	 * 获取：
	 */
	public Integer getAttribute1() {
		return attribute1;
	}
	/**
	 * 设置：
	 */
	public void setAttribute2(Integer attribute2) {
		this.attribute2 = attribute2;
	}
	/**
	 * 获取：
	 */
	public Integer getAttribute2() {
		return attribute2;
	}
	/**
	 * 设置：
	 */
	public void setAttribute3(String attribute3) {
		this.attribute3 = attribute3;
	}
	/**
	 * 获取：
	 */
	public String getAttribute3() {
		return attribute3;
	}
	/**
	 * 设置：
	 */
	public void setAttribute4(String attribute4) {
		this.attribute4 = attribute4;
	}
	/**
	 * 获取：
	 */
	public String getAttribute4() {
		return attribute4;
	}
}
