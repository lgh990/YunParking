package com.smart.iot.dev.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


/**
 * 网关表
 *
 * @author yuncitys
 * @email yuncitys@smart.iot.com
 * @version 2022-07-21 13:56:23
 */

public class DevGatewayVo implements Serializable {
	private Integer gwId;
	private Date crtTime;
	private String parkingAddress;
	private String phoneNum;
	private String erpairPhone;
	private String parkingId;
	private String gwCurfreq;
	private String severIpPort;
	private String gwDevNumber;
	private String gwName;
	private String gwDevnumber2;
	private String gwVersion;
	private String gwSn;
	private String parkingName;

	public Integer getGwId() {
		return gwId;
	}

	public void setGwId(Integer gwId) {
		this.gwId = gwId;
	}

	public Date getCrtTime() {
		return crtTime;
	}

	public void setCrtTime(Date crtTime) {
		this.crtTime = crtTime;
	}

	public String getParkingAddress() {
		return parkingAddress;
	}

	public void setParkingAddress(String parkingAddress) {
		this.parkingAddress = parkingAddress;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getErpairPhone() {
		return erpairPhone;
	}

	public void setErpairPhone(String erpairPhone) {
		this.erpairPhone = erpairPhone;
	}

	public String getParkingId() {
		return parkingId;
	}

	public void setParkingId(String parkingId) {
		this.parkingId = parkingId;
	}

	public String getGwCurfreq() {
		return gwCurfreq;
	}

	public void setGwCurfreq(String gwCurfreq) {
		this.gwCurfreq = gwCurfreq;
	}

	public String getSeverIpPort() {
		return severIpPort;
	}

	public void setSeverIpPort(String severIpPort) {
		this.severIpPort = severIpPort;
	}

	public String getGwDevNumber() {
		return gwDevNumber;
	}

	public void setGwDevNumber(String gwDevNumber) {
		this.gwDevNumber = gwDevNumber;
	}

	public String getGwName() {
		return gwName;
	}

	public void setGwName(String gwName) {
		this.gwName = gwName;
	}

	public String getGwDevnumber2() {
		return gwDevnumber2;
	}

	public void setGwDevnumber2(String gwDevnumber2) {
		this.gwDevnumber2 = gwDevnumber2;
	}

	public String getGwVersion() {
		return gwVersion;
	}

	public void setGwVersion(String gwVersion) {
		this.gwVersion = gwVersion;
	}

	public String getGwSn() {
		return gwSn;
	}

	public void setGwSn(String gwSn) {
		this.gwSn = gwSn;
	}

	public String getParkingName() {
		return parkingName;
	}

	public void setParkingName(String parkingName) {
		this.parkingName = parkingName;
	}


}
