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
@Table(name = "dev_early_warning_log")
public class DevEarlyWarningLog implements Serializable {
	private static final long serialVersionUID = 1L;

	    //
    @Id
    @ApiModelProperty(value="id")
    private String id;

	    //内容
    @ApiModelProperty(value="内容")
    @Column(name = "content")
    private String content;

	    //设备id
    @ApiModelProperty(value="设备id")
    @Column(name = "gw_sn")
    private String gwSn;

	    //创建时间
    @ApiModelProperty(value="创建时间")
    @Column(name = "crt_time")
    private Date crtTime;

	    //预警类型（0=高预警、1=低预警）
    @ApiModelProperty(value="预警类型（0=高预警、1=低预警）")
    @Column(name = "type")
    private Integer type;


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
	 * 设置：内容
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * 获取：内容
	 */
	public String getContent() {
		return content;
	}
	/**
	 * 设置：设备id
	 */
	public void setGwSn(String gwSn) {
		this.gwSn = gwSn;
	}
	/**
	 * 获取：设备id
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
	 * 设置：预警类型（0=高预警、1=低预警）
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：预警类型（0=高预警、1=低预警）
	 */
	public Integer getType() {
		return type;
	}
}
