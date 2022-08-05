package com.smart.iot.parking.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 *
 *
 * @author YUNCITYS
 * @email YUNCITYS@smart.iot.com
 * @version 2022-10-22 10:51:18
 */
@ApiModel(value="")
@Table(name = "robot")
public class Robot implements Serializable {
	private static final long serialVersionUID = 1L;

	    //
    @Id
    @ApiModelProperty(value="robotId")
    private String robotId;

	    //车道标识
    @ApiModelProperty(value="车道标识")
    @Column(name = "channel")
    private String channel;

	    //车场名称
    @ApiModelProperty(value="车场名称")
    @Column(name = "park_name")
    private String parkName;

	    //机器人设备 IP
    @ApiModelProperty(value="机器人设备 IP")
    @Column(name = "address")
    private String address;

	    //车道出入口标识
    @ApiModelProperty(value="车道出入口标识")
    @Column(name = "in_orout")
    private String inOrout;

	    //机器人对应的相机 IP
    @ApiModelProperty(value="机器人对应的相机 IP ")
    @Column(name = "equip_ip")
    private String equipIp;

	    //
    @ApiModelProperty(value="")
    @Column(name = "crt_time")
    private Date crtTime;


	/**
	 * 设置：
	 */
	public void setRobotId(String robotId) {
		this.robotId = robotId;
	}
	/**
	 * 获取：
	 */
	public String getRobotId() {
		return robotId;
	}
	/**
	 * 设置：车道标识
	 */
	public void setChannel(String channel) {
		this.channel = channel;
	}
	/**
	 * 获取：车道标识
	 */
	public String getChannel() {
		return channel;
	}
	/**
	 * 设置：车场名称
	 */
	public void setParkName(String parkName) {
		this.parkName = parkName;
	}
	/**
	 * 获取：车场名称
	 */
	public String getParkName() {
		return parkName;
	}
	/**
	 * 设置：机器人设备 IP
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * 获取：机器人设备 IP
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * 设置：车道出入口标识
	 */
	public void setInOrout(String inOrout) {
		this.inOrout = inOrout;
	}
	/**
	 * 获取：车道出入口标识
	 */
	public String getInOrout() {
		return inOrout;
	}
	/**
	 * 设置：机器人对应的相机 IP
	 */
	public void setEquipIp(String equipIp) {
		this.equipIp = equipIp;
	}
	/**
	 * 获取：机器人对应的相机 IP
	 */
	public String getEquipIp() {
		return equipIp;
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
