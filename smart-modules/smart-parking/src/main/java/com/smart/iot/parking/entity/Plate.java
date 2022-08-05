package com.smart.iot.parking.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 车牌表
 *
 * @author YUNCITYS
 * @email YUNCITYS@smart.iot.com
 * @version 2022-09-03 15:04:57
 */
@ApiModel(value="车牌表")
@Table(name = "plate")
public class Plate implements Serializable {
	private static final long serialVersionUID = 1L;

	    //车牌id
    @Id
    @ApiModelProperty(value="plaId")
    private String plaId;

	    //车牌号
    @ApiModelProperty(value="车牌号")
    @Column(name = "car_number")
    private String carNumber;

	    //发动机号
    @ApiModelProperty(value="发动机号")
    @Column(name = "enginNo")
    private String enginno;

	    //用户id
    @ApiModelProperty(value="用户id")
    @Column(name = "user_id")
    private String userId;

	    //车辆类型（01:大型汽车号牌；02:小型汽车号牌；03:使馆汽车号牌；04:领馆汽车号牌；05:境外汽车号牌；06:外籍汽车号牌；07:两、三轮摩托车号牌；08:轻便摩托车号牌；09:使馆摩托车号牌；10:领馆摩托车号牌；11:境外摩托车号牌；12:外籍摩托车号牌；13:农用运输车号牌；14:拖拉机号牌；15:挂车号牌；16:教练汽车号牌；17:教练摩托车号牌；18:试验汽车号牌；19:试验摩托车号牌；20:临时入境汽车号牌；21:临时入境摩托车号牌；22:临时行驶车号牌；23:警用汽车号牌；24:警用摩托号牌）
    @ApiModelProperty(value="车辆类型（01:大型汽车号牌；02:小型汽车号牌；03:使馆汽车号牌；04:领馆汽车号牌；05:境外汽车号牌；06:外籍汽车号牌；07:两、三轮摩托车号牌；08:轻便摩托车号牌；09:使馆摩托车号牌；10:领馆摩托车号牌；11:境外摩托车号牌；12:外籍摩托车号牌；13:农用运输车号牌；14:拖拉机号牌；15:挂车号牌；16:教练汽车号牌；17:教练摩托车号牌；18:试验汽车号牌；19:试验摩托车号牌；20:临时入境汽车号牌；21:临时入境摩托车号牌；22:临时行驶车号牌；23:警用汽车号牌；24:警用摩托号牌）")
    @Column(name = "car_type")
    private String carType;

	    //字典defualt_plate（是否默认车牌）
    @ApiModelProperty(value="字典defualt_plate（是否默认车牌）")
    @Column(name = "default_license_plate")
    private String defaultLicensePlate;

	    //有效标志(y:有效；n：无效)
    @ApiModelProperty(value="有效标志(y:有效；n：无效)")
    @Column(name = "enabled_flag")
    private String enabledFlag;

	    //车架号
    @ApiModelProperty(value="车架号")
    @Column(name = "frameno")
    private String frameno;

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


	/**
	 * 设置：车牌id
	 */
	public void setPlaId(String plaId) {
		this.plaId = plaId;
	}
	/**
	 * 获取：车牌id
	 */
	public String getPlaId() {
		return plaId;
	}
	/**
	 * 设置：车牌号
	 */
	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}
	/**
	 * 获取：车牌号
	 */
	public String getCarNumber() {
		return carNumber;
	}
	/**
	 * 设置：发动机号
	 */
	public void setEnginno(String enginno) {
		this.enginno = enginno;
	}
	/**
	 * 获取：发动机号
	 */
	public String getEnginno() {
		return enginno;
	}
	/**
	 * 设置：用户id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * 获取：用户id
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * 设置：车辆类型（01:大型汽车号牌；02:小型汽车号牌；03:使馆汽车号牌；04:领馆汽车号牌；05:境外汽车号牌；06:外籍汽车号牌；07:两、三轮摩托车号牌；08:轻便摩托车号牌；09:使馆摩托车号牌；10:领馆摩托车号牌；11:境外摩托车号牌；12:外籍摩托车号牌；13:农用运输车号牌；14:拖拉机号牌；15:挂车号牌；16:教练汽车号牌；17:教练摩托车号牌；18:试验汽车号牌；19:试验摩托车号牌；20:临时入境汽车号牌；21:临时入境摩托车号牌；22:临时行驶车号牌；23:警用汽车号牌；24:警用摩托号牌）
	 */
	public void setCarType(String carType) {
		this.carType = carType;
	}
	/**
	 * 获取：车辆类型（01:大型汽车号牌；02:小型汽车号牌；03:使馆汽车号牌；04:领馆汽车号牌；05:境外汽车号牌；06:外籍汽车号牌；07:两、三轮摩托车号牌；08:轻便摩托车号牌；09:使馆摩托车号牌；10:领馆摩托车号牌；11:境外摩托车号牌；12:外籍摩托车号牌；13:农用运输车号牌；14:拖拉机号牌；15:挂车号牌；16:教练汽车号牌；17:教练摩托车号牌；18:试验汽车号牌；19:试验摩托车号牌；20:临时入境汽车号牌；21:临时入境摩托车号牌；22:临时行驶车号牌；23:警用汽车号牌；24:警用摩托号牌）
	 */
	public String getCarType() {
		return carType;
	}
	/**
	 * 设置：字典defualt_plate（是否默认车牌）
	 */
	public void setDefaultLicensePlate(String defaultLicensePlate) {
		this.defaultLicensePlate = defaultLicensePlate;
	}
	/**
	 * 获取：字典defualt_plate（是否默认车牌）
	 */
	public String getDefaultLicensePlate() {
		return defaultLicensePlate;
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
	 * 设置：车架号
	 */
	public void setFrameno(String frameno) {
		this.frameno = frameno;
	}
	/**
	 * 获取：车架号
	 */
	public String getFrameno() {
		return frameno;
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
}
