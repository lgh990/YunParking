package com.smart.iot.parking.vo;

import com.smart.iot.parking.biz.ParkingBiz;
import com.smart.iot.parking.biz.ParkingBusinessTypeBiz;
import com.smart.iot.parking.entity.Parking;
import com.smart.iot.parking.entity.ParkingBusinessType;
import com.smart.iot.parking.feign.ChargeRulesTypeFeign;
import com.smart.iot.parking.feign.DictFeign;
import com.smart.iot.parking.feign.UserFeign;
import com.yuncitys.smart.merge.annonation.MergeField;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 停车场
 *
 * @author Mr.AG
 * @email
 *@version 2022-08-10 17:17:41
 */
public class ParkingVo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String parkingId;

    private Integer version;

    private String cityId;

    private String leftNum;

    private String parkingAddress;

	private BigDecimal parkingRevenue;

    private String parkingDescription;

    private String parkingLayer;

    private String parkingName;

    private String parkingOpen;

    private String parkingType;

    private String parkingBusType;

    private String parkingBusModel;

    private String pointLat;

    private String pointLng;

    private String totalNum;

    private String userId;

    private Integer approachNum;

    private String enabledFlag;

    private Integer dataOwnedSys;

    private String chargeRuleId;

	private BigDecimal firstHourPrice;

    private Date crtTime;

    private String crtUser;

    private String crtName;

    private String crtHost;

    private Date updTime;

    private String updUser;

    private String updName;

    private String updHost;

    private String attr1;

    private String attr2;

    private String attr3;

    private String attr4;

    private String attr5;

    private String attr6;

    private String attr7;

    private String attr8;

    private String tenantId;

    private Map chargeRule;

	private Map user;

	private ParkingBusinessType parkingBusTypeEntity;

	public ParkingVo(Parking parking) {
		this.parkingId = parking.getParkingId();
		this.version = parking.getVersion();
		this.cityId = parking.getCityId();
		this.leftNum = parking.getLeftNum();
		this.parkingAddress = parking.getParkingAddress();
		this.parkingRevenue = parking.getParkingRevenue();
		this.parkingDescription = parking.getParkingDescription();
		this.parkingLayer = parking.getParkingLayer();
		this.parkingName = parking.getParkingName();
		this.parkingOpen = parking.getParkingOpen();
		this.parkingType = parking.getParkingType();
		this.parkingBusType = parking.getParkingBusType();
		this.parkingBusModel = parking.getParkingBusModel();
		this.pointLat = parking.getPointLat();
		this.pointLng = parking.getPointLng();
		this.totalNum = parking.getTotalNum();
		this.userId = parking.getUserId();
		this.approachNum = parking.getApproachNum();
		this.enabledFlag = parking.getEnabledFlag();
		this.dataOwnedSys = parking.getDataOwnedSys();
		this.chargeRuleId = parking.getChargeRuleId();
		this.firstHourPrice = parking.getFirstHourPrice();
		this.crtTime = parking.getCrtTime();
		this.crtUser = parking.getCrtUser();
		this.crtName = parking.getCrtName();
		this.crtHost = parking.getCrtHost();
		this.updTime = parking.getUpdTime();
		this.updUser = parking.getUpdUser();
		this.updName = parking.getUpdName();
		this.updHost = parking.getUpdHost();
		this.attr1 = parking.getAttr1();
		this.attr2 = parking.getAttr2();
		this.attr3 = parking.getAttr3();
		this.attr4 = parking.getAttr4();
		this.attr5 = parking.getAttr5();
		this.attr6 = parking.getAttr6();
		this.attr7 = parking.getAttr7();
		this.attr8 = parking.getAttr8();
		this.tenantId = parking.getTenantId();
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

	public Map getChargeRule() {
		return chargeRule;
	}

	public void setChargeRule(Map chargeRule) {
		this.chargeRule = chargeRule;
	}

	public Map getUser() {
		return user;
	}

	public void setUser(Map user) {
		this.user = user;
	}

	public ParkingBusinessType getParkingBusTypeEntity() {
		return parkingBusTypeEntity;
	}

	public void setParkingBusTypeEntity(ParkingBusinessType parkingBusTypeEntity) {
		this.parkingBusTypeEntity = parkingBusTypeEntity;
	}
}
