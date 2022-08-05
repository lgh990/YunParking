package com.smart.iot.parking.entity;

import javax.persistence.Basic;
import javax.persistence.Column;

/**
 * 类说明：
 */
public class GetPayMent
{
    private String sign;// : //
    // 微信
    private String appid;// wxe29e1215f4410b89
    private String noncestr;// 754851
    // private String package; //Sign=WXPay
    private String partnerid; // 1385001002
    private String prepayid; // wx2016100518104211d7d8ff3d0737652262,
    private String timestamp;// 1475662242

    // 支付宝
    private String input_charset;// 编码 UTF-8,
    private String body;// 商品信息详情;
    private String notify_url;// 回调路径:
                              // http;//://139.196.234.236/rs/admin/checkout/aliPayNotify,
    private String out_trade_no;// 订单号: 32016100575150991199,
    private String partner;// 商户pid : 2088421590095294,
    private String payment_type;// 支付类型 固定=1,
    private String seller_id;// 签约卖家支付宝账号 postmaster@hengchisuixing.com,
    private String service;// mobile.parkingpay.pay
    private String sign_type;// 加密类型 RSA,
    private String subject;// 商品名称
    private String total_fee;// 商品详情
    private String orderInfo;
    private String tradeNo;// 金额
    private Integer attribute1;
    private Integer attribute2;
    private String attribute3;
    private String attribute4;
    @Basic
    @Column(name = "ATTRIBUTE1")
    public Integer getAttribute1() {
        return attribute1;
    }

    public void setAttribute1(Integer attribute1) {
        this.attribute1 = attribute1;
    }

    @Basic
    @Column(name = "ATTRIBUTE2")
    public Integer getAttribute2() {
        return attribute2;
    }

    public void setAttribute2(Integer attribute2) {
        this.attribute2 = attribute2;
    }

    @Basic
    @Column(name = "ATTRIBUTE3")
    public String getAttribute3() {
        return attribute3;
    }

    public void setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
    }

    @Basic
    @Column(name = "ATTRIBUTE4")
    public String getAttribute4() {
        return attribute4;
    }

    public void setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
    }

    public String getTradeNo()
    {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo)
    {
        this.tradeNo = tradeNo;
    }

    public String getSign()
    {
        return sign;
    }

    public void setSign(String sign)
    {
        this.sign = sign;
    }

    public String getAppid()
    {
        return appid;
    }

    public void setAppid(String appid)
    {
        this.appid = appid;
    }

    public String getNoncestr()
    {
        return noncestr;
    }

    public void setNoncestr(String noncestr)
    {
        this.noncestr = noncestr;
    }

    public String getPartnerid()
    {
        return partnerid;
    }

    public void setPartnerid(String partnerid)
    {
        this.partnerid = partnerid;
    }

    public String getPrepayid()
    {
        return prepayid;
    }

    public void setPrepayid(String prepayid)
    {
        this.prepayid = prepayid;
    }

    public String getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getBody()
    {
        return body;
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    public String getNotify_url()
    {
        return notify_url;
    }

    public void setNotify_url(String notify_url)
    {
        this.notify_url = notify_url;
    }

    public String getOut_trade_no()
    {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no)
    {
        this.out_trade_no = out_trade_no;
    }

    public String getPartner()
    {
        return partner;
    }

    public void setPartner(String partner)
    {
        this.partner = partner;
    }

    public String getPayment_type()
    {
        return payment_type;
    }

    public void setPayment_type(String payment_type)
    {
        this.payment_type = payment_type;
    }

    public String getSeller_id()
    {
        return seller_id;
    }

    public void setSeller_id(String seller_id)
    {
        this.seller_id = seller_id;
    }

    public String getService()
    {
        return service;
    }

    public void setService(String service)
    {
        this.service = service;
    }

    public String getSign_type()
    {
        return sign_type;
    }

    public void setSign_type(String sign_type)
    {
        this.sign_type = sign_type;
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public String getTotal_fee()
    {
        return total_fee;
    }

    public void setTotal_fee(String total_fee)
    {
        this.total_fee = total_fee;
    }

    public String getInput_charset()
    {
        return input_charset;
    }

    public void setInput_charset(String input_charset)
    {
        this.input_charset = input_charset;
    }

    public String getOrderInfo()
    {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo)
    {
        this.orderInfo = orderInfo;
    }
}
