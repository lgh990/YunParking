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
@Table(name = "robot_data")
public class RobotData implements Serializable {
	private static final long serialVersionUID = 1L;

	    //
    @Id
    @ApiModelProperty(value="id")
    private String id;

	    //机器人id
    @ApiModelProperty(value="机器人id")
    @Column(name = "robot_id")
    private String robotId;

	    //人脸特征值
    @ApiModelProperty(value="人脸特征值")
    @Column(name = "face_value")
    private String faceValue;

	    //拍照时间
    @ApiModelProperty(value="拍照时间")
    @Column(name = "enterTime")
    private String entertime;

	    //车牌号
    @ApiModelProperty(value="车牌号")
    @Column(name = "carNum")
    private String carnum;

	    //抓拍图片地址
    @ApiModelProperty(value="抓拍图片地址")
    @Column(name = "imageUrl")
    private String imageurl;

	    //
    @ApiModelProperty(value="")
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
	 * 设置：机器人id
	 */
	public void setRobotId(String robotId) {
		this.robotId = robotId;
	}
	/**
	 * 获取：机器人id
	 */
	public String getRobotId() {
		return robotId;
	}
	/**
	 * 设置：人脸特征值
	 */
	public void setFaceValue(String faceValue) {
		this.faceValue = faceValue;
	}
	/**
	 * 获取：人脸特征值
	 */
	public String getFaceValue() {
		return faceValue;
	}
	/**
	 * 设置：拍照时间
	 */
	public void setEntertime(String entertime) {
		this.entertime = entertime;
	}
	/**
	 * 获取：拍照时间
	 */
	public String getEntertime() {
		return entertime;
	}
	/**
	 * 设置：车牌号
	 */
	public void setCarnum(String carnum) {
		this.carnum = carnum;
	}
	/**
	 * 获取：车牌号
	 */
	public String getCarnum() {
		return carnum;
	}
	/**
	 * 设置：抓拍图片地址
	 */
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	/**
	 * 获取：抓拍图片地址
	 */
	public String getImageurl() {
		return imageurl;
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
