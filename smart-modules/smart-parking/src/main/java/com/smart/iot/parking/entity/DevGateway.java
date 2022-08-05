package com.smart.iot.parking.entity;

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
 * @author 云创智城
 * @email 云创智城@smart.iot.com
 * @version 2022-07-08 09:28:51
 */
@ApiModel(value="网关表")
@Table(name = "dev_gateway")
public class DevGateway implements Serializable {
	private static final long serialVersionUID = 1L;

	    //
    @Id
    @ApiModelProperty(value="gwId")
    private Integer gwId;

	    //有效标志(Y:有效；N：无效)
    @ApiModelProperty(value="有效标志(Y:有效；N：无效)")
    @Column(name = "ENABLED_FLAG")
    private String enabledFlag;

	    //创建人
    @ApiModelProperty(value="创建人")
    @Column(name = "CRT_USER")
    private String crtUser;

	    //修改人
    @ApiModelProperty(value="修改人")
    @Column(name = "UPD_USER")
    private String updUser;

	    //修改时间
    @ApiModelProperty(value="修改时间")
    @Column(name = "UPD_DATE")
    private Date updDate;

	    //所属系统
    @ApiModelProperty(value="所属系统")
    @Column(name = "DATA_OWNED_SYS")
    private Integer dataOwnedSys;

	    //网络方式
    @ApiModelProperty(value="网络方式")
    @Column(name = "NETWORK_MODE")
    private String networkMode;

	    //项目字符
    @ApiModelProperty(value="项目字符")
    @Column(name = "PRO_CODE")
    private String proCode;

	    //47=服务器ip,46=TCP服务器1 IP端口
    @ApiModelProperty(value="47=服务器ip,46=TCP服务器1 IP端口")
    @Column(name = "SEVER_IP_PORT")
    private String severIpPort;

	    //心跳周期
    @ApiModelProperty(value="心跳周期")
    @Column(name = "RECV_PERIOD")
    private Integer recvPeriod;

	    //族群ID
    @ApiModelProperty(value="族群ID")
    @Column(name = "SGM_ID")
    private Integer sgmId;

	    //当前所在地
    @ApiModelProperty(value="当前所在地")
    @Column(name = "GW_CRUPOSITION")
    private String gwCruposition;

	    //当前频率
    @ApiModelProperty(value="当前频率")
    @Column(name = "GW_CURFREQ")
    private String gwCurfreq;

	    //47=在网设备数,46=一级网终端个数设备数
    @ApiModelProperty(value="47=在网设备数,46=一级网终端个数设备数")
    @Column(name = "GW_DEVNUMBER")
    private Integer gwDevnumber;

	    //网关名称
    @ApiModelProperty(value="网关名称")
    @Column(name = "GW_NAME")
    private String gwName;

	    //系列号
    @ApiModelProperty(value="系列号")
    @Column(name = "GW_SN")
    private String gwSn;

	    //版本
    @ApiModelProperty(value="版本")
    @Column(name = "GW_VERSION")
    private String gwVersion;

	    //本机号码
    @ApiModelProperty(value="本机号码")
    @Column(name = "PHONE_NUM")
    private String phoneNum;

	    //维护人员号码
    @ApiModelProperty(value="维护人员号码")
    @Column(name = "REPAIR_PHONE")
    private String repairPhone;

	    //复位次数
    @ApiModelProperty(value="复位次数")
    @Column(name = "RESET_TIMES")
    private String resetTimes;

	    //接收到SGM数据计数
    @ApiModelProperty(value="接收到SGM数据计数")
    @Column(name = "SGM_SUM")
    private String sgmSum;

	    //设备数据写索引
    @ApiModelProperty(value="设备数据写索引")
    @Column(name = "DEV_INDEX")
    private String devIndex;

	    //设备数据
    @ApiModelProperty(value="设备数据")
    @Column(name = "DEV_DATA")
    private String devData;

	    //网关模块SN
    @ApiModelProperty(value="网关模块SN")
    @Column(name = "GW_MOD_SN")
    private String gwModSn;

