package com.smart.iot.parking.entity;

import com.smart.iot.parking.biz.ParkingBiz;
import com.smart.iot.parking.biz.ParkingBusinessTypeBiz;
import com.smart.iot.parking.feign.ChargeRulesTypeFeign;
import com.smart.iot.parking.feign.DictFeign;
import com.smart.iot.parking.feign.UserFeign;
import com.yuncitys.smart.merge.annonation.MergeField;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 停车场
 *
 * @author Mr.AG
 * @email
 *@version 2022-08-10 17:17:41
 */
@Table(name = "parking")
@Api(value="停车场信息")
public class Parking implements Serializable {
	private static final long serialVersionUID = 1L;

	    //停车场id
	@Id
	@ApiModelProperty(value="停车场id")
	private String parkingId;

	    //
	@Version
	@Column(name = "version")
	@ApiModelProperty(value="版本号")
    private Integer version;

	    //城市id
    @Column(name = "city_id")
	@ApiModelProperty(value="城市id")
    private String cityId;

	    //剩余车位数
	@MergeField(feign = ParkingBiz.class,method = "queryLeftSpaceNum",isValueNeedMerge = true)
	@Column(name = "left_num")
	@ApiModelProperty(value="剩余车位数")
    private String leftNum;

	    //停车场地址
    @Column(name = "parking_address")
	@ApiModelProperty(value="停车场地址")
    private String parkingAddress;

	//停车场收入
	@Column(name = "parking_revenue")
	@ApiModelProperty(value="停车场收入")
	private BigDecimal parkingRevenue;

	//停车场描述
    @Column(name = "parking_description")
	@ApiModelProperty(value="停车场描述")
    private String parkingDescription;

	    //停车场层数
    @Column(name = "parking_layer")
	@ApiModelProperty(value="停车场层数")
    private String parkingLayer;

	    //停车场名称
    @Column(name = "parking_name")
	@ApiModelProperty(value="停车场名称")
    private String parkingName;

	    //是否对外开放
    @Column(name = "parking_open")
	@ApiModelProperty(value="是否对外开放")
    private String parkingOpen;

	@MergeField(key = "parking_type", feign = DictFeign.class, method = "getDictValues")
    @Column(name = "parking_type")
	@ApiModelProperty(value="停车场类型（室内=onsize，路测=roadside）")
    private String parkingType;

    @Column(name = "parking_bus_type")
	@ApiModelProperty(value="停车场业务流程")
    private String parkingBusType;

	@MergeField(key = "parking_bus_model", feign = DictFeign.class, method = "getDictValues")
    @Column(name = "parking_bus_model")
	@ApiModelProperty(value="停车场经营模式（非自营=nonSelfSupport，自营=selfSupport）")
    private String parkingBusModel;

	    //纬度
    @Column(name = "point_lat")
	@ApiModelProperty(value="纬度")
    private String pointLat;

	    //经度
    @Column(name = "point_lng")
	@ApiModelProperty(value="经度")
    private String pointLng;

	    //总车位数
	@MergeField(feign = ParkingBiz.class,method = "querySpaceNumTotal",isValueNeedMerge = true)
	@Column(name = "total_num")
	@ApiModelProperty(value="总车位数")
    private String totalNum;

	@Column(name = "user_id")
	@ApiModelProperty(value="用户编号")
    private String userId;

	    //进场数
    @Column(name = "approach_num")
	@ApiModelProperty(value="进场数")
    private Integer approachNum;

	@MergeField(key = "enabled_flag", feign = DictFeign.class, method = "getDictValues")
    @Column(name = "enabled_flag")
	@ApiModelProperty(value="有效标志(y:有效；n：无效)")
    private String enabledFlag;

	    //所属系统
    @Column(name = "data_owned_sys")
    private Integer dataOwnedSys;

	@Column(name = "charge_rule_id")
	@ApiModelProperty(value="收费规则id ")
    private String chargeRuleId;

	//收费规则类型
	@Column(name = "first_hour_price")
	@ApiModelProperty(value="首小时收费 ")
	private BigDecimal firstHourPrice;

	//创建时间
	@ApiModelProperty(value="创建时间 ")
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

