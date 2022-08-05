package com.smart.iot.parking.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


/**
 *
 *
 * @author Mr.AG
 * @email 
 *@version 2022-09-04 14:08:17
 */
@Table(name = "dev_type")
public class DevType implements Serializable {
	private static final long serialVersionUID = 1L;

	    //
    @Id
    private String id;

	    //
    @Column(name = "enabled_flag")
    private String enabledFlag;

	    //
    @Column(name = "created_by")
    private String createdBy;

	    //
    @Column(name = "created_date")
    private Date createdDate;

	    //
    @Column(name = "last_update_by")
    private String lastUpdateBy;

	    //
    @Column(name = "last_update_date")
    private Date lastUpdateDate;

	    //
    @Column(name = "data_owned_sys")
    private Double dataOwnedSys;

	    //
    @Column(name = "parking_type")
    private Double parkingType;

	    //
    @Column(name = "type_name")
    private String typeName;

	    //
    @Column(name = "type_code")
    private String typeCode;

	    //
    @Column(name = "attribute1")
    private Double attribute1;

	    //
    @Column(name = "attribute2")
    private Double attribute2;

	    //
    @Column(name = "attribute3")
    private String attribute3;

	    //
    @Column(name = "attribute4")
    private String attribute4;


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
	 * 设置：
	 */
	public void setEnabledFlag(String enabledFlag) {
		this.enabledFlag = enabledFlag;
	}
	/**
	 * 获取：
	 */
	public String getEnabledFlag() {
		return enabledFlag;
	}
	/**
	 * 设置：
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	/**
	 * 获取：
	 */
	public String getCreatedBy() {
		return createdBy;
	}
	/**
	 * 设置：
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	/**
	 * 获取：
	 */
	public Date getCreatedDate() {
		return createdDate;
	}
	/**
	 * 设置：
	 */
	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}
	/**
	 * 获取：
	 */
	public String getLastUpdateBy() {
		return lastUpdateBy;
	}
	/**
	 * 设置：
	 */
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	/**
	 * 获取：
	 */
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	/**
	 * 设置：
	 */
	public void setDataOwnedSys(Double dataOwnedSys) {
		this.dataOwnedSys = dataOwnedSys;
	}
	/**
	 * 获取：
	 */
	public Double getDataOwnedSys() {
		return dataOwnedSys;
	}
	/**
	 * 设置：
	 */
	public void setParkingType(Double parkingType) {
		this.parkingType = parkingType;
	}
	/**
	 * 获取：
	 */
	public Double getParkingType() {
		return parkingType;
	}
	/**
	 * 设置：
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	/**
	 * 获取：
	 */
	public String getTypeName() {
		return typeName;
	}
	/**
	 * 设置：
	 */
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	/**
	 * 获取：
	 */
	public String getTypeCode() {
		return typeCode;
	}
	/**
	 * 设置：
	 */
	public void setAttribute1(Double attribute1) {
		this.attribute1 = attribute1;
	}
	/**
	 * 获取：
	 */
	public Double getAttribute1() {
		return attribute1;
	}
	/**
	 * 设置：
	 */
	public void setAttribute2(Double attribute2) {
		this.attribute2 = attribute2;
	}
	/**
	 * 获取：
	 */
	public Double getAttribute2() {
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
