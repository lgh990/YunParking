package com.yuncitys.smart.parking.gate.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;


/**
 *
 *
 * @author Mr.AG
 * @email
 *@version 2022-01-18 11:11:06
 */
@Table(name = "prod")
public class Prod implements Serializable {
	private static final long serialVersionUID = 1L;

	    //
    @Id
    private Integer id;

	    //
    @Column(name = "num")
    private Integer num;


	/**
	 * 设置：
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：
	 */
	public void setNum(Integer num) {
		this.num = num;
	}
	/**
	 * 获取：
	 */
	public Integer getNum() {
		return num;
	}
}
