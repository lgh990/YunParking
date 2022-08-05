package com.smart.iot.dev.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 设备表
 *
 * @author yuncitys
 * @email yuncitys@smart.iot.com
 * @version 2022-07-22 10:13:48
 */
@ApiModel(value="设备表")
@Table(name = "onerankdev")
public class Onerankdev implements Serializable {
	private static final long serialVersionUID = 1L;

	    //id
    @Id
    @ApiModelProperty(value="devId")
    private String devId;

	    //业务类型
    @ApiModelProperty(value="业务类型")
    @Column(name = "onerankdev_type")
    private String onerankdevType;

	    //网关sn
    @ApiModelProperty(value="网关sn")
    @Column(name = "gw_sn")
    private String gwSn;

	    //设备sn
    @ApiModelProperty(value="设备sn")
    @Column(name = "device_sn")
    private String deviceSn;

	    //设备id
    @ApiModelProperty(value="设备id")
    @Column(name = "onerankdev_dev_sn")
    private String onerankdevDevSn;

	    //最新流水时间
    @ApiModelProperty(value="最新流水时间")
    @Column(name = "last_flow_date")
    private String lastFlowDate;

	    //终端id
    @ApiModelProperty(value="终端id")
    @Column(name = "onerankdev_termin_id")
    private String onerankdevTerminId;

	    //流水号
    @ApiModelProperty(value="流水号")
    @Column(name = "flow_num")
    private Integer flowNum;

	    //0=smart,1=光纤
    @ApiModelProperty(value="0=smart,1=光纤")
    @Column(name = "type")
    private Integer type;

	    //厂商
    @ApiModelProperty(value="厂商")
    @Column(name = "manufacturer")
    private String manufacturer;

	    //地址
    @ApiModelProperty(value="地址")
    @Column(name = "address")
    private String address;

	    //设备心跳周期
    @ApiModelProperty(value="设备心跳周期")
    @Column(name = "geomag_bh_period")
    private Integer geomagBhPeriod;

	    //有效标志(y:有效；n：无效)
    @ApiModelProperty(value="有效标志(y:有效；n：无效)")
    @Column(name = "enabled_flag")
    private String enabledFlag;

	    //创建时间
    @ApiModelProperty(value="创建时间")
    @Column(name = "crt_time")
    private Date crtTime;

	    //创建人id
    @ApiModelProperty(value="创建人id")
    @Column(name = "crt_user")
    private String crtUser;

	    //创建人
    @ApiModelProperty(value="创建人")
    @Column(name = "crt_name")
    private String crtName;

	    //创建主机
    @ApiModelProperty(value="创建主机")
    @Column(name = "crt_host")
    private String crtHost;

	    //最后更新时间
    @ApiModelProperty(value="最后更新时间")
    @Column(name = "upd_time")
    private Date updTime;

	    //最后更新人id
    @ApiModelProperty(value="最后更新人id")
    @Column(name = "upd_user")
    private String updUser;

	    //最后更新人
    @ApiModelProperty(value="最后更新人")
    @Column(name = "upd_name")
    private String updName;

	    //最后更新主机
    @ApiModelProperty(value="最后更新主机")
    @Column(name = "upd_host")
    private String updHost;

	    //
    @ApiModelProperty(value="")
    @Column(name = "attr1")
    private String attr1;

	    //
    @ApiModelProperty(value="")
    @Column(name = "attr2")
    private String attr2;

	    //
    @ApiModelProperty(value="")
    @Column(name = "attr3")
    private String attr3;

	    //
    @ApiModelProperty(value="")
    @Column(name = "attr4")
    private String attr4;

	    //
    @ApiModelProperty(value="")
    @Column(name = "attr5")
    private String attr5;

	    //
    @ApiModelProperty(value="")
    @Column(name = "attr6")
    private String attr6;

	    //
    @ApiModelProperty(value="")
    @Column(name = "attr7")
    private String attr7;

	    //
    @ApiModelProperty(value="")
    @Column(name = "attr8")
    private String attr8;

	    //租户id
    @ApiModelProperty(value="租户id")
    @Column(name = "tenant_id")
    private String tenantId;

	    //
    @ApiModelProperty(value="")
    @Column(name = "crt_user_id")
    private String crtUserId;


	/**
	 * 设置：id
	 */
	public void setDevId(String devId) {
		this.devId = devId;
	}
	/**
	 * 获取：id
	 */
	public String getDevId() {
		return devId;
	}
	/**
	 * 设置：业务类型
	 */
	public void setOnerankdevType(String onerankdevType) {
		this.onerankdevType = onerankdevType;
	}
	/**
	 * 获取：业务类型
	 */
	public String getOnerankdevType() {
		return onerankdevType;
	}
	/**
	 * 设置：网关sn
	 */
	public void setGwSn(String gwSn) {
		this.gwSn = gwSn;
	}
	/**
	 * 获取：网关sn
	 */
	public String getGwSn() {
		return gwSn;
	}

	/**
	 * 设置：设备sn
	 */
	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}
	/**
	 * 获取：设备sn
	 */
	public String getDeviceSn() {
		return deviceSn;
	}
	/**
	 * 设置：设备id
	 */
	public void setOnerankdevDevSn(String onerankdevDevSn) {
		this.onerankdevDevSn = onerankdevDevSn;
	}
	/**
	 * 获取：设备id
	 */
	public String getOnerankdevDevSn() {
		return onerankdevDevSn;
	}
	/**
	 * 设置：最新流水时间
	 */
	public void setLastFlowDate(String lastFlowDate) {
		this.lastFlowDate = lastFlowDate;
	}
	/**
	 * 获取：最新流水时间
	 */
	public String getLastFlowDate() {
		return lastFlowDate;
	}
	/**
	 * 设置：终端id
	 */
	public void setOnerankdevTerminId(String onerankdevTerminId) {
		this.onerankdevTerminId = onerankdevTerminId;
	}
	/**
	 * 获取：终端id
	 */
	public String getOnerankdevTerminId() {
		return onerankdevTerminId;
	}
	/**
	 * 设置：流水号
	 */
	public void setFlowNum(Integer flowNum) {
		this.flowNum = flowNum;
	}
	/**
	 * 获取：流水号
	 */
	public Integer getFlowNum() {
		return flowNum;
	}
	/**
	 * 设置：0=smart,1=光纤
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：0=smart,1=光纤
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：厂商
	 */
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	/**
	 * 获取：厂商
	 */
	public String getManufacturer() {
		return manufacturer;
	}
	/**
	 * 设置：地址
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * 获取：地址
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * 设置：设备心跳周期
	 */
	public void setGeomagBhPeriod(Integer geomagBhPeriod) {
		this.geomagBhPeriod = geomagBhPeriod;
	}
	/**
	 * 获取：设备心跳周期
	 */
	public Integer getGeomagBhPeriod() {
		return geomagBhPeriod;
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
