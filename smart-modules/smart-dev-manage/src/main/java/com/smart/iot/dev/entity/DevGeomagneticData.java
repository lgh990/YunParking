package com.smart.iot.dev.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 *
 *
 * @author yuncitys
 * @email yuncitys@smart.iot.com
 * @version 2022-07-21 13:56:23
 */
@ApiModel(value="")
@Table(name = "dev_geomagnetic_data")
public class DevGeomagneticData implements Serializable {
	private static final long serialVersionUID = 1L;

	    //
    @Id
    @ApiModelProperty(value="id")
    private String id;

	    //设备sn
    @ApiModelProperty(value="设备sn")
    @Column(name = "onerankdev_sn")
    private String onerankdevSn;

	    //平台id
    @ApiModelProperty(value="平台id")
    @Column(name = "dev_id")
    private String devId;

	    //包计数
    @ApiModelProperty(value="包计数")
    @Column(name = "upackage_count")
    private String upackageCount;

	    //数据长度
    @ApiModelProperty(value="数据长度")
    @Column(name = "date_length")
    private String dateLength;

	    //电池电压
    @ApiModelProperty(value="电池电压")
    @Column(name = "battery_voltage")
    private String batteryVoltage;

	    //亮度值
    @ApiModelProperty(value="亮度值")
    @Column(name = "brightness_value")
    private String brightnessValue;

	    //温度（单位℃）湿度
    @ApiModelProperty(value="温度（单位℃）湿度")
    @Column(name = "temperature_humidity")
    private String temperatureHumidity;

	    //雷达模块工作状态
    @ApiModelProperty(value="雷达模块工作状态")
    @Column(name = "radar_module_work_state")
    private String radarModuleWorkState;

	    //有车无车状态流水
    @ApiModelProperty(value="有车无车状态流水")
    @Column(name = "lot_type")
    private String lotType;

	    //信号强度
    @ApiModelProperty(value="信号强度")
    @Column(name = "dda_signal_strength")
    private String ddaSignalStrength;

	    //太阳能输入电压（单位100mV）
    @ApiModelProperty(value="太阳能输入电压（单位100mV）")
    @Column(name = "solar_input_voltage")
    private String solarInputVoltage;

	    //太阳能输出电压（单位100mV）
    @ApiModelProperty(value="太阳能输出电压（单位100mV）")
    @Column(name = "solar_output_voltage")
    private String solarOutputVoltage;

	    //设备类型
    @ApiModelProperty(value="设备类型")
    @Column(name = "dev_type")
    private String devType;

	    //创建时间
    @ApiModelProperty(value="创建时间")
    @Column(name = "crt_time")
    private Date crtTime;


	/**
	 * 设置：
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public String getId() {
		return id;
	}
	/**
	 * 设置：设备sn
	 */
	public void setOnerankdevSn(String onerankdevSn) {
		this.onerankdevSn = onerankdevSn;
	}
	/**
	 * 获取：设备sn
	 */
	public String getOnerankdevSn() {
		return onerankdevSn;
	}
	/**
	 * 设置：平台id
	 */
	public void setDevId(String devId) {
		this.devId = devId;
	}
	/**
	 * 获取：平台id
	 */
	public String getDevId() {
		return devId;
	}
	/**
	 * 设置：包计数
	 */
	public void setUpackageCount(String upackageCount) {
		this.upackageCount = upackageCount;
	}
	/**
	 * 获取：包计数
	 */
	public String getUpackageCount() {
		return upackageCount;
	}
	/**
	 * 设置：数据长度
	 */
	public void setDateLength(String dateLength) {
		this.dateLength = dateLength;
	}
	/**
	 * 获取：数据长度
	 */
	public String getDateLength() {
		return dateLength;
	}
	/**
	 * 设置：电池电压
	 */
	public void setBatteryVoltage(String batteryVoltage) {
		this.batteryVoltage = batteryVoltage;
	}
	/**
	 * 获取：电池电压
	 */
	public String getBatteryVoltage() {
		return batteryVoltage;
	}
	/**
	 * 设置：亮度值
	 */
	public void setBrightnessValue(String brightnessValue) {
		this.brightnessValue = brightnessValue;
	}
	/**
	 * 获取：亮度值
	 */
	public String getBrightnessValue() {
		return brightnessValue;
	}
	/**
	 * 设置：温度（单位℃）湿度
	 */
	public void setTemperatureHumidity(String temperatureHumidity) {
		this.temperatureHumidity = temperatureHumidity;
	}
	/**
	 * 获取：温度（单位℃）湿度
	 */
	public String getTemperatureHumidity() {
		return temperatureHumidity;
	}
	/**
	 * 设置：雷达模块工作状态
	 */
	public void setRadarModuleWorkState(String radarModuleWorkState) {
		this.radarModuleWorkState = radarModuleWorkState;
	}
	/**
	 * 获取：雷达模块工作状态
	 */
	public String getRadarModuleWorkState() {
		return radarModuleWorkState;
	}
	/**
	 * 设置：有车无车状态流水
	 */
	public void setLotType(String lotType) {
		this.lotType = lotType;
	}
	/**
	 * 获取：有车无车状态流水
	 */
	public String getLotType() {
		return lotType;
	}
	/**
	 * 设置：信号强度
	 */
	public void setDdaSignalStrength(String ddaSignalStrength) {
		this.ddaSignalStrength = ddaSignalStrength;
	}
	/**
	 * 获取：信号强度
	 */
	public String getDdaSignalStrength() {
		return ddaSignalStrength;
	}
	/**
	 * 设置：太阳能输入电压（单位100mV）
	 */
	public void setSolarInputVoltage(String solarInputVoltage) {
		this.solarInputVoltage = solarInputVoltage;
	}
	/**
	 * 获取：太阳能输入电压（单位100mV）
	 */
	public String getSolarInputVoltage() {
		return solarInputVoltage;
	}
	/**
	 * 设置：太阳能输出电压（单位100mV）
	 */
	public void setSolarOutputVoltage(String solarOutputVoltage) {
		this.solarOutputVoltage = solarOutputVoltage;
	}
	/**
	 * 获取：太阳能输出电压（单位100mV）
	 */
	public String getSolarOutputVoltage() {
		return solarOutputVoltage;
	}
	/**
	 * 设置：设备类型
	 */
	public void setDevType(String devType) {
		this.devType = devType;
	}
	/**
	 * 获取：设备类型
	 */
	public String getDevType() {
		return devType;
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
}
