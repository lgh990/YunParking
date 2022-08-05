package com.smart.iot.start.biz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.smart.iot.onsite.biz.OSDevDateHandleBiz;
import com.smart.iot.parking.biz.*;
import com.smart.iot.parking.constant.BaseConstants;
import com.smart.iot.parking.constant.DevConstants;
import com.smart.iot.parking.entity.*;
import com.smart.iot.parking.srever.DevBusinessService;
import com.smart.iot.parking.vo.Devpackage;
import com.smart.iot.roadside.biz.RSDevDateHandleBiz;
import com.smart.iot.start.util.publicUtil;
import com.yuncitys.smart.parking.common.util.DateUtil;
import com.yuncitys.smart.parking.common.util.StringUtil;
import com.sun.xml.internal.rngom.parse.host.Base;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created by Administrator on 2018/8/22 0022.
 */
@Slf4j
@Service
@Transactional
public class EquipmentDataProcess implements DevBusinessService {
    @Autowired
    public OnerankdevBiz onerankdevBiz;
    @Autowired
    public SpaceOnerankdeBiz spaceOnerankdeBiz;
    @Autowired
    public ParkingSpaceBiz parkingSpaceBiz;
    @Autowired
    public ParkingBiz parkingBiz;
    @Autowired
    public ParkingBusinessTypeBiz parkingBusinessTypeBiz;
    @Autowired
    public OSDevDateHandleBiz oSDevDateHandleBiz;
    @Autowired
    public RSDevDateHandleBiz rSDevDateHandleBiz;
    @Autowired
    public PublishMsgBiz publishMsgBiz;
    @Autowired
    public ReservatRecordBiz reservatRecordBiz;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private DevGatewayBiz devGatewayBiz;
    //原始临时数据
    public static final String ORIGINAL_PAYLOAD_KEY = "IOT:EXPER:MQTT:ORIGINAL:";
    private static final org.jboss.logging.Logger logger = org.jboss.logging.Logger.getLogger(EquipmentDataProcess.class);
    @Override
    public void dealRxData(String topic, String msg) {
        long startTime=System.currentTimeMillis();

        JSONObject msgJs = JSONObject.parseObject(msg);
        /*************将JSOn数据存入数据库********************/
        MqttMsg mqttMsg=new MqttMsg();
        mqttMsg.setMsg(msg);
        mqttMsg.setTopic(topic);
        mqttMsg.setDataOwnedSys(109);
/*
        mqttMsgBiz.insertSelective(mqttMsg);
*/

        /******************************************************/
        int msgType = (int) msgJs.get("msgType");

        log.info("topic:"+topic+"----"+"msg:"+msg);
        if(topic.split("/").length == 5) {
            switch (msgType) {
                case DevConstants.mqMsgType.DEV_ATTR://设备属性数据

                    List<Onerankdev> onerankdevlist = onerankdevBiz.selectListAll();
                    String devType = msgJs.get("devtype")+"";
                    String gwDevid = msgJs.get("id")+"";
                    String devId =  msgJs.get("deviceId")+"";
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
                    break;
                case DevConstants.mqMsgType.DEV_DATA://设备状态数据
                    int index=topic.lastIndexOf("/");
                    String key=ORIGINAL_PAYLOAD_KEY+topic.substring(0,index);//得到topic
                    double num=Double.valueOf(topic.substring(index+1));//得到流水号
                    ZSetOperations<String,String> setOperations=redisTemplate.opsForZSet();
                    setOperations.add(key,msg,num);//根据流水号排序添加
                    redisTemplate.expire(key,24, TimeUnit.HOURS);//集合暂定有效24小时
                    long len=setOperations.size(key);
                    if(len>1){//有库存
                        setOperations.range(key,0,len);
                        Set<String> sets= setOperations.range(key,0,len);//得到一个有序集合
                        Iterator<String> scan=sets.iterator();
                        double score=0;
                        while (scan.hasNext()){//流水号从小到大遍历
                            msg=scan.next();
                            msgJs = JSONObject.parseObject(msg);
                            score=msgJs.getDouble("countTopicId");//得到集合中最大的流水号
                            decode(msgJs,topic);
                        }
                        setOperations.removeRangeByScore(key,0,score);//处理后清除
                    }else{
                        decode(msgJs,topic);
                        setOperations.remove(key,msg);//处理后清除
                    }
                    break;
                default:
                    log.info("未找到类型进入default");
                    break;
            }
        }
/*
        MqttMessage message = new MqttMessage(JSON.toJSONString("ss").getBytes());
        // 设置消息的服务质量
        message.setQos(1);
        // 发布消息
        MqttClient.mqttClient.publish("iot_topic/gw/",message);
*/
        //执行方法
        long endTime=System.currentTimeMillis();
        float excTime=(float)(endTime-startTime)/1000;
        System.out.println("执行时间："+excTime+"s");
    }