	    //登陆标志
    @ApiModelProperty(value="登陆标志")
    @Column(name = "LOGIN_FLAG")
    private String loginFlag;

	    //无线加密字
    @ApiModelProperty(value="无线加密字")
    @Column(name = "ENCRYPTION_MARK")
    private String encryptionMark;

	    //电池电压检测值1(mV)
    @ApiModelProperty(value="电池电压检测值1(mV)")
    @Column(name = "CURR_VOLTAGE1")
    private String currVoltage1;

	    //电池电压检测值2(mV)
    @ApiModelProperty(value="电池电压检测值2(mV)")
    @Column(name = "CURR_VOLTAGE2")
    private String currVoltage2;

	    //电池电压检测值3(mV)
    @ApiModelProperty(value="电池电压检测值3(mV)")
    @Column(name = "CURR_VOLTAGE3")
    private String currVoltage3;

	    //DS18B20温度值
    @ApiModelProperty(value="DS18B20温度值")
    @Column(name = "DS_TEMP")
    private String dsTemp;

	    //ENS210温度值
    @ApiModelProperty(value="ENS210温度值")
    @Column(name = "EM_TEMP")
    private String emTemp;

	    //ENS210湿度值
    @ApiModelProperty(value="ENS210湿度值")
    @Column(name = "EN_WEB")
    private String enWeb;

	    //开关盖状态
    @ApiModelProperty(value="开关盖状态")
    @Column(name = "SWICOVER_STATUS")
    private String swicoverStatus;

	    //停车场编号
    @ApiModelProperty(value="停车场编号")
    @Column(name = "PARKING_ID")
    private Integer parkingId;

	    //预留字段1
    @ApiModelProperty(value="预留字段1")
    @Column(name = "ATTRIBUTE1")
    private Integer attribute1;

	    //预留字段2
    @ApiModelProperty(value="预留字段2")
    @Column(name = "ATTRIBUTE2")
    private Integer attribute2;

	    //预留字段3
    @ApiModelProperty(value="网关硬件版本")
    @Column(name = "ATTRIBUTE3")
    private String attribute3;

	    //预留字段4
    @ApiModelProperty(value="预留字段4")
    @Column(name = "ATTRIBUTE4")
    private String attribute4;

	    //TCP服务器2 IP端口
    @ApiModelProperty(value="TCP服务器2 IP端口")
    @Column(name = "SEVER_IP_PORT1")
    private String severIpPort1;

	    //二级网终端个数设备数
    @ApiModelProperty(value="二级网终端个数设备数")
    @Column(name = "GW_DEVNUMBER2")
    private Integer gwDevnumber2;

	    //计划内复位次数
    @ApiModelProperty(value="计划内复位次数")
    @Column(name = "PLAN_RESETS_COUNT")
    private String planResetsCount;

	    //数据写地址
    @ApiModelProperty(value="数据写地址")
    @Column(name = "DATA_WRITE_ADDRESS")
    private String dataWriteAddress;

	    //数据读地址
    @ApiModelProperty(value="数据读地址")
    @Column(name = "DATA_READ_ADDRESS")
    private String dataReadAddress;

	    //数据写地址256
    @ApiModelProperty(value="数据写地址256")
    @Column(name = "DATA_WRITE_ADDRESS256")
    private String dataWriteAddress256;

	    //数据读地址256
    @ApiModelProperty(value="数据读地址256")
    @Column(name = "DATA_READ_ADDRESS256")
    private String dataReadAddress256;

	    //网关模块版本号
    @ApiModelProperty(value="网关模块版本号")
    @Column(name = "GW_MODULAR_VERSION_SN")
    private String gwModularVersionSn;

	    //
    @ApiModelProperty(value="")
    @Column(name = "CRT_TIME")
    private Date crtTime;


