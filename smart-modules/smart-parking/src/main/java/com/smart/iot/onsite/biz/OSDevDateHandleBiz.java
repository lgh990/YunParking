package com.smart.iot.onsite.biz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.smart.iot.parking.biz.*;
import com.smart.iot.parking.constant.BaseConstants;
import com.smart.iot.parking.entity.*;
import com.smart.iot.parking.utils.HttpUtil;
import com.smart.iot.parking.utils.SpiderGateWay;
import com.smart.iot.parking.vo.Devpackage;
import com.yuncitys.smart.parking.common.util.DateUtil;
import com.yuncitys.smart.parking.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@Transactional
public class OSDevDateHandleBiz {
    @Autowired
    public LotMsgBiz lotMsgBiz;
    @Autowired
    public ParkingOrdersOSBiz parkingOrdersOSBiz;
    @Autowired
    public OnerankdevBiz onerankdevBiz;
    @Autowired
    public ParkingSpaceBiz parkingSpaceBiz;
    @Autowired
    public HttpUtil httpUtil;
    @Value("${rpc.api_url}")
    public String api_url;
    @Value("${rpc.rpc_url}")
    public String rpc_url;
    @Value("${rpc.get_devparams_url}")
    public String get_devparams_url;
    @Value("${rpc.get_token_url}")
    public String get_token_url;
    @Value("${rpc.username}")
    public String username;
    @Value("${rpc.password}")
    public String password;
    public static String TOKEN = "";
    @Autowired
    public PublishMsgBiz publishMsgBiz;
    @Autowired
    public ParkingBiz parkingBiz;
    @Autowired
    public SpaceOnerankdeBiz spaceOnerankdeBiz;
    @Autowired
    public PlateBiz plateBiz;
    SpiderGateWay c_spd = new SpiderGateWay();

    public void HandleOnsiteGeomagneticUpageck(Devpackage devpackage, Onerankdev onerankdev, ParkingSpace space, Parking parking) {
        updateAbnormalLotMsg(space);
        if ("地磁状态变化包".equals(devpackage.getUpackageType())) {
            //每次数据包发生时数据流水加1，初始值为0，表示开机第一包，而后255加1后回1；
            log.info("------------------------------------------------开始处理地磁状态变化包------------------------------------------------");
            String spaceStatus = devpackage.getSpaceStatus();
            if (space != null) {
                if (!space.getLotType().equals(devpackage.getSpaceStatus())) {
                    String occurrenceTime = devpackage.getOccurrenceTime();
                    log.info(space.getSpaceNum() + "车位" + ("y".equals(spaceStatus) ? "有车" : "无车"));
                    lotMsgBiz.updateLotMsg(space.getSpaceId(), spaceStatus, occurrenceTime);
                    if (spaceStatus.equals(BaseConstants.enabledFlag.n)) {   //无车（由有车变无车）
                        log.info("-----------------------------室内，极光推送：有车变无车-----------------------------");
                        //进行操作，关闭车位锁
                        if (space.getSpaceType().equals(BaseConstants.SpaceType.private1) || space.getSpaceType().equals(BaseConstants.SpaceType.vip)) {
                            log.info("-------------车位是vip车位，由有车变无车，关闭车位锁----------------");
                            space.setLockl(BaseConstants.enabledFlag.n);
                            parkingOrdersOSBiz.CarLockCannel(spaceStatus, space.getSpaceId());
                        }
                    } else if (spaceStatus.equals(BaseConstants.enabledFlag.y)) {   //有车（由无车变有车）
                        log.info("-----------------------------室内，极光推送：无车变有车-----------------------------");
                    }
                    space.setLotType(spaceStatus);
                    //推送
                    publishMsgBiz.publishPdaAndWebMsg(parking.getParkingId(), space);
                }
            }
            parkingSpaceBiz.saveOrUpdate(space);
            //更新最新数据包流水号
            log.info("更新设备流水包号");
            onerankdev.setFlowNum(devpackage.getFlowPackgeNum());
            onerankdev.setLastFlowDate(DateUtil.dateTimeToStr(new Date()));
        }
        onerankdevBiz.saveOrUpdate(onerankdev);
    }
    private void updateAbnormalLotMsg(ParkingSpace space) {
        //判断车位是否处于失联状态
        if(space.getSpaceStatus().equals(BaseConstants.errorType.communication) && space.getLotType().equals(BaseConstants.enabledFlag.y))
        {
            space.setSpaceStatus(BaseConstants.errorType.normal);
        }
    }


