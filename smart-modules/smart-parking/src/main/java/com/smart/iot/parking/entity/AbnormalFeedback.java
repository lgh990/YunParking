package com.smart.iot.parking.entity;

import com.smart.iot.parking.biz.AppUserBiz;
import com.yuncitys.smart.merge.annonation.MergeField;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


/**
 * 车位故障上报表
 *
 * @author heyaohuan
 * @email
 *@version 2022-09-07 11:10:32
 */
@Table(name = "abnormal_feedback")
public class AbnormalFeedback implements Serializable {
	private static final long serialVersionUID = 1L;

	    //
	@Id
    private String id;

	    //反馈信息
    @Column(name = "fb_msg")
    private String fbMsg;

	    //车位号
    @Column(name = "space_id")
    private String spaceId;

	    //车位头
    @Column(name = "image_head")
    private String imageHead;

	    //车位尾
    @Column(name = "image_tail")
    private String imageTail;

	    //车位侧身
    @Column(name = "image_sideways")
    private String imageSideways;

	    //订单号
    @Column(name = "order_num")
    private String orderNum;

	    //反馈类型 feedback_type（有车报无车，无车报有车）
    @Column(name = "fb_type")
    private String fbType;

    @Column(name = "user_id")
    private String userId;

	    //手持机人员id
    @Column(name = "hmid")
    private String hmid;

	    //字典 proce_status 是否已解决（未解决，已解决，取消）
    @Column(name = "proce_status")
    private String proceStatus;

	    //用户上报申述日期
    @Column(name = "fb_date")
    private String fbDate;

	    //备注
    @Column(name = "remarks")
    private String remarks;

	    //停车场id
    @Column(name = "parking_id")
    private String parkingId;

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

	    //租户id
    @Column(name = "tenant_id")
    private String tenantId;


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
	 * 设置：反馈信息
	 */
	public void setFbMsg(String fbMsg) {
		this.fbMsg = fbMsg;
	}
	/**
	 * 获取：反馈信息
	 */
	public String getFbMsg() {
		return fbMsg;
	}
	/**
	 * 设置：车位号
	 */
	public void setSpaceId(String spaceId) {
		this.spaceId = spaceId;
	}
	/**
	 * 获取：车位号
	 */
	public String getSpaceId() {
		return spaceId;
	}
	/**
	 * 设置：车位头
	 */
	public void setImageHead(String imageHead) {
		this.imageHead = imageHead;
	}
	/**
	 * 获取：车位头
	 */
	public String getImageHead() {
		return imageHead;
	}
	/**
	 * 设置：车位尾
	 */
	public void setImageTail(String imageTail) {
		this.imageTail = imageTail;
	}
	/**
	 * 获取：车位尾
	 */
	public String getImageTail() {
		return imageTail;
	}
	/**
	 * 设置：车位侧身
	 */
	public void setImageSideways(String imageSideways) {
		this.imageSideways = imageSideways;
	}
	/**
	 * 获取：车位侧身
	 */
	public String getImageSideways() {
		return imageSideways;
	}
	/**
	 * 设置：订单号
	 */
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	/**
	 * 获取：订单号
	 */
	public String getOrderNum() {
		return orderNum;
	}
	/**
	 * 设置：反馈类型 feedback_type（有车报无车，无车报有车）
	 */
	public void setFbType(String fbType) {
		this.fbType = fbType;
	}
	/**
	 * 获取：反馈类型 feedback_type（有车报无车，无车报有车）
	 */
	public String getFbType() {
		return fbType;
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
	 * 设置：手持机人员id
	 */
	public void setHmid(String hmid) {
		this.hmid = hmid;
	}
	/**
	 * 获取：手持机人员id
	 */
	public String getHmid() {
		return hmid;
	}
	/**
	 * 设置：字典 proce_status 是否已解决（未解决，已解决，取消）
	 */
	public void setProceStatus(String proceStatus) {
		this.proceStatus = proceStatus;
	}
	/**
	 * 获取：字典 proce_status 是否已解决（未解决，已解决，取消）
	 */
	public String getProceStatus() {
		return proceStatus;
	}
	/**
	 * 设置：用户上报申述日期
	 */
	public void setFbDate(String fbDate) {
		this.fbDate = fbDate;
	}
	/**
	 * 获取：用户上报申述日期
	 */
	public String getFbDate() {
		return fbDate;
	}
	/**
	 * 设置：备注
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	/**
	 * 获取：备注
	 */
	public String getRemarks() {
		return remarks;
	}
	/**
	 * 设置：停车场id
	 */
	public void setParkingId(String parkingId) {
		this.parkingId = parkingId;
	}
	/**
	 * 获取：停车场id
	 */
	public String getParkingId() {
		return parkingId;
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
	/**
	 * 设置：租户id
	 */
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	/**
	 * 获取：租户id
	 */
	public String getTenantId() {
		return tenantId;
	}
}
