package com.smart.iot.parking.vo;

import com.smart.iot.parking.biz.*;
import com.smart.iot.parking.entity.*;
import com.smart.iot.parking.feign.DictFeign;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yuncitys.smart.merge.annonation.MergeField;
import com.yuncitys.smart.parking.common.audit.AceAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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
 *@version 2022-08-07 17:30:13
 * @email
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "订单记录")
public class ParkingOrdersVo implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "订单编号")
    private String orderId;
    @ApiModelProperty(value = "订单开始时间")
    private String beginDate;
    @ApiModelProperty(value = "订单结束时间")
    private String endDate;
    @ApiModelProperty(value = "收费规则类型")
    private String chargeRulesTypeId;
    @ApiModelProperty(value = "车位使用记录id")
    private String lmId;
    @ApiModelProperty(value = "出入id")
    private String lpId;
    @ApiModelProperty(value = "订单状态（待支付=unpay，已完成=complete，进行中=running，异常=exception，退款=refund，取消=cancel）")
    private String orderStatus;
    @ApiModelProperty(value = "订单类型(充值VIP=vip_charge,充值=recharge,手持机订单=handset,普通停车=common,月卡停车=monthCard," +
            "购买月卡=pur_monthcard,视频桩订单=videopile,vip停车订单=vip,私人车位订单=shared_lot,免费订单=free_admission)")
    private String orderType;
    @ApiModelProperty(value="业主id（当订单是业主订单的时候")
    private String privateUserId;
    @ApiModelProperty(value="流程id")
    private String parkingBusType;
    @ApiModelProperty(value="停车场id")
    private String parkingId;
    @ApiModelProperty(value="停车时长")
    private String parkingLongTime;
    @ApiModelProperty(value="车牌id")
    private String plaId;
    @ApiModelProperty(value="预定时间")
    private String reverseDate;
    @ApiModelProperty(value="订单实际消费金额")
    private BigDecimal realMoney;
    @ApiModelProperty(value="订单编号")
    private String orderNum;
    @ApiModelProperty(value="md5订单编号")
    private String orderNumMd5;
    @ApiModelProperty(value="车位id")
    private String spaceId;
    @ApiModelProperty(value="用户交易信息编号")
    private String udId;
    @ApiModelProperty(value="用户id")
    private String userId;
    @ApiModelProperty(value="收费人id")
    private String chargeId;
    @ApiModelProperty(value="月卡价格")
    private BigDecimal monthCardPrice;
    @ApiModelProperty(value="月卡数量")
    private int monthCount;
    @ApiModelProperty(value="收费时间")
    private String chargeDate;
    @ApiModelProperty(value="车牌图片base64")
    private String platenumImage;
    @ApiModelProperty(value="支付状态(成功=success,失败=fail)")
    private String payStatus;
    @ApiModelProperty(value="支付类型(支付宝=alipay,微信=wechat,余额=balance,现金=cash.免费=free)")
    private String payType;
    @ApiModelProperty(value="进出场状态(已入场=admission，已出场=leave，未进场=unapproach)")
    private String position;
    @ApiModelProperty(value="订单创建方式（自动下单=machine，人工下单=artificial，免费自动下单=free_automatic，免费人工下单=free_labor）")
    private String createOrderType;
    @ApiModelProperty(value="父订单id")
    private String parentId;
    @ApiModelProperty(value="异常类型(通讯异常=communication,流水异常=flow,地磁上报异常=geomagnetic,地磁时间异常=geomagneticTime,手持机上报异常=handheld_machine,正常=normal)")
    private String errorType;
    @ApiModelProperty(value="有效标志(y:有效；n：无效)")
    private String enabledFlag;
    @ApiModelProperty(value="创建时间")
    private Date crtTime;
    @ApiModelProperty(value="创建人id")
    private String crtUser;
    @ApiModelProperty(value="创建人")
    private String crtName;
    @ApiModelProperty(value="创建主机")
    private String crtHost;
    @ApiModelProperty(value="最后更新时间")
    private Date updTime;
    @ApiModelProperty(value="最后更新人id")
    private String updUser;
    @ApiModelProperty(value="最后更新人")
    private String updName;
    @ApiModelProperty(value="最后更新主机")
    private String updHost;

    private String attr1;

    private String attr2;

    private String attr3;

    private String attr4;

    private String attr5;

    private String attr6;

    private String attr7;

    private String attr8;

    private IoRecord ioRecord;

    private ParkingBusinessType parkingBusTypeEntity;

    private Plate plate;

    private Parking parking;

    private ParkingSpace parkingSpace;

    public ParkingOrdersVo(ParkingOrders parkingOrders) {
        this.orderId = parkingOrders.getOrderId();
        this.beginDate = parkingOrders.getBeginDate();
        this.endDate = parkingOrders.getEndDate();
        this.chargeRulesTypeId = parkingOrders.getChargeRulesTypeId();
        this.lmId = parkingOrders.getLmId();
        this.lpId = parkingOrders.getLpId();
        this.orderStatus = parkingOrders.getOrderStatus();
        this.orderType = parkingOrders.getOrderType();
        this.privateUserId = parkingOrders.getPrivateUserId();
        this.parkingBusType = parkingOrders.getParkingBusType();
        this.parkingId = parkingOrders.getParkingId();
        this.parkingLongTime = parkingOrders.getParkingLongTime();
        this.plaId = parkingOrders.getPlaId();
        this.reverseDate = parkingOrders.getReverseDate();
        this.realMoney = parkingOrders.getRealMoney();
        this.orderNum = parkingOrders.getOrderNum();
        this.orderNumMd5 = parkingOrders.getOrderNumMd5();
        this.spaceId = parkingOrders.getSpaceId();
        this.udId = parkingOrders.getUdId();
        this.userId = parkingOrders.getUserId();
        this.chargeId = parkingOrders.getChargeId();
        this.monthCardPrice = parkingOrders.getMonthCardPrice();
        this.monthCount = parkingOrders.getMonthCount();
        this.chargeDate = parkingOrders.getChargeDate();
        this.platenumImage = parkingOrders.getPlatenumImage();
        this.payStatus = parkingOrders.getPayStatus();
        this.payType = parkingOrders.getPayType();
        this.position = parkingOrders.getPosition();
        this.createOrderType = parkingOrders.getCreateOrderType();
        this.parentId = parkingOrders.getParentId();
        this.errorType = parkingOrders.getErrorType();
        this.enabledFlag = parkingOrders.getEnabledFlag();
        this.crtTime = parkingOrders.getCrtTime();
        this.crtUser = parkingOrders.getCrtUser();
        this.crtName = parkingOrders.getCrtName();
        this.crtHost = parkingOrders.getCrtHost();
        this.updTime = parkingOrders.getUpdTime();
        this.updUser = parkingOrders.getUpdUser();
        this.updName = parkingOrders.getUpdName();
        this.updHost = parkingOrders.getUpdHost();
        this.attr1 = parkingOrders.getAttr1();
        this.attr2 = parkingOrders.getAttr2();
        this.attr3 = parkingOrders.getAttr3();
        this.attr4 = parkingOrders.getAttr4();
        this.attr5 = parkingOrders.getAttr5();
        this.attr6 = parkingOrders.getAttr6();
        this.attr7 = parkingOrders.getAttr7();
        this.attr8 = parkingOrders.getAttr8();
    }


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

    public IoRecord getIoRecord() {
        return ioRecord;
    }

    public void setIoRecord(IoRecord ioRecord) {
        this.ioRecord = ioRecord;
    }

    public ParkingBusinessType getParkingBusTypeEntity() {
        return parkingBusTypeEntity;
    }

    public void setParkingBusTypeEntity(ParkingBusinessType parkingBusTypeEntity) {
        this.parkingBusTypeEntity = parkingBusTypeEntity;
    }

    public Plate getPlate() {
        return plate;
    }

    public void setPlate(Plate plate) {
        this.plate = plate;
    }

    public Parking getParking() {
        return parking;
    }

    public void setParking(Parking parking) {
        this.parking = parking;
    }

    public ParkingSpace getParkingSpace() {
        return parkingSpace;
    }

    public void setParkingSpace(ParkingSpace parkingSpace) {
        this.parkingSpace = parkingSpace;
    }
}
