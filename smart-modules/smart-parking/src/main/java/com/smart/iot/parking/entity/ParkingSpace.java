package com.smart.iot.parking.entity;

import com.smart.iot.parking.biz.ParkingAreaBiz;
import com.smart.iot.parking.biz.ParkingBiz;
import com.smart.iot.parking.feign.DictFeign;
import com.yuncitys.smart.merge.annonation.MergeField;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;

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
@Table(name = "parking_space")
@Api(value="车位信息")
public class ParkingSpace implements Serializable {
	private static final long serialVersionUID = 1L;

	    //车位id
	@Id
	@ApiModelProperty(value="车位id")
	private String spaceId;

	    //
	@tk.mybatis.mapper.annotation.Version
	@Column(name = "version")
	@ApiModelProperty(value="版本号")
	private Integer version;

	    //朝向：090180270=上右下左
    @Column(name = "angle")
	@ApiModelProperty(value="车位朝向")
    private Integer angle;

	@MergeField(key = "enabled_flag", feign = DictFeign.class, method = "getDictValues")
    @Column(name = "lot_type")
	@ApiModelProperty(value="车位状态（Y=有，N=无）")
    private String lotType;

	@MergeField(key = "enabled_flag", feign = DictFeign.class, method = "getDictValues")
    @Column(name = "lockl")
	@ApiModelProperty(value="锁状态（Y 开  n 关）")
    private String lockl;

	@MergeField(key = "space_type", feign = DictFeign.class, method = "getDictValues")
    @Column(name = "space_type")
	@ApiModelProperty(value="车位类型（普通车位=common，私人车位=private，临时车位=temporary，vip车位=vip）")
    private String spaceType;
	@ApiModelProperty(value="区层id")
	@Column(name = "area_id")
    private String areaId;

	    //停车场
	@Column(name = "parking_id")
	@ApiModelProperty(value="停车场id")
    private String parkingId;

	    //车位号
    @Column(name = "space_num")
	@ApiModelProperty(value="车位号")
    private String spaceNum;

	@MergeField(key = "error_type", feign = DictFeign.class, method = "getDictValues")
    @Column(name = "space_status")
	@ApiModelProperty(value="异常类型（通讯异常=communication，流水异常=flow，地磁上报异常=geomagnetic ，地磁时间异常=geomagneticTime，手持机上报异常=handheld_machine，正常=normal）")
    private String spaceStatus;

	    //0为不占用，1为占用上面，2为占用右边，3为占用下方，4为占用左边
    @Column(name = "toward")
	@ApiModelProperty(value="0为不占用，1为占用上面，2为占用右边，3为占用下方，4为占用左边")
    private String toward;

	    //横向坐标
    @Column(name = "abscissa")
	@ApiModelProperty(value="横向坐标")
    private String abscissa;

	    //纵向坐标
    @Column(name = "ordinate")
	@ApiModelProperty(value="纵向坐标")
    private String ordinate;

	    //车位纬度
    @Column(name = "latitude")
	@ApiModelProperty(value="车位纬度")
    private String latitude;

	    //车位经度
    @Column(name = "longitude")
	@ApiModelProperty(value="车位经度")
    private String longitude;

	    //缩放比例：100=1:1
    @Column(name = "zoom")
	@ApiModelProperty(value="缩放比例：100=1:")
    private Integer zoom;

	    //车位号
    @Column(name = "berth_number")
	@ApiModelProperty(value="车位号")
    private String berthNumber;

	    //业主id
    @Column(name = "user_id")
	@ApiModelProperty(value="业主id")
    private String userId;

	    //有效标志(y:有效；n：无效)
	@MergeField(key = "enabled_flag", feign = DictFeign.class, method = "getDictValues")
	@Column(name = "enabled_flag")
	@ApiModelProperty(value="有效标志(y:有效；n：无效)")
    private String enabledFlag;
	//是否包含充电桩
	@MergeField(key = "charge_pile", feign = DictFeign.class, method = "getDictValues")
	@Column(name = "charge_pile")
	@ApiModelProperty(value="是否包含充电桩(y:是；n：否)")
	private String chargePile;

	//创建时间
    @Column(name = "crt_time")
	@ApiModelProperty(value="创建时间")
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

	    //租户id
    @Column(name = "tenant_id")
    private String tenantId;

	    //
    @Column(name = "crt_user_id")
    private String crtUserId;


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