    public void carVideoData(Devpackage devpackage,SpaceOnerankde spaceOnerankde,ParkingSpace space) {
        if(devpackage.getSpaceStatus().equals(BaseConstants.enabledFlag.y)){
            Plate paramsPlate=new Plate();
            paramsPlate.setCarNumber(devpackage.getCarNumber());
            Plate plate=plateBiz.selectOne(paramsPlate);
            if (plate == null) {
                plate = new Plate();
                plate.setPlaId(StringUtil.uuid());
                plate.setCarNumber(devpackage.getCarNumber());
                //通过车牌颜色判断车的大小
                String carType = getCarTypeByPlateColor(devpackage.getCarNumColor());
                plate.setCarType(carType);
                plateBiz.saveOrUpdate(plate);
            }
            ParkingOrders ordersParams=new ParkingOrders();
            ordersParams.setPlaId(plate.getPlaId());
            ordersParams.setOrderStatus(BaseConstants.OrderStatus.running);
            ParkingOrders parkingOrders=parkingOrdersOSBiz.selectOne(ordersParams);
            if(parkingOrders!=null) {
                parkingOrders.setSpaceId(space.getSpaceId());
                parkingOrdersOSBiz.updateById(parkingOrders);
                log.info("车牌:"+plate.getCarNumber()+"已绑定车位:"+space.getSpaceNum());
            }
            space.setLotType(BaseConstants.enabledFlag.y);
        }else{
            log.info("-----------------------------室内，极光推送：有车变无车-----------------------------");
            //进行操作，关闭车位锁
            if (space.getSpaceType().equals(BaseConstants.SpaceType.private1) || space.getSpaceType().equals(BaseConstants.SpaceType.vip)) {
                log.info("-------------车位是vip车位，由有车变无车，关闭车位锁----------------");
                space.setLockl(BaseConstants.enabledFlag.n);
                parkingOrdersOSBiz.CarLockCannel("0", space.getSpaceId());
            }
            space.setLotType(BaseConstants.enabledFlag.n);
        }
        parkingSpaceBiz.saveOrUpdate(space);
    }
    //0表示蓝，1黄，2黑，3白，4其他
    private String getCarTypeByPlateColor(String plate) {
        if ("1".equals(plate)) {
            return BaseConstants.plateType.truck;
        } else {
            return BaseConstants.plateType.auto;
        }
    }

    /**
     * 超声波上传有车无车包处理
     */
    public void HandleSpaceStatuspageckByUltrasonic(Devpackage devpackage, Onerankdev onerankdev, ParkingSpace space, Parking parking) {
        log.info("------------------------------------------------超声波有车无车包处理------------------------------------------------");
        if (devpackage!=null && "主动上报".equals(devpackage.getUpackageType())) {
            log.info("------------------------------------------------开始处理主动上报超声波态变化包------------------------------------------------");
            String spaceStatus = "1".equals(devpackage.getSpaceStatus()) ? BaseConstants.enabledFlag.y : BaseConstants.enabledFlag.n;
            if (space != null) {
                log.info("==============车位信息:"+space.getSpaceNum()+ "车位" + ("1".equals(devpackage.getSpaceStatus()) ? "有车" : "无车"));
                if (!space.getLotType().equals(spaceStatus)) {
                    log.info(space.getSpaceNum() + "车位" + ("1".equals(devpackage.getSpaceStatus()) ? "有车" : "无车"));
                    log.info("spaceStatus============"+spaceStatus);
                    if (spaceStatus.equals(BaseConstants.enabledFlag.n)) {
                        if (BaseConstants.enabledFlag.y.equals(space.getLockl())) {
                            log.info("关闭车位锁");
                            String code = parkingOrdersOSBiz.CarLockCannel(spaceStatus, space.getSpaceId());
                            if ("200".equals(code)) {
                                space.setLockl(BaseConstants.enabledFlag.n);
                            }
                        }
                        space.setLotType(BaseConstants.enabledFlag.n);
                    } else {
                        log.info("------------------------------------------------开始处理地超声波有车------------------------------------------------");
                        space.setLotType(BaseConstants.enabledFlag.y);
                    }
                   /* if(!StringUtil.isEmpty(space.getAttr1())){
                        Onerankdev params=new Onerankdev();
                        params.setOnerankdevDevSn(space.getAttr2());
                        Onerankdev onerankdev1=onerankdevBiz.selectOne(params);
                        long spaceCount=0;
                        if(!StringUtil.isEmpty(onerankdev1.getAttr1())){
                            spaceCount=Long.parseLong(onerankdev1.getAttr1());
                        }
                        if(devpackage.getSpaceStatus().equals("0")){
                            onerankdev1.setAttr1(String.valueOf(spaceCount+1));
                        }else{
                            onerankdev1.setAttr1(String.valueOf(spaceCount-1));
                        }
                        spaceCount=Long.parseLong(onerankdev1.getAttr1());
                        onerankdevBiz.saveOrUpdate(onerankdev1);
                        *//*byte[] bt = c_spd.OperateBootSrceen1(spaceCount, onerankdev1.getOnerankdevTerminId());
                        parkingOrdersOSBiz.operateDevByPost(onerankdev, bt);*//*
                    }*/
                    parkingSpaceBiz.saveOrUpdate(space);
                    //推送
                    publishMsgBiz.publishPdaAndWebMsg(parking.getParkingId(), space);
                }
            }
        }
    }

