package com.smart.iot.parking.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


/**
 * 自定义部件属性
 *
 * @author Mr.AG
 * @email
 *@version 2022-08-07 17:30:11
 */
@Table(name = "parking_component_data")
public class ParkingComponentData implements Serializable {
	private static final long serialVersionUID = 1L;

	    //自定义部件id
    @Id
	private String componentDataId;

	    //朝向：090180270=上右下左
    @Column(name = "angle")
    private Integer angle;

	    //所属区层
    @Column(name = "parking_area_id")
    private String parkingAreaId;

	    //自定义部件
    @Column(name = "parking_component_id")
    private String parkingComponentId;

	    //停车场
    @Column(name = "parking_id")
    private String parkingId;

	    //横向坐标
    @Column(name = "x")
    private Integer x;

	    //纵向坐标
    @Column(name = "y")
    private Integer y;

	    //缩放比例：100=1:1
    @Column(name = "zoom")
    private Integer zoom;

	    //有效标志(y:有效；n：无效)
    @Column(name = "enabled_flag")
    private String enabledFlag;

	    //创建时间
    @Column(name = "crt_time")
    private Date crtTime;

	    //创建人id
    @Column(name = "crt_user")
    private String crtUser;

	    //创建人
    @Column(name = "crt_name")
    private String crtName;

	    //创建主机
    @Column(name = "crt_host")
    private String crtHost;

	    //最后更新时间
    @Column(name = "upd_time")
    private Date updTime;

	    //最后更新人id
    @Column(name = "upd_user")
    private String updUser;

	    //最后更新人
    @Column(name = "upd_name")
    private String updName;

	    //最后更新主机
    @Column(name = "upd_host")
    private String updHost;

	    //
    @Column(name = "attr1")
    private String attr1;

	    //
    @Column(name = "attr2")
    private String attr2;

	    //
    @Column(name = "attr3")
    private String attr3;

	    //
    @Column(name = "attr4")
    private String attr4;

	    //
    @Column(name = "attr5")
    private String attr5;

	    //
    @Column(name = "attr6")
    private String attr6;

	    //
    @Column(name = "attr7")
    private String attr7;

	    //
    @Column(name = "attr8")
    private String attr8;


	/**
	 * 设置：自定义部件id
	 */
	public void setComponentDataId(String componentDataId) {
		this.componentDataId = componentDataId;
	}
	/**
	 * 获取：自定义部件id
	 */
	public String getComponentDataId() {
		return componentDataId;
	}
	/**
	 * 设置：朝向：090180270=上右下左
	 */
	public void setAngle(Integer angle) {
		this.angle = angle;
	}
	/**
	 * 获取：朝向：090180270=上右下左
	 */
	public Integer getAngle() {
		return angle;
	}
	/**
	 * 设置：所属区层
	 */
	public void setParkingAreaId(String parkingAreaId) {
		this.parkingAreaId = parkingAreaId;
	}
	/**
	 * 获取：所属区层
	 */
	public String getParkingAreaId() {
		return parkingAreaId;
	}
	/**
	 * 设置：自定义部件
	 */
	public void setParkingComponentId(String parkingComponentId) {
		this.parkingComponentId = parkingComponentId;
	}
	/**
	 * 获取：自定义部件
	 */
	public String getParkingComponentId() {
		return parkingComponentId;
	}
	/**
	 * 设置：停车场
	 */
	public void setParkingId(String parkingId) {
		this.parkingId = parkingId;
	}
	/**
	 * 获取：停车场
	 */
	public String getParkingId() {
		return parkingId;
	}
	/**
	 * 设置：横向坐标
	 */
	public void setX(Integer x) {
		this.x = x;
	}
	/**
	 * 获取：横向坐标
	 */
	public Integer getX() {
		return x;
	}
	/**
	 * 设置：纵向坐标
	 */
	public void setY(Integer y) {
		this.y = y;
	}
	/**
	 * 获取：纵向坐标
	 */
	public Integer getY() {
		return y;
	}
	/**
	 * 设置：缩放比例：100=1:1
	 */
	public void setZoom(Integer zoom) {
		this.zoom = zoom;
	}
	/**
	 * 获取：缩放比例：100=1:1
	 */
	public Integer getZoom() {
		return zoom;
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
