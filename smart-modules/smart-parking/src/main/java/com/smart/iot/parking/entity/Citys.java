package com.smart.iot.parking.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


/**
 * 区县行政编码字典表
 *
 * @author Mr.AG
 * @email
 *@version 2022-08-07 17:30:12
 */
@Table(name = "citys")
public class Citys implements Serializable {
	private static final long serialVersionUID = 1L;

	    //
    @Id
    private String id;

	    //名称
    @Column(name = "name")
    private String name;

	    //层级标识： 1  省份， 2  市， 3  区县
    @Column(name = "arealevel")
    private Boolean arealevel;

	    //父节点
    @Column(name = "parent_id")
    private Integer parentId;

	//创建时间
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
	 * 设置：名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：层级标识： 1  省份， 2  市， 3  区县
	 */
	public void setArealevel(Boolean arealevel) {
		this.arealevel = arealevel;
	}
	/**
	 * 获取：层级标识： 1  省份， 2  市， 3  区县
	 */
	public Boolean getArealevel() {
		return arealevel;
	}
	/**
	 * 设置：父节点
	 */
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	/**
	 * 获取：父节点
	 */
	public Integer getParentId() {
		return parentId;
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
