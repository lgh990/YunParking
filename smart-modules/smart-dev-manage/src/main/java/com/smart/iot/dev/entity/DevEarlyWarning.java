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
 * @version 2022-07-26 14:48:10
 */
@ApiModel(value="")
@Table(name = "dev_early_warning")
public class DevEarlyWarning implements Serializable {
	private static final long serialVersionUID = 1L;

	    //
    @Id
    @ApiModelProperty(value="id")
    private String id;

	    //设备sn
    @ApiModelProperty(value="设备sn")
    @Column(name = "gw_sn")
    private String gwSn;

	    //创建时间
    @ApiModelProperty(value="创建时间")
    @Column(name = "crt_time")
    private Date crtTime;

	    //
    @ApiModelProperty(value="")
    @Column(name = "max")
    private Integer max;

	    //
    @ApiModelProperty(value="")
    @Column(name = "min")
    private Integer min;

	    //
    @ApiModelProperty(value="")
    @Column(name = "keyName")
    private String keyname;

	    //
    @ApiModelProperty(value="")
    @Column(name = "key_s")
    private String key;


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
	public void setGwSn(String gwSn) {
		this.gwSn = gwSn;
	}
	/**
	 * 获取：设备sn
	 */
	public String getGwSn() {
		return gwSn;
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
	 * 设置：
	 */
	public void setMax(Integer max) {
		this.max = max;
	}
	/**
	 * 获取：
	 */
	public Integer getMax() {
		return max;
	}
	/**
	 * 设置：
	 */
	public void setMin(Integer min) {
		this.min = min;
	}
	/**
	 * 获取：
	 */
	public Integer getMin() {
		return min;
	}
	/**
	 * 设置：
	 */
	public void setKeyname(String keyname) {
		this.keyname = keyname;
	}
	/**
	 * 获取：
	 */
	public String getKeyname() {
		return keyname;
	}
	/**
	 * 设置：
	 */
	public void setKey(String key) {
		this.key = key;
	}
	/**
	 * 获取：
	 */
	public String getKey() {
		return key;
	}
}
