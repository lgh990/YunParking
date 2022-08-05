package com.smart.iot.parking.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


/**
 * MQTT日志表
 *
 * @author Mr.AG
 * @email
 *@version 2022-08-23 11:36:21
 */
@Table(name = "mqtt_msg")
public class MqttMsg implements Serializable {
	private static final long serialVersionUID = 1L;

	    //MQTT通信信息Id
	@Id
    private Integer mmId;

	    //有效标志(Y:有效；N：无效)
    @Column(name = "ENABLED_FLAG")
    private String enabledFlag;

	    //创建人
    @Column(name = "CREATED_BY")
    private String createdBy;

	    //创建时间
    @Column(name = "CREATED_DATE")
    private Date createdDate;

	    //修改人
    @Column(name = "LAST_UPDATE_BY")
    private String lastUpdateBy;

	    //修改时间
    @Column(name = "LAST_UPDATE_DATE")
    private Date lastUpdateDate;

	    //所属系统
    @Column(name = "DATA_OWNED_SYS")
    private Integer dataOwnedSys;

	    //通信主题
    @Column(name = "TOPIC")
    private String topic;

	    //通信信息
    @Column(name = "MSG")
    private String msg;

	    //
    @Column(name = "ATTRIBUTE1")
    private Integer attribute1;

	    //
    @Column(name = "ATTRIBUTE2")
    private Integer attribute2;

	    //
    @Column(name = "ATTRIBUTE3")
    private String attribute3;

	    //
    @Column(name = "ATTRIBUTE4")
    private String attribute4;


	/**
	 * 设置：MQTT通信信息Id
	 */
	public void setMmId(Integer mmId) {
		this.mmId = mmId;
	}
	/**
	 * 获取：MQTT通信信息Id
	 */
	public Integer getMmId() {
		return mmId;
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
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	/**
	 * 获取：创建人
	 */
	public String getCreatedBy() {
		return createdBy;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreatedDate() {
		return createdDate;
	}
	/**
	 * 设置：修改人
	 */
	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}
	/**
	 * 获取：修改人
	 */
	public String getLastUpdateBy() {
		return lastUpdateBy;
	}
	/**
	 * 设置：修改时间
	 */
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getLastUpdateDate() {
		return lastUpdateDate;
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
	 * 设置：通信主题
	 */
	public void setTopic(String topic) {
		this.topic = topic;
	}
	/**
	 * 获取：通信主题
	 */
	public String getTopic() {
		return topic;
	}
	/**
	 * 设置：通信信息
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
	/**
	 * 获取：通信信息
	 */
	public String getMsg() {
		return msg;
	}
	/**
	 * 设置：
	 */
	public void setAttribute1(Integer attribute1) {
		this.attribute1 = attribute1;
	}
	/**
	 * 获取：
	 */
	public Integer getAttribute1() {
		return attribute1;
	}
	/**
	 * 设置：
	 */
	public void setAttribute2(Integer attribute2) {
		this.attribute2 = attribute2;
	}
	/**
	 * 获取：
	 */
	public Integer getAttribute2() {
		return attribute2;
	}
	/**
	 * 设置：
	 */
	public void setAttribute3(String attribute3) {
		this.attribute3 = attribute3;
	}
	/**
	 * 获取：
	 */
	public String getAttribute3() {
		return attribute3;
	}
	/**
	 * 设置：
	 */
	public void setAttribute4(String attribute4) {
		this.attribute4 = attribute4;
	}
	/**
	 * 获取：
	 */
	public String getAttribute4() {
		return attribute4;
	}
}
