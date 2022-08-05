package com.smart.iot.parking.entity;

import com.smart.iot.parking.feign.DictFeign;
import com.yuncitys.smart.merge.annonation.MergeField;
import com.yuncitys.smart.parking.common.audit.AceAudit;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 订单表
 *
 * @author Mr.AG
 * @email
 *@version 2022-08-07 17:30:13
 */
@Table(name = "parking_orders")
@AceAudit
public class ParkingOrders implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	@Id
	private String orderId;

	//开始时间
	@Column(name = "begin_date")
	private String beginDate;


	//结束时间
	@Column(name = "end_date")
	private String endDate;

	//收费规则类型
	@Column(name = "charge_rules_type_id")
	private String chargeRulesTypeId;

	//车位使用记录id
	@Column(name = "lm_id")
	private String lmId;

	//出入id
	@Column(name = "lp_id")
	private String lpId;

/*
	@MergeField(key = "order_status", feign = DictFeign.class, method = "getDictValues")
*/
	@Column(name = "order_status")
	private String orderStatus;

	@MergeField(key = "order_type", feign = DictFeign.class, method = "getDictValues")
	@Column(name = "order_type")
	private String orderType;

	//业主id（当订单是业主订单的时候）
	@Column(name = "private_user_id")
	private String privateUserId;

	//流程id
	@Column(name = "parking_bus_type")
	private String parkingBusType;

	//停车场id
	@Column(name = "parking_id")
	private String parkingId;


	//停车时长
	@Column(name = "parking_long_time")
	private String parkingLongTime;

	//车牌id
	@Column(name = "pla_id")
	private String plaId;

	@Column(name = "reverse_date")
	private String reverseDate;


	//订单实际消费金额
	@Column(name = "real_money")
	private BigDecimal realMoney;

	//订单编号
	@Column(name = "order_num")
	private String orderNum;

	//md5订单编号
	@Column(name = "order_num_md5")
	private String orderNumMd5;

	//车位id
	@Column(name = "space_id")
	private String spaceId;

	//用户交易信息编号
	@Column(name = "ud_id")
	private String udId;
	//用户id
	//关联人员编号