	/**
	 * 设置：
	 */
	public void setGwId(Integer gwId) {
		this.gwId = gwId;
	}
	/**
	 * 获取：
	 */
	public Integer getGwId() {
		return gwId;
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
	public void setCrtUser(String crtUser) {
		this.crtUser = crtUser;
	}
	/**
	 * 获取：创建人
	 */
	public String getCrtUser() {
		return crtUser;
	}
	/**
	 * 设置：修改人
	 */
	public void setUpdUser(String updUser) {
		this.updUser = updUser;
	}
	/**
	 * 获取：修改人
	 */
	public String getUpdUser() {
		return updUser;
	}
	/**
	 * 设置：修改时间
	 */
	public void setUpdDate(Date updDate) {
		this.updDate = updDate;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getUpdDate() {
		return updDate;
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
	 * 设置：网络方式
	 */
	public void setNetworkMode(String networkMode) {
		this.networkMode = networkMode;
	}
	/**
	 * 获取：网络方式
	 */
	public String getNetworkMode() {
		return networkMode;
	}
	/**
	 * 设置：项目字符
	 */
	public void setProCode(String proCode) {
		this.proCode = proCode;
	}
	/**
	 * 获取：项目字符
	 */
	public String getProCode() {
		return proCode;
	}
	/**
	 * 设置：47=服务器ip,46=TCP服务器1 IP端口
	 */
	public void setSeverIpPort(String severIpPort) {
		this.severIpPort = severIpPort;
	}
	/**
	 * 获取：47=服务器ip,46=TCP服务器1 IP端口
	 */
	public String getSeverIpPort() {
		return severIpPort;
	}
	/**
	 * 设置：心跳周期
	 */
	public void setRecvPeriod(Integer recvPeriod) {
		this.recvPeriod = recvPeriod;
	}
	/**
	 * 获取：心跳周期
	 */
	public Integer getRecvPeriod() {
		return recvPeriod;
	}
	/**
	 * 设置：族群ID
	 */
	public void setSgmId(Integer sgmId) {
		this.sgmId = sgmId;
	}
	/**
	 * 获取：族群ID
	 */
	public Integer getSgmId() {
		return sgmId;
	}
	/**
	 * 设置：当前所在地
	 */
	public void setGwCruposition(String gwCruposition) {
		this.gwCruposition = gwCruposition;
	}
	/**
	 * 获取：当前所在地
	 */
	public String getGwCruposition() {
		return gwCruposition;
	}
	/**
	 * 设置：当前频率
	 */
	public void setGwCurfreq(String gwCurfreq) {
		this.gwCurfreq = gwCurfreq;
	}
	/**
	 * 获取：当前频率
	 */
	public String getGwCurfreq() {
		return gwCurfreq;
	}
	/**
	 * 设置：47=在网设备数,46=一级网终端个数设备数
	 */
	public void setGwDevnumber(Integer gwDevnumber) {
		this.gwDevnumber = gwDevnumber;
	}
	/**
	 * 获取：47=在网设备数,46=一级网终端个数设备数
	 */
	public Integer getGwDevnumber() {
		return gwDevnumber;
	}
	/**
	 * 设置：网关名称
	 */
	public void setGwName(String gwName) {
		this.gwName = gwName;
	}
	/**
	 * 获取：网关名称
	 */
	public String getGwName() {
		return gwName;
	}
	/**
	 * 设置：系列号
	 */
	public void setGwSn(String gwSn) {
		this.gwSn = gwSn;
	}
	/**
	 * 获取：系列号
	 */
	public String getGwSn() {
		return gwSn;
	}
	/**
	 * 设置：版本
	 */
	public void setGwVersion(String gwVersion) {
		this.gwVersion = gwVersion;
	}
	/**
	 * 获取：版本
	 */
	public String getGwVersion() {
		return gwVersion;
	}
	/**
	 * 设置：本机号码
	 */
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	/**
	 * 获取：本机号码
	 */
	public String getPhoneNum() {
		return phoneNum;
	}
	/**
	 * 设置：维护人员号码
	 */
	public void setRepairPhone(String repairPhone) {
		this.repairPhone = repairPhone;
	}
	/**
	 * 获取：维护人员号码
	 */
	public String getRepairPhone() {
		return repairPhone;
	}
	/**
	 * 设置：复位次数
	 */
	public void setResetTimes(String resetTimes) {
		this.resetTimes = resetTimes;
	}
	/**
	 * 获取：复位次数
	 */
	public String getResetTimes() {
		return resetTimes;
	}
	/**
	 * 设置：接收到SGM数据计数
	 */
	public void setSgmSum(String sgmSum) {
		this.sgmSum = sgmSum;
	}
	/**
	 * 获取：接收到SGM数据计数
	 */
	public String getSgmSum() {
		return sgmSum;
	}
	/**
	 * 设置：设备数据写索引
	 */
	public void setDevIndex(String devIndex) {
		this.devIndex = devIndex;
	}
	/**
	 * 获取：设备数据写索引
	 */
	public String getDevIndex() {
		return devIndex;
	}
	/**
	 * 设置：设备数据
	 */
	public void setDevData(String devData) {
		this.devData = devData;
	}
	/**
	 * 获取：设备数据
	 */
	public String getDevData() {
		return devData;
	}
	/**
	 * 设置：网关模块SN
	 */
	public void setGwModSn(String gwModSn) {
		this.gwModSn = gwModSn;
	}
	/**
	 * 获取：网关模块SN
	 */
	public String getGwModSn() {
		return gwModSn;
	}
	/**
	 * 设置：登陆标志
	 */
	public void setLoginFlag(String loginFlag) {
		this.loginFlag = loginFlag;
	}
	/**
	 * 获取：登陆标志
	 */
	public String getLoginFlag() {
		return loginFlag;
	}
	/**
	 * 设置：无线加密字
	 */
	public void setEncryptionMark(String encryptionMark) {
		this.encryptionMark = encryptionMark;
	}
	/**
	 * 获取：无线加密字
	 */
	public String getEncryptionMark() {
		return encryptionMark;
	}
	/**
	 * 设置：电池电压检测值1(mV)
	 */
	public void setCurrVoltage1(String currVoltage1) {
		this.currVoltage1 = currVoltage1;
	}
	/**
	 * 获取：电池电压检测值1(mV)
	 */
	public String getCurrVoltage1() {
		return currVoltage1;
	}
	/**
	 * 设置：电池电压检测值2(mV)
	 */
	public void setCurrVoltage2(String currVoltage2) {
		this.currVoltage2 = currVoltage2;
	}
	/**
	 * 获取：电池电压检测值2(mV)
	 */
	public String getCurrVoltage2() {
		return currVoltage2;
	}
	/**
	 * 设置：电池电压检测值3(mV)
	 */
	public void setCurrVoltage3(String currVoltage3) {
		this.currVoltage3 = currVoltage3;
	}
	/**
	 * 获取：电池电压检测值3(mV)
	 */
	public String getCurrVoltage3() {
		return currVoltage3;
	}
	/**
	 * 设置：DS18B20温度值
	 */
	public void setDsTemp(String dsTemp) {
		this.dsTemp = dsTemp;
	}
	/**
	 * 获取：DS18B20温度值
	 */
	public String getDsTemp() {
		return dsTemp;
	}
	/**
	 * 设置：ENS210温度值
	 */
	public void setEmTemp(String emTemp) {
		this.emTemp = emTemp;
	}
	/**
	 * 获取：ENS210温度值
	 */
	public String getEmTemp() {
		return emTemp;
	}
	/**
	 * 设置：ENS210湿度值
	 */
	public void setEnWeb(String enWeb) {
		this.enWeb = enWeb;
	}
	/**
	 * 获取：ENS210湿度值
	 */
	public String getEnWeb() {
		return enWeb;
	}
	/**
	 * 设置：开关盖状态
	 */
	public void setSwicoverStatus(String swicoverStatus) {
		this.swicoverStatus = swicoverStatus;
	}
	/**
	 * 获取：开关盖状态
	 */
	public String getSwicoverStatus() {
		return swicoverStatus;
	}
	/**
	 * 设置：停车场编号
	 */
	public void setParkingId(Integer parkingId) {
		this.parkingId = parkingId;
	}
	/**
	 * 获取：停车场编号
	 */
	public Integer getParkingId() {
		return parkingId;
	}
	/**
	 * 设置：预留字段1
	 */
	public void setAttribute1(Integer attribute1) {
		this.attribute1 = attribute1;
	}
	/**
	 * 获取：预留字段1
	 */
	public Integer getAttribute1() {
		return attribute1;
	}
	/**
	 * 设置：预留字段2
	 */
	public void setAttribute2(Integer attribute2) {
		this.attribute2 = attribute2;
	}
	/**
	 * 获取：预留字段2
	 */
	public Integer getAttribute2() {
		return attribute2;
	}
	/**
	 * 设置：预留字段3
	 */
	public void setAttribute3(String attribute3) {
		this.attribute3 = attribute3;
	}
	/**
	 * 获取：预留字段3
	 */
	public String getAttribute3() {
		return attribute3;
	}
	/**
	 * 设置：预留字段4
	 */
	public void setAttribute4(String attribute4) {
		this.attribute4 = attribute4;
	}
	/**
	 * 获取：预留字段4
	 */
	public String getAttribute4() {
		return attribute4;
	}
	/**
	 * 设置：TCP服务器2 IP端口
	 */
	public void setSeverIpPort1(String severIpPort1) {
		this.severIpPort1 = severIpPort1;
	}
	/**
	 * 获取：TCP服务器2 IP端口
	 */
	public String getSeverIpPort1() {
		return severIpPort1;
	}
	/**
	 * 设置：二级网终端个数设备数
	 */
	public void setGwDevnumber2(Integer gwDevnumber2) {
		this.gwDevnumber2 = gwDevnumber2;
	}
	/**
	 * 获取：二级网终端个数设备数
	 */
	public Integer getGwDevnumber2() {
		return gwDevnumber2;
	}
	/**
	 * 设置：计划内复位次数
	 */
	public void setPlanResetsCount(String planResetsCount) {
		this.planResetsCount = planResetsCount;
	}
	/**
	 * 获取：计划内复位次数
	 */
	public String getPlanResetsCount() {
		return planResetsCount;
	}
	/**
	 * 设置：数据写地址
	 */
	public void setDataWriteAddress(String dataWriteAddress) {
		this.dataWriteAddress = dataWriteAddress;
	}
	/**
	 * 获取：数据写地址
	 */
	public String getDataWriteAddress() {
		return dataWriteAddress;
	}
	/**
	 * 设置：数据读地址
	 */
	public void setDataReadAddress(String dataReadAddress) {
		this.dataReadAddress = dataReadAddress;
	}
	/**
	 * 获取：数据读地址
	 */
	public String getDataReadAddress() {
		return dataReadAddress;
	}
	/**
	 * 设置：数据写地址256
	 */
	public void setDataWriteAddress256(String dataWriteAddress256) {
		this.dataWriteAddress256 = dataWriteAddress256;
	}
	/**
	 * 获取：数据写地址256
	 */
	public String getDataWriteAddress256() {
		return dataWriteAddress256;
	}
	/**
	 * 设置：数据读地址256
	 */
	public void setDataReadAddress256(String dataReadAddress256) {
		this.dataReadAddress256 = dataReadAddress256;
	}
	/**
	 * 获取：数据读地址256
	 */
	public String getDataReadAddress256() {
		return dataReadAddress256;
	}
	/**
	 * 设置：网关模块版本号
	 */
	public void setGwModularVersionSn(String gwModularVersionSn) {
		this.gwModularVersionSn = gwModularVersionSn;
	}
	/**
	 * 获取：网关模块版本号
	 */
	public String getGwModularVersionSn() {
		return gwModularVersionSn;
	}
	/**
	 * 设置：
	 */
	public void setCrtTime(Date crtTime) {
		this.crtTime = crtTime;
	}
	/**
	 * 获取：
	 */
	public Date getCrtTime() {
		return crtTime;
	}
}
