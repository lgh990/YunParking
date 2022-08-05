package com.smart.iot.parking.vo;

import com.smart.iot.parking.biz.ParkingBiz;
import com.smart.iot.parking.biz.ParkingIoBiz;
import com.smart.iot.parking.biz.PlateBiz;
import com.smart.iot.parking.entity.IoRecord;
import com.smart.iot.parking.entity.Parking;
import com.smart.iot.parking.entity.ParkingIo;
import com.smart.iot.parking.entity.Plate;
import com.yuncitys.smart.merge.annonation.MergeField;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


/**
 * 进出记录表
 *
 * @author Mr.AG
 * @email
 *@version 2022-08-16 17:35:26
 */
public class IoRecordVo implements Serializable {
	private static final long serialVersionUID = 1L;

	    //
    private String lrId;

	    //进入时间
    private String accDate;

    private String accinId;

	    //进入照片
    private String accPhoto;

	    //出去时间
    private String gououtDate;

	    //出口id
    private String exitId;

	    //出去图片
    private String gououtPhoto;

	    //停车场id
    private String parkingId;

	    //车牌id
    private String plateId;

	    //有效标志(y:有效；n：无效)
    private String enabledFlag;

	    //创建时间
    private Date crtTime;

	    //创建人id
    private String crtUser;

	    //创建人
    private String crtName;

	    //创建主机
    private String crtHost;

	    //最后更新时间
    private Date updTime;

	    //最后更新人id
    private String updUser;

	    //最后更新人
    private String updName;

	    //最后更新主机
    private String updHost;

	    //
    private String attr1;

	    //
    private String attr2;

	    //
    private String attr3;

	    //
    private String attr4;

	    //
    private String attr5;

	    //
    private String attr6;

	    //
    private String attr7;

	    //
    private String attr8;

	    //租户id
    private String tenantId;

	    //
    private String crtUserId;

    private ParkingIo accinParkingIo;

    private ParkingIo exitParkingIo;

	private Parking parking;

	public IoRecordVo(IoRecord ioRecord) {
		this.lrId = ioRecord.getLrId();
		this.accDate = ioRecord.getAccDate();
		this.accinId = ioRecord.getAccinId();
		this.accPhoto = ioRecord.getAccPhoto();
		this.gououtDate = ioRecord.getGououtDate();
		this.exitId = ioRecord.getExitId();
		this.gououtPhoto = ioRecord.getGououtPhoto();
		this.parkingId = ioRecord.getParkingId();
		this.plateId = ioRecord.getPlateId();
		this.enabledFlag = ioRecord.getEnabledFlag();
		this.crtTime = ioRecord.getCrtTime();
		this.crtUser = ioRecord.getCrtUser();
		this.crtName = ioRecord.getCrtName();
		this.crtHost = ioRecord.getCrtHost();
		this.updTime = ioRecord.getUpdTime();
		this.updUser = ioRecord.getUpdUser();
		this.updName = ioRecord.getUpdName();
		this.updHost = ioRecord.getUpdHost();
		this.attr1 = ioRecord.getAttr1();
		this.attr2 = ioRecord.getAttr2();
		this.attr3 = ioRecord.getAttr3();
		this.attr4 = ioRecord.getAttr4();
		this.attr5 = ioRecord.getAttr5();
		this.attr6 = ioRecord.getAttr6();
		this.attr7 = ioRecord.getAttr7();
		this.attr8 = ioRecord.getAttr8();
		this.tenantId = ioRecord.getTenantId();
		this.crtUserId = ioRecord.getCrtUserId();
	}

	public ParkingIo getAccinParkingIo() {
		return accinParkingIo;
	}

	public void setAccinParkingIo(ParkingIo accinParkingIo) {
		this.accinParkingIo = accinParkingIo;
	}

	public ParkingIo getExitParkingIo() {
		return exitParkingIo;
	}

	public void setExitParkingIo(ParkingIo exitParkingIo) {
		this.exitParkingIo = exitParkingIo;
	}

	public Parking getParking() {
		return parking;
	}

	public void setParking(Parking parking) {
		this.parking = parking;
	}

	public Plate getPlate() {
		return plate;
	}

	public void setPlate(Plate plate) {
		this.plate = plate;
	}

	private Plate plate;
	/**
	 * 设置：
	 */
	public void setLrId(String lrId) {
		this.lrId = lrId;
	}
	/**
	 * 获取：
	 */
	public String getLrId() {
		return lrId;
	}
	/**
	 * 设置：进入时间
	 */
	public void setAccDate(String accDate) {
		this.accDate = accDate;
	}
	/**
	 * 获取：进入时间
	 */
	public String getAccDate() {
		return accDate;
	}
	/**
	 * 设置：入口id
	 */
	public void setAccinId(String accinId) {
		this.accinId = accinId;
	}
	/**
	 * 获取：入口id
	 */
	public String getAccinId() {
		return accinId;
	}
	/**
	 * 设置：进入照片
	 */
	public void setAccPhoto(String accPhoto) {
		this.accPhoto = accPhoto;
	}
	/**
	 * 获取：进入照片
	 */
	public String getAccPhoto() {
		return accPhoto;
	}
	/**
	 * 设置：出去时间
	 */
	public void setGououtDate(String gououtDate) {
		this.gououtDate = gououtDate;
	}
	/**
	 * 获取：出去时间
	 */
	public String getGououtDate() {
		return gououtDate;
	}
	/**
	 * 设置：出口id
	 */
	public void setExitId(String exitId) {
		this.exitId = exitId;
	}
	/**
	 * 获取：出口id
	 */
	public String getExitId() {
		return exitId;
	}
	/**
	 * 设置：出去图片
	 */
	public void setGououtPhoto(String gououtPhoto) {
		this.gououtPhoto = gououtPhoto;
	}
	/**
	 * 获取：出去图片
	 */
	public String getGououtPhoto() {
		return gououtPhoto;
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
	 * 设置：车牌id
	 */
	public void setPlateId(String plateId) {
		this.plateId = plateId;
	}
	/**
	 * 获取：车牌id
	 */
	public String getPlateId() {
		return plateId;
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
	/**
	 * 设置：
	 */
	public void setCrtUserId(String crtUserId) {
		this.crtUserId = crtUserId;
	}
	/**
	 * 获取：
	 */
	public String getCrtUserId() {
		return crtUserId;
	}
}