    private void decode(JSONObject msgJs,String topic){
        String devType =String.valueOf(msgJs.get("2_devTypeCode"));
        if(!(devType.equals(BaseConstants.devType.WC_GEOMAGNETIC) || !(devType.equals(BaseConstants.devType.ULTRA_VEHI_DET)) || !(devType.equals(BaseConstants.devType.SECOND_WC_GEOMAGNETIC) || devType.equals(BaseConstants.devType.RELAY))))
        {
            return;
        }
        String packageType = (String) msgJs.get("type");
        String deviceId = (String) msgJs.get("deviceId");
        String data="";
        if(msgJs.get("data")!=null &&  !StringUtil.isEmpty(String.valueOf(msgJs.get("data")))) {
            data = String.valueOf( msgJs.get("data"));
        }
        if("0".equals(data)){
            return;
        }
        byte[] dataByte = hexToBytes(data);
        String flowNum = topic.split("/")[3];
        Map<String, Object> analyData = new ConcurrentHashMap<String, Object>();
        Devpackage devpackage = new Devpackage();
        //一级地磁
        if ((devType.equals(BaseConstants.devType.WC_GEOMAGNETIC) || devType.equals(BaseConstants.devType.SECOND_WC_GEOMAGNETIC)) && packageType.equals(DevConstants.packageType.U_PACKAGE)) {
            devpackage.setUpackageType("0".equals(String.valueOf(dataByte[0] & 0xFF)) ? "心跳包" : "地磁状态变化包");
            devpackage.setDeviceId(deviceId);
            devpackage.setPackageType("U包");
            if ("地磁状态变化包".equals(devpackage.getUpackageType())) {
                devpackage.setDevtype(devType);
                devpackage.setSpaceStatus((msgJs.get("2_spaceStatus").equals("0") ? BaseConstants.enabledFlag.n : BaseConstants.enabledFlag.y));
                devpackage.setBrighValue((String) msgJs.get("2_brighValue"));
                devpackage.setRFRecStreng((String) msgJs.get("2_rfRecStreng"));
                devpackage.setClockState((String) msgJs.get("2_clockState"));
                devpackage.setOccurrenceTime((String) msgJs.get("2_deviceTime"));
                devpackage.setFlowPackgeNum((Integer) msgJs.get("2_packageSerialNumberHead"));
                try {} catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else if(packageType.equals(DevConstants.packageType.U_PACKAGE) && devType.equals(BaseConstants.devType.ULTRA_VEHI_DET))
        {
            devpackage.setUpackageType("0".equals(String.valueOf((dataByte[0] & 0x08) >> 3)) ? "主动上报" : "数据应答");
            if ("主动上报".equals(devpackage.getUpackageType())) {
                devpackage.setFlowPackgeNum(dataByte[1] & 0xFF);
                devpackage.setSpaceStatus(String.valueOf(dataByte[3] & 0xFF));
                devpackage.setDeviceId(deviceId);
                devpackage.setPackageType("U包");
            }
        }else if( devType.equals(BaseConstants.devType.CAR_VIDEO)){
            String upackageType=String.valueOf(dataByte[0] & 0xFF);
            //16进制 60=大华 61=臻识
            if("96".equals(upackageType)) {
                //大华车位视频
                devpackage.setSpaceStatus(String.valueOf(dataByte[3] & 0xFF));
                devpackage.setNumber(String.valueOf(dataByte[4] & 0xFF));
                devpackage.setCarNumColor(String.valueOf(dataByte[5] & 0xFF));
                int carNumLenth = Integer.parseInt(String.valueOf(dataByte[6] & 0xFF));
                String carNum = "";
                byte[] bk = new byte[carNumLenth];
                for (int s = 1; s <= carNumLenth; s++) {
                    bk[s - 1] = (byte) (dataByte[6 + s] & 0xFF);
                }
                try {
                    carNum = new String(bk, "GBK");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                devpackage.setPackageType("M包");
                devpackage.setDevtype(devType);
                devpackage.setCarNumber(carNum);
                devpackage.setDeviceId(deviceId + "_" + devpackage.getNumber());
            }else if("97".equals(upackageType)){
                log.info("======================视频接到臻识");
                String spaceStatus=String.valueOf(dataByte[3] & 0xFF);
                if("1".equals(spaceStatus)){
                    spaceStatus=BaseConstants.enabledFlag.y;
                }else{
                    spaceStatus=BaseConstants.enabledFlag.n;
                }
                devpackage.setSpaceStatus(spaceStatus);
                devpackage.setNumber(String.valueOf(dataByte[4] & 0xFF));
                devpackage.setCarNumColor(String.valueOf(dataByte[5] & 0xFF));
                int carNumLenth = Integer.parseInt(String.valueOf(dataByte[6] & 0xFF));
                String carNum = "";
                if("y".equals(devpackage.getSpaceStatus())) {
                    byte[] bk = new byte[carNumLenth];
                    for (int s = 1; s <= carNumLenth; s++) {
                        bk[s - 1] = (byte) (dataByte[6 + s] & 0xFF);
                    }
                    try {
                        String type=String.valueOf(bk[0] & 0xFF);
                        carNum = new String(bk, "GBK");
                        carNum=carNum.substring(1,carNum.length());
                        String plateHead=publicUtil.licensePlateHead(Integer.valueOf(type));
                        carNum=plateHead+carNum;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                devpackage.setPackageType("M包");
                devpackage.setDevtype(devType);
                devpackage.setCarNumber(carNum);
                devpackage.setDeviceId(deviceId + "_" + devpackage.getNumber());
                String logs= "y".equals(devpackage.getSpaceStatus()) ?"有车":"无车";
                log.info("=======视频接到车牌:"+carNum+"，车位状态:"+logs+"车位号:"+devpackage.getNumber());
            }
        }
        if(devpackage==null || StringUtil.isEmpty(devpackage.getDeviceId())){
            logger.info("======================deviceId为空！！！");
            return;
        }
        comsumDevGeomagneticPackage(devpackage);
    }



    /**
     * 处理数据包方法
     * 所有数据包都通过该方法
     *
     * @return
     * @throws IOException
     * @param devpackage
     */
    public void comsumDevGeomagneticPackage(Devpackage devpackage){
        Onerankdev onerankdevEx = new  Onerankdev();
        onerankdevEx.setOnerankdevDevSn(devpackage.getDeviceId());
        Onerankdev onerankdev = onerankdevBiz.selectOne(onerankdevEx);
        if(onerankdev != null) {
            String devType = onerankdev.getOnerankdevType();
            SpaceOnerankde spaceOnerankdeEx = new  SpaceOnerankde();
            spaceOnerankdeEx.setOnerankdevSn(onerankdev.getOnerankdevDevSn());
            SpaceOnerankde spaceOnerankde = spaceOnerankdeBiz.selectOne(spaceOnerankdeEx);
            //地磁处于未绑定状态则不处理该信息
            if (spaceOnerankde == null) {
                return;
            }
            ParkingSpace space = parkingSpaceBiz.selectById(spaceOnerankde.getSpaceId());
            String currSpaceStatus = space.getLotType();
            Parking parking = parkingBiz.selectById(space.getParkingId());
            ParkingBusinessType parkingBusinessType = parkingBusinessTypeBiz.selectById(parking.getParkingBusType());
            if ("U包".equals(devpackage.getPackageType()) && (devType.equals(BaseConstants.devType.WC_GEOMAGNETIC) ||  (devType.equals(BaseConstants.devType.SECOND_WC_GEOMAGNETIC)))) {
                if(parkingBusinessType.getBusinessModule().equals(BaseConstants.BusinessModule.ace_onsite_server)) {
                    oSDevDateHandleBiz.HandleOnsiteGeomagneticUpageck(devpackage, onerankdev, space,parking);
                }else if(parkingBusinessType.getBusinessModule().equals(BaseConstants.BusinessModule.ace_roadside_server)) {
                    rSDevDateHandleBiz.HandleOnsiteGeomagneticUpageck(devpackage, onerankdev, space,parking);
                }
            }else if ("U包".equals(devpackage.getPackageType()) && (devType.equals(BaseConstants.devType.ULTRA_VEHI_DET) )) {
                if(parkingBusinessType.getBusinessModule().equals(BaseConstants.BusinessModule.ace_onsite_server)) {
                    oSDevDateHandleBiz.HandleSpaceStatuspageckByUltrasonic(devpackage, onerankdev, space,parking);
                }
            }else if(devType.equals(BaseConstants.devType.CAR_VIDEO)){
                oSDevDateHandleBiz.carVideoData(devpackage,spaceOnerankde,space);
            }
            if(devType.equals(BaseConstants.devType.CAR_VIDEO) && devpackage.getSpaceStatus().equals(BaseConstants.enabledFlag.y) && space.getSpaceType().equals(BaseConstants.SpaceType.common))
            {
                log.info("=====执行判断是否存在预定记录");
                ReservatRecord example = new ReservatRecord();
                example.setSpaceId(space.getSpaceId());
                example.setReservatState(BaseConstants.proceStatus.running);
                //判断是否存在预定记录
                ReservatRecord reservatRecord = reservatRecordBiz.selectOne(example);
                if(reservatRecord != null) {
                    try {
                        reservatRecord.setEndDate(DateUtil.getDateTime());
                        reservatRecord.setReservatState(BaseConstants.proceStatus.complete);
                        reservatRecordBiz.saveOrUpdate(reservatRecord);
                        publishMsgBiz.publishParkingSpacesOccupiedInformation(space,reservatRecord.getUserId());
                        log.info("=====推送重新分配车位");
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static String formZero(int num) {
        if( num < 10)
        {
            return "0" + num;
        }
        return String.valueOf(num);
    }
    private static String formZero1(String num) {
        if(Integer.valueOf(num) < 10)
        {
            return "0" + num;
        }
        return String.valueOf(num);

    }

    //字符串转byte
    public static byte[] hexToBytes(String hexString)
    {
        if (hexString == null || "".equals(hexString))
        {
            return null;
        }
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] bytes = new byte[length];
        String hexDigits = "0123456789ABCDEF";
        byte[] hex = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F };
        for (int i = 0; i < length; i++)
        {
            int pos = i * 2; // 两个字符对应一个byte
            int h = 0; // 注1
            int l = 0;
            for (int x = 0; x < hexDigits.length(); x++)
            {
                if (hexDigits.charAt(x) == hexChars[pos])
                {
                    h = hex[x] << 4;
                }
                if (hexDigits.charAt(x) == hexChars[pos + 1])
                {
                    l = hex[x];
                }
            }
            if (h == -1 || l == -1)
            { // 非16进制字符
                return null;
            }
            bytes[i] = (byte) (h | l);
        }
        return bytes;
    }
}