/*
	@MergeField(feign = UserFeign.class,method = "getByUserIds",isValueNeedMerge = true)
*/
	@Column(name = "user_id")
	private String userId;

	//收费人id
	@Column(name = "charge_id")
	private String chargeId;

	//月卡价格
	@Column(name = "month_card_price")
	private BigDecimal monthCardPrice;
	//月卡数量
	@Column(name = "month_count")
	private int monthCount;


	//收费时间
	@Column(name = "charge_date")
	private String chargeDate;

	//车牌图片base64
	@Column(name = "platenum_image")
	private String platenumImage;

	@MergeField(key = "pay_status", feign = DictFeign.class, method = "getDictValues")
	@Column(name = "pay_status")
	private String payStatus;

	@MergeField(key = "pay_type", feign = DictFeign.class, method = "getDictValues")
	@Column(name = "pay_type")
	private String payType;

	@MergeField(key = "position", feign = DictFeign.class, method = "getDictValues")
	@Column(name = "position")
	private String position;

	@MergeField(key = "create_order_type", feign = DictFeign.class, method = "getDictValues")
	@Column(name = "create_order_type")
	private String createOrderType;

	@Column(name = "parent_id")
	private String parentId;

	@MergeField(key = "error_type", feign = DictFeign.class, method = "getDictValues")
	@Column(name = "error_type")
	private String errorType;


	//有效标志(y:有效；n：无效)
	@MergeField(key = "enabled_flag", feign = DictFeign.class, method = "getDictValues")
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
	 * 月卡价格：
	 */
	public void setMonthCardPrice(BigDecimal monthCardPrice) {
		this.monthCardPrice = monthCardPrice;
	}
	/**
	 * 月卡价格：
	 */
	public BigDecimal getMonthCardPrice() {
		return monthCardPrice;
	}
	/**
	 * 月卡数量：
	 */
	public void setMonthCount(int monthCount) {
		this.monthCount = monthCount;
	}
	/**
	 * 月卡数量：
	 */
	public int getMonthCount() {
		return monthCount;
	}

	/**
	 * 设置：
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	/**
	 * 获取：
	 */
	public String getOrderId() {
		return orderId;
	}
	/**
	 * 设置：开始时间
	 */
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	/**
	 * 获取：开始时间
	 */
	public String getBeginDate() {
		return beginDate;
	}
	/**
	 * 设置：开始时间
	 */
	public void setReverseDate(String reverseDate) {
		this.reverseDate = reverseDate;
	}
	/**
	 * 获取：开始时间
	 */
	public String getReverseDate() {
		return reverseDate;
	}
	/**
	 * 设置：结束时间
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	/**
	 * 获取：结束时间
	 */
	public String getEndDate() {
		return endDate;
	}
	/**
	 * 设置：收费规则类型
	 */
	public void setChargeRulesTypeId(String chargeRulesTypeId) {
		this.chargeRulesTypeId = chargeRulesTypeId;
	}
	/**
	 * 获取：收费规则类型
	 */
	public String getChargeRulesTypeId() {
		return chargeRulesTypeId;
	}
	/**
	 * 设置：车位使用记录id
	 */
	public void setLmId(String lmId) {
		this.lmId = lmId;
	}
	/**
	 * 获取：车位使用记录id
	 */
	public String getLmId() {
		return lmId;
	}
	/**
	 * 设置：出入id
	 */
	public void setLpId(String lpId) {
		this.lpId = lpId;
	}
	/**
	 * 获取：出入id
	 */
	public String getLpId() {
		return lpId;
	}
	/**
	 * 设置：0已完成，1进行中，2异常结束，3已取消，4待支付
	 */
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	/**
	 * 获取：0已完成，1进行中，2异常结束，3已取消，4待支付
	 */
	public String getOrderStatus() {
		return orderStatus;
	}
	/**
	 * 设置：1普通入场订单 2vip订单 3视频桩 4充值订单 , 5退款订单6购买月卡 7月卡入场订单  8手持机订单9vip充值订单
	 */
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	/**
	 * 获取：1普通入场订单 2vip订单 3视频桩 4充值订单 , 5退款订单6购买月卡 7月卡入场订单  8手持机订单9vip充值订单
	 */
	public String getOrderType() {
		return orderType;
	}
	/**
	 * 设置：业主id（当订单是业主订单的时候）
	 */
	public void setPrivateUserId(String privateUserId) {
		this.privateUserId = privateUserId;
	}
	/**
	 * 获取：业主id（当订单是业主订单的时候）
	 */
	public String getPrivateUserId() {
		return privateUserId;
	}
	/**
	 * 设置：流程id
	 */
	public void setParkingBusType(String parkingBusType) {
		this.parkingBusType = parkingBusType;
	}
	/**
	 * 获取：流程id
	 */
	public String getParkingBusType() {
		return parkingBusType;
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
	 * 设置：停车时长
	 */
	public void setParkingLongTime(String parkingLongTime) {
		this.parkingLongTime = parkingLongTime;
	}
	/**
	 * 获取：停车时长
	 */
	public String getParkingLongTime() {
		return parkingLongTime;
	}
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
	 * 设置：订单实际消费金额
	 */
	public void setRealMoney(BigDecimal realMoney) {
		this.realMoney = realMoney;
	}
	/**
	 * 获取：订单实际消费金额
	 */
	public BigDecimal getRealMoney() {
		return realMoney;
	}
	/**
	 * 设置：订单编号
	 */
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	/**
	 * 获取：订单编号
	 */
	public String getOrderNum() {
		return orderNum;
	}
	/**
	 * 设置：md5订单编号
	 */
	public void setOrderNumMd5(String orderNumMd5) {
		this.orderNumMd5 = orderNumMd5;
	}
	/**
	 * 获取：md5订单编号
	 */
	public String getOrderNumMd5() {
		return orderNumMd5;
	}
	/**
	 * 设置：车位id
	 */
	public void setSpaceId(String spaceId) {
		this.spaceId = spaceId;
	}
	/**
	 * 获取：车位id
	 */
	public String getSpaceId() {
		return spaceId;
	}
	/**
	 * 设置：用户交易信息编号
	 */
	public void setUdId(String udId) {
		this.udId = udId;
	}
	/**
	 * 获取：用户交易信息编号
	 */
	public String getUdId() {
		return udId;
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
	 * 设置：收费人id
	 */
	public void setChargeId(String chargeId) {
		this.chargeId = chargeId;
	}
	/**
	 * 获取：收费人id
	 */
	public String getChargeId() {
		return chargeId;
	}
	/**
	 * 设置：收费时间
	 */
	public void setChargeDate(String chargeDate) {
		this.chargeDate = chargeDate;
	}
	/**
	 * 获取：收费时间
	 */
	public String getChargeDate() {
		return chargeDate;
	}
	/**
	 * 设置：车牌图片base64
	 */
	public void setPlatenumImage(String platenumImage) {
		this.platenumImage = platenumImage;
	}
	/**
	 * 获取：车牌图片base64
	 */
	public String getPlatenumImage() {
		return platenumImage;
	}
	/**
	 * 设置：支付宝或微信回调状态，0=未成功，1=成功
	 */
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	/**
	 * 获取：支付宝或微信回调状态，0=未成功，1=成功
	 */
	public String getPayStatus() {
		return payStatus;
	}
	/**
	 * 设置：(0现金支付1余额支付,2支付宝3微信)
	 */
	public void setPayType(String payType) {
		this.payType = payType;
	}
	/**
	 * 获取：(0现金支付1余额支付,2支付宝3微信)
	 */
	public String getPayType() {
		return payType;
	}
	/**
	 * 设置：0=在场外  1=在场内
	 */
	public void setPosition(String position) {
		this.position = position;
	}
	/**
	 * 获取：0=在场外  1=在场内
	 */
	public String getPosition() {
		return position;
	}
	/**
	 * 设置：创建订单方式（1人工，2自动）
	 */
	public void setCreateOrderType(String createOrderType) {
		this.createOrderType = createOrderType;
	}
	/**
	 * 获取：创建订单方式（1人工，2自动）
	 */
	public String getCreateOrderType() {
		return createOrderType;
	}
	/**
	 * 设置：0正常1流水号异常2通讯异常3地磁上报异常4手持机上报异常
	 */
	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}
	/**
	 * 获取：0正常1流水号异常2通讯异常3地磁上报异常4手持机上报异常
	 */
	public String getErrorType() {
		return errorType;
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

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
}