    public void queryDeviceArrr() {
        HashMap<String, String> mData = new HashMap<String, String>();
        mData.put("type", "default");
        mData.put("limit", "1000");
        String js = httpUtil.ajaxProxyGet(get_devparams_url.toString(), mData);
        JSONObject jsonObject = JSONObject.parseObject(js);
        JSONArray jdata = (JSONArray) jsonObject.get("data");
        List<Onerankdev> onerankdevlist = onerankdevBiz.selectListAll();
        for (int i = 0; i < jdata.size(); i++) {
            // 遍历 jsonarray 数组，把每一个对象转成 json 对象
            JSONObject job = jdata.getJSONObject(i);
            JSONObject id = (JSONObject) job.get("id");
            String name = (String) job.get("name");
            if (name.split("/").length > 1) {
                String devType = name.split("/")[1].split("_")[2];
                String gwDevid = name.split("/")[1].split("_")[1];
                String devId = (String) id.get("id");
                boolean flag = false;
                for (Onerankdev onerankdev : onerankdevlist) {
                    if(devType.equals(BaseConstants.devType.CAR_VIDEO) && devType.equals(onerankdev.getOnerankdevType())){
                        int index=onerankdev.getOnerankdevDevSn().lastIndexOf("_");
                        String devSn=onerankdev.getOnerankdevDevSn().substring(0,index);
                        onerankdev.setOnerankdevDevSn(devSn);
                        if (onerankdev.getOnerankdevDevSn().equals(devId) && devType.equals(onerankdev.getOnerankdevType())) {
                            flag = true;
                        }
                    }else {
                        if (onerankdev.getOnerankdevDevSn().equals(devId) && devType.equals(onerankdev.getOnerankdevType())) {
                            flag = true;
                        }
                    }
                }
                if (!flag) {
                    if(devType.equals(BaseConstants.devType.CAR_VIDEO)) {
                        for(int j=1;j<=3;j++){
                            Onerankdev onerankdev = new Onerankdev();
                            onerankdev.setDevId(StringUtil.uuid());
                            onerankdev.setOnerankdevDevSn(devId+"_"+j);
                            onerankdev.setOnerankdevType(devType);
                            onerankdev.setOnerankdevTerminId(gwDevid);
                            onerankdevBiz.saveOrUpdate(onerankdev);
                        }
                    }else{
                        Onerankdev onerankdev = new Onerankdev();
                        onerankdev.setDevId(StringUtil.uuid());
                        onerankdev.setOnerankdevDevSn(devId);
                        onerankdev.setOnerankdevType(devType);
                        onerankdev.setOnerankdevTerminId(gwDevid);
                        onerankdevBiz.saveOrUpdate(onerankdev);
                    }
                }
            }
        }
    }
}