	    //租户id
    @Column(name = "tenant_id")
    private String tenantId;


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
	 * 设置：城市id
	 */
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	/**
	 * 获取：城市id
	 */
	public String getCityId() {
		return cityId;
	}
	/**
	 * 设置：剩余车位数
	 */
	public void setLeftNum(String leftNum) {
		this.leftNum = leftNum;
	}
	/**
	 * 获取：剩余车位数
	 */
	public String getLeftNum() {
		return leftNum;
	}


	/**
	 * 设置：停车场收入
	 */
	public void setParkingRevenue(BigDecimal parkingRevenue) {
		this.parkingRevenue = parkingRevenue;
	}
	/**
	 * 获取：停车场收入
	 */
	public BigDecimal getParkingRevenue() {
		return parkingRevenue;
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
	 * 设置：停车场描述
	 */
	public void setParkingDescription(String parkingDescription) {
		this.parkingDescription = parkingDescription;
	}
	/**
	 * 获取：停车场描述
	 */
	public String getParkingDescription() {
		return parkingDescription;
	}
	/**
	 * 设置：停车场层数
	 */
	public void setParkingLayer(String parkingLayer) {
		this.parkingLayer = parkingLayer;
	}
	/**
	 * 获取：停车场层数
	 */
	public String getParkingLayer() {
		return parkingLayer;
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
	 * 设置：是否对外开放
	 */
	public void setParkingOpen(String parkingOpen) {
		this.parkingOpen = parkingOpen;
	}
	/**
	 * 获取：是否对外开放
	 */
	public String getParkingOpen() {
		return parkingOpen;
	}
	/**
	 * 设置：停车场类型(0=室内停车场  1=是路边停车场)
	 */
	public void setParkingType(String parkingType) {
		this.parkingType = parkingType;
	}
	/**
	 * 获取：停车场类型(0=室内停车场  1=是路边停车场)
	 */
	public String getParkingType() {
		return parkingType;
	}
	/**
	 * 设置：停车场业务流程
	 */
	public void setParkingBusType(String parkingBusType) {
		this.parkingBusType = parkingBusType;
	}
	/**
	 * 获取：停车场业务流程
	 */
	public String getParkingBusType() {
		return parkingBusType;
	}
	/**
	 * 设置：停车场经营模式(0=自营  1=非自营)
	 */
	public void setParkingBusModel(String parkingBusModel) {
		this.parkingBusModel = parkingBusModel;
	}
	/**
	 * 获取：停车场经营模式(0=自营  1=非自营)
	 */
	public String getParkingBusModel() {
		return parkingBusModel;
	}
	/**
	 * 设置：经纬度
	 */
	public void setPointLat(String pointLat) {
		this.pointLat = pointLat;
	}
	/**
	 * 获取：经纬度
	 */
	public String getPointLat() {
		return pointLat;
	}
	/**
	 * 设置：经纬度
	 */
	public void setPointLng(String pointLng) {
		this.pointLng = pointLng;
	}
	/**
	 * 获取：经纬度
	 */
	public String getPointLng() {
		return pointLng;
	}
	/**
	 * 设置：总车位数
	 */
	public void setTotalNum(String totalNum) {
		this.totalNum = totalNum;
	}
	/**
	 * 获取：总车位数
	 */
	public String getTotalNum() {
		return totalNum;
	}
	/**
	 * 设置：绑定用户id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * 获取：绑定用户id
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * 设置：进场数
	 */
	public void setApproachNum(Integer approachNum) {
		this.approachNum = approachNum;
	}
	/**
	 * 获取：进场数
	 */
	public Integer getApproachNum() {
		return approachNum;
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
	 * 设置：收费规则类型
	 */
	public void setChargeRuleId(String chargeRuleId) {
		this.chargeRuleId = chargeRuleId;
	}
	/**
	 * 获取：收费规则类型
	 */
	public String getChargeRuleId() {
		return chargeRuleId;
	}
	/**
	 * 设置：收费规则类型
	 */
	public void setFirstHourPrice(BigDecimal firstHourPrice) {
		this.firstHourPrice = firstHourPrice;
	}
	/**
	 * 获取：收费规则类型
	 */
	public BigDecimal getFirstHourPrice() {
		return firstHourPrice;
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
