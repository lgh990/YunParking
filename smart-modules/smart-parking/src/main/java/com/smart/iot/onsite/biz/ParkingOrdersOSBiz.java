package com.smart.iot.onsite.biz;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smart.iot.onsite.feign.ChargeRulesTypeFeign;
import com.smart.iot.onsite.feign.ToolFeign;
import com.smart.iot.parking.biz.*;
import com.smart.iot.parking.constant.BaseConstants;
import com.smart.iot.parking.constant.DevConstants;
import com.smart.iot.parking.entity.*;
import com.smart.iot.parking.feign.DepartFeign;
import com.smart.iot.parking.feign.DictFeign;
import com.smart.iot.parking.mapper.OnerankdevMapper;
import com.smart.iot.parking.mapper.ParkingOrdersMapper;
import com.smart.iot.parking.mapper.ReservatRecordMapper;
import com.smart.iot.parking.rabbitmq.ExpirationMessagePostProcessor;
import com.smart.iot.parking.rabbitmq.QueueConfig;
import com.smart.iot.parking.utils.*;
import com.smart.iot.parking.vo.ParkingOrdersVo;
import com.smart.iot.parking.vo.UserParkingSpaceVo;
import com.smart.iot.roadside.biz.ParkingOrdersRSBiz;
import com.yuncitys.smart.merge.core.MergeCore;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.msg.TableResultPageResponse;
import com.yuncitys.smart.parking.common.msg.TableResultResponse;
import com.yuncitys.smart.parking.common.util.DateUtil;
import com.yuncitys.smart.parking.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.font.CreatedFontTracker;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;


/**
 * ?????????
 *
 * @author Mr.AG
 *@version 2022-08-07 14:53:11
 * @email 
 */
@Service
@Transactional
@Slf4j
public class ParkingOrdersOSBiz extends BusinessBiz<ParkingOrdersMapper, ParkingOrders> {
    @Autowired
    public OnerankdevMapper onerankdevMapper;
    @Autowired
    public ParkingIoBiz parkingIoBiz;
    @Autowired
    public UserMoncardsBiz userMoncardsBiz;
    @Autowired
    public ParkingOrdersOSBiz ordersBiz;
    @Autowired
    public IoRecordBiz ioRecordBiz;
    @Autowired
    public ParkingOrdersMapper parkingOrdersMapper;
    @Autowired
    public UserParkingSpaceBiz userParkingSpaceBiz;
    @Autowired
    public DictFeign dictFeign;
    @Autowired
    public UserParkingBiz userParkingBiz;
    @Autowired
    public DepartFeign departFeign;
    SpiderGateWay c_spd = new SpiderGateWay();
    @Value("${sysPath}")
    private String sysPath;

    @Value("${alipay.ali_app_partner}")
    private String ali_app_partner;
    @Value("${alipay.ali_app_seller_id}")
    private String ali_app_seller_id;
    @Value("${alipay.ali_notify_url}")
    private String ali_notify_url;
    @Value("${alipay.ali_app_id}")
    private String ali_app_id;
    @Value("${alipay.ali_private_sign}")
    private String ali_private_sign;

    @Value("${wcpay.wc_app_appid}")
    private String wc_app_appid;
    @Value("${wcpay.wc_app_mchid}")
    private String wc_app_mchid;
    @Value("${wcpay.wc_app_apikey}")
    private String wc_app_apikey;
    @Value("${wcpay.wc_app_notify_url}")
    private String wc_app_notify_url;

    @Value("${rpc.api_url}")
    private String api_url;
    @Value("${rpc.rpc_url}")
    private String rpc_url;
    @Value("${rpc.get_devparams_url}")
    private String get_devparams_url;
    @Value("${rpc.get_token_url}")
    private String get_token_url;
    @Value("${rpc.username}")
    private String username;
    @Value("${rpc.password}")
    private String password;
    @Autowired
    private HttpUtil httpUtil;
    @Autowired
    public ReservatRecordMapper reservatRecordMapper;
    @Autowired
    public SpaceOnerankdeBiz spaceOnerankdeBiz;
    @Autowired
    public OnerankdevBiz onerankdevBiz;
    @Autowired
    public PublishMsgBiz publishMsgBiz;
    @Autowired
    public ParkingBiz parkingBiz;
    @Autowired
    public PlateBiz plateBiz;
    @Autowired
    public ChargeRulesTypeFeign chargeRulesTypeFeign;
    @Autowired
    public ReservatRecordBiz reservatRecordBiz;
    @Autowired
    public ParkingSpaceBiz parkingSpaceBiz;
    @Autowired
    public RabbitTemplate rabbitTemplate;
    @Autowired
    public AppUserBiz appUserBiz;
    @Autowired
    public ParkingOrdersBiz parkingOrdersBiz;
    @Autowired
    public MergeCore mergeCore;
    @Autowired
    public ParkingOrdersRSBiz parkingOrdersRSBiz;
    @Autowired
    public ToolFeign toolFeign;
    @Autowired
    public CombinePayBiz combinePayBiz;
    public volatile Set set=new HashSet();
    public ObjectRestResponse<AppUser> buyMonthCard(ParkingOrders parkingOrders) {
        String parkingId = parkingOrders.getParkingId();
        Parking parking = parkingBiz.selectById(parkingId);
        BigDecimal dMonthCount = new BigDecimal(parkingOrders.getMonthCount());
        BigDecimal money = parkingOrders.getMonthCardPrice().multiply(dMonthCount);
        AppUser user = appUserBiz.selectById(parkingOrders.getUserId());
        String password = user.getPassword();
        String userId = user.getId();

        BigDecimal monthCardPrice = parkingOrders.getMonthCardPrice();
        int monthCount = parkingOrders.getMonthCount();
        Plate plate = plateBiz.selectById(parkingOrders.getPlaId());

        //??????????????????
        if (!parking.getParkingType().equals(BaseConstants.parkingType.onsize)) {
            return new ObjectRestResponse<AppUser>().BaseResponse(BaseConstants.StateConstates.NO_ONSIZE_PARKING_CODE, BaseConstants.StateConstates.NO_ONSIZE_PARKING_MSG);
        }
        if (user.getNosecretPayFlag().equals(BaseConstants.enabledFlag.n)) {
            if (password == null) {
                return new ObjectRestResponse<AppUser>().BaseResponse(BaseConstants.StateConstates.PARAMETER_IS_EMPTY_ID, BaseConstants.StateConstates.PARAMETER_IS_EMPTY_MSG);
            }
            if (StringUtil.isEmpty(user.getPassword())) {
                return new ObjectRestResponse<AppUser>().BaseResponse(BaseConstants.StateConstates.CODE_SETTINGS_CODE, BaseConstants.StateConstates.CODE_SETTINGS_MSG);
            }
            if (!user.getPassword().equals(password)) {
                return new ObjectRestResponse<AppUser>().BaseResponse(BaseConstants.StateConstates.PAYMENT_PASSWORD_ID, BaseConstants.StateConstates.PAYMENT_PASSWORD_MSG);
            }
        }
        if (user.getMoney() == null) {
            return new ObjectRestResponse<AppUser>().BaseResponse(BaseConstants.StateConstates.BALANCE_INSUFFICIENT_ID, BaseConstants.StateConstates.BALANCE_INSUFFICIENT_MSG);
        }
        int sum = user.getMoney().compareTo(BigDecimal.valueOf(Double.valueOf(0)));
        if (sum == -1) {
            return new ObjectRestResponse<AppUser>().BaseResponse(BaseConstants.StateConstates.BALANCE_INSUFFICIENT_ID, BaseConstants.StateConstates.BALANCE_INSUFFICIENT_MSG);
        }
        sum = user.getMoney().compareTo(money);
        if (sum == -1) {
            return new ObjectRestResponse<AppUser>().BaseResponse(BaseConstants.StateConstates.BALANCE_INSUFFICIENT_ID, BaseConstants.StateConstates.BALANCE_INSUFFICIENT_MSG);
        }
        ParkingOrders exOrder = new ParkingOrders();
        exOrder.setUserId(userId);
        exOrder.setOrderStatus(BaseConstants.OrderStatus.running);
        exOrder.setOrderType(BaseConstants.OrderType.common);
        exOrder.setParkingId(parkingId);
        ParkingOrders order = ordersBiz.selectOne(exOrder);
        //?????????????????????????????????????????????????????????????????????
        if (order != null) {
            order.setOrderType(BaseConstants.OrderType.monthCard);
            ordersBiz.saveOrUpdate(order);
        }
        //??????????????????
        if (plate == null) {
            return new ObjectRestResponse<AppUser>().BaseResponse(BaseConstants.StateConstates.PLATE_NOT_EXIST_USER_CODE, BaseConstants.StateConstates.PLATE_NOT_EXIST_USER_MSG);
        }
        Example example=new Example(UserMoncards.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("plateId",plate.getPlaId());
        criteria.andEqualTo("parkingId",parkingId);
        example.orderBy("endDate").desc();
        List<UserMoncards> userMoncardsList=userMoncardsBiz.selectByExample(example);
        UserMoncards userMoncard =null;
        if(userMoncardsList!=null && userMoncardsList.size()>0) {
            userMoncard=userMoncardsList.get(0);
        }
        //????????????????????????
        UserMoncards userMoncards = new UserMoncards();
        Date dates = new Date();
        if (userMoncard != null) {
            userMoncards.setMoncardsId(StringUtil.uuid());
            dates = DateUtil.strToDate(userMoncard.getEndDate(), "yyyy-MM-dd HH:mm:ss");
        }else{
            userMoncards.setMoncardsId(StringUtil.uuid());
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(dates);
        calendar.add(Calendar.MONTH, monthCount);
        dates = calendar.getTime();
        String endDate = DateUtil.dateTimeToStr(dates);
        if(userMoncard!=null) {
            userMoncards.setStartDate(userMoncard.getEndDate());
        }else{
            userMoncards.setStartDate(DateUtil.format(new Date()));
        }
        userMoncards.setMoncardsHostman(user.getName());
        userMoncards.setMoncardsTelephone(user.getMobile());
        userMoncards.setParkingId(parkingId);
        userMoncards.setPlateId(plate.getPlaId());
        userMoncards.setEndDate(endDate);
        //????????????
        ParkingOrders parkingOrder = new ParkingOrders();
        parkingOrder.setOrderId(StringUtil.uuid());
        parkingOrder.setRealMoney(money);
        parkingOrder.setOrderStatus(BaseConstants.OrderStatus.complete);
        parkingOrder.setOrderType(BaseConstants.OrderType.pur_monthcard);
        parkingOrder.setParkingId(parkingId);
        String orderNum = DateUtil.getOrderNum();
        parkingOrder.setOrderNum(orderNum);
        parkingOrder.setOrderNumMd5(MD5.MD5(orderNum));
        parkingOrder.setPlaId(plate.getPlaId());
        parkingOrder.setUserId(userId);
        parkingOrder.setBeginDate(DateUtil.dateTimeToStr(dates));
        parkingOrder.setEndDate(endDate);
        parkingOrder.setMonthCardPrice(monthCardPrice);
        parkingOrder.setMonthCount(monthCount);
        parkingOrder.setParkingBusType(parking.getParkingBusType());
        DecimalFormat df = new DecimalFormat("######0.00"); //?????????????????????
        BigDecimal moneys = user.getMoney().subtract(money);
        moneys = new BigDecimal(df.format(moneys));
        user.setMoney(moneys);
        userMoncards.setOrderNum(orderNum);
        appUserBiz.saveOrUpdate(user);
        parkingOrdersBiz.saveOrUpdate(parkingOrder);
        userMoncardsBiz.saveOrUpdate(userMoncards);
        publishMsgBiz.publishMoncardMsg(parking.getParkingId());
        return new ObjectRestResponse<AppUser>().data(user);

    }

    public void ioBusiness(Map<String, Object> paramsMap) throws Exception {
        long startTime = System.currentTimeMillis();
        //?????????sn
        String camera_id = String.valueOf(paramsMap.get("camera_id"));
        //????????????
        String color = String.valueOf(paramsMap.get("color"));
        //????????????
        String car_plate = String.valueOf(paramsMap.get("car_plate"));
        //????????????
        String picture = String.valueOf(paramsMap.get("picture"));
        //1??????????????????
        String createOrderType = String.valueOf(paramsMap.get("createOrderType"));
        //??????
        String fragmentPicure = String.valueOf(paramsMap.get("fragmentPicure"));
        int type = Integer.valueOf(String.valueOf(paramsMap.get("type")));
        try {
            //???????????????
            ParkingIo parkingIoEx = new ParkingIo();
            parkingIoEx.setCameraSn(camera_id);
            ParkingIo parkingIo = parkingIoBiz.selectOne(parkingIoEx);
            if (parkingIo == null) {
                log.info("?????????????????????");
                return;
            }
            //??????????????????
            Plate plateEx = new Plate();
            plateEx.setCarNumber(car_plate);
            plateEx.setEnabledFlag(BaseConstants.enabledFlag.y);
            Plate plate = plateBiz.selectOne(plateEx);
            if (plate == null) {
                plate = new Plate();
                plate.setPlaId(StringUtil.uuid());
                plate.setCarNumber(car_plate);
                //????????????????????????????????????
                String carType = getCarTypeByPlateColor(color);
                plate.setCarType(carType);
                plateBiz.saveOrUpdate(plate);
            }
            if (parkingIo.getParkingIoType().equals(BaseConstants.parkingIoType.entrance)) {
                log.info("??????set??????????????????=====" + set.contains(car_plate));
                if (!set.contains(car_plate)) {
                    set.add(car_plate);
                    log.info("???????????????????????????========" + car_plate);
                } else {
                    operateTollScreen(parkingIo.getParkingIoId(), plate, BaseConstants.admin_status.NORMAL_ADMISSION, DevConstants.toolSrceen.CAR_INED,new BigDecimal(0));
                    log.info(car_plate + "===========????????????????????????????????????");
                    return;
                }
            }
            Calendar now = Calendar.getInstance();
            int year = now.get(Calendar.YEAR);
            int month = (now.get(Calendar.MONTH) + 1);
            ObjectRestResponse pictures = new ObjectRestResponse();
            ObjectRestResponse fragmentPicures = new ObjectRestResponse();
            if (!StringUtil.isEmpty(picture)) {
                pictures = toolFeign.imageUpload(picture, year + "_" + month + "/" + car_plate);
            }
            if (!StringUtil.isEmpty(fragmentPicure)) {
                fragmentPicures = toolFeign.imageUpload(fragmentPicure, year + "_" + month + "/" + car_plate + "/fragmentStr");
            }
             /*    //?????????????????????
            String ioLastplate = parkingIo.getIoLastplate();
            //???????????????????????????
            parkingIo.setIoLastplate(car_plate);
            parkingIoBiz.saveOrUpdate(parkingIo);
            //???????????????????????????????????????
            if (!car_plate.equals(ioLastplate) && !BaseConstants.createOrderType.artificial.equals(createOrderType)) {
                log.info("???????????????????????????" + car_plate);
            }*/
            //??????????????????
            if (parkingIo.getParkingIoType().equals(BaseConstants.parkingIoType.entrance)) {
                EntranceBusinessCannel(color, car_plate, String.valueOf(pictures.getData()), parkingIo, plate, parkingIo.getParkingId(), String.valueOf(fragmentPicures.getData()), type,set);
            } else {
                ExitBusinessCannel(color, car_plate, String.valueOf(pictures.getData()), parkingIo, plate, parkingIo.getParkingId(), String.valueOf(fragmentPicures.getData()), type);
                set.remove(car_plate);
            }
            //CarnelBootScreen(parkingIo.getParkingId());
            long endTime = System.currentTimeMillis();
            float excTime = (float) (endTime - startTime) / 1000;
            System.out.println("???????????????" + excTime + "s");
            }catch (Exception e){
                log.info(e.getMessage());
                set.remove(car_plate);
            }
    }

    /**
     * ????????????????????????
     *
     * @param color     ????????????
     * @param car_plate ????????????
     * @param picture   ????????????
     * @param parkingIo ???????????????
     * @param plate     ????????????
     * @param parkingId ?????????id
     */
    private void ExitBusinessCannel(String color, String car_plate, String picture, ParkingIo parkingIo, Plate plate, String parkingId,String fragmentPicure,int type) throws Exception {
        String typeMessage="????????????";
        Map<String,String> params=new HashMap<>();
        ParkingOrders exOrder = new ParkingOrders();
        exOrder.setOrderStatus(BaseConstants.OrderStatus.running);
        exOrder.setParkingId(parkingId);
        exOrder.setPlaId(plate.getPlaId());
        ParkingOrders order = ordersBiz.selectOne(exOrder);
        if (order == null) {
            params.put("msg",car_plate+"?????????????????????,????????????,?????????????????????!");
            RobotUtil.screenMessage(params,parkingIo);
            RobotUtil.videoMessage(params,parkingIo);
            log.info("??????????????????????????????,???????????????");
            return;
        }
        IoRecord ioRecord = QueryIorecord(parkingId, plate.getPlaId());
        Parking parking = parkingBiz.selectById(parkingId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("parkingId", parkingId);
        /*String carType="";
        if(plate.getCarType().equals("2")){
            carType="auto";
        }else if(plate.getCarType().equals("1")){
            carType="truck";
        }*/
        map.put("carType",plate.getCarType());
        map.put("chargeRuleType", parking.getChargeRuleId());
        map.put("beginDate", order.getBeginDate());
        if(order.getOrderType().equals(BaseConstants.OrderType.vip))
        {
            typeMessage="vip??????";
            map.put("type","vip");
            map.put("orderNum",order.getOrderNum());
        }
        BigDecimal money=new BigDecimal(0);
        if(type==0) {
            money= chargeRulesTypeFeign.queryCostByIdAndParkingId(map).getData();
        }
        BigDecimal moneys=money;
        BigDecimal currMoney = new BigDecimal(0);
        AppUser appUser = null;

        if (order.getUserId() != null) {
            appUser = appUserBiz.selectById(order.getUserId());
            currMoney = appUser.getMoney();
        }

        if (!order.getPayStatus().equals(BaseConstants.status.success)) {

             if (order.getOrderType().equals(BaseConstants.OrderType.monthCard) || type==1) {
                 typeMessage="????????????";
                money = new BigDecimal(0);
            }
            int count=order.getRealMoney().compareTo(money);
            if(order.getOrderType().equals(BaseConstants.OrderType.vip) && (count==1) ){
                money = new BigDecimal(0);
            }else if(order.getOrderType().equals(BaseConstants.OrderType.vip)){
                log.info("========================vip="+money+"??????" );
                money = money.subtract(order.getRealMoney());
            }
            if(type==1){
                String longTime = DateUtil.getDateDiff(order.getBeginDate(), DateUtil.getDateTime());
                order.setParkingLongTime(longTime);   //??????????????????
                order.setPayType(BaseConstants.payType.free); //???????????????
                order.setAttr1(String.valueOf(money));
                order.setRealMoney(moneys);
                order.setChargeDate(DateUtil.getDateTime());
                //???????????????????????????
                parking.setParkingRevenue(parking.getParkingRevenue().add(money));
                parkingBiz.saveOrUpdate(parking);
            }else if (plate.getUserId() != null) {
                typeMessage="????????????";
                if (currMoney.compareTo(money) == 1 || currMoney.compareTo(money) == 0) {   //????????????????????????
                    currMoney = currMoney.subtract(money); //??????????????????
                    String longTime = DateUtil.getDateDiff(order.getBeginDate(), DateUtil.getDateTime());
                    order.setParkingLongTime(longTime);   //??????????????????
                    order.setPayType(BaseConstants.payType.balance); //?????????????????????
                    if(!order.getOrderType().equals(BaseConstants.OrderType.vip)) {
                        order.setRealMoney(moneys);
                    }
                    if(order.getOrderType().equals(BaseConstants.OrderType.monthCard)) {
                        order.setAttr1(String.valueOf(money));
                    }else{
                        order.setAttr1(String.valueOf(moneys));
                    }
                    order.setChargeDate(DateUtil.getDateTime());
                    //???????????????????????????
                    appUser.setMoney(currMoney);
                    parking.setParkingRevenue(parking.getParkingRevenue().add(money));
                    appUserBiz.saveOrUpdate(appUser);
                    parkingBiz.saveOrUpdate(parking);
                } else {
                    //??????????????????
                    if(order.getUserId() != null) {
                        publishMsgBiz.publishBalanceIsNotEnough(order.getUserId());
                    }
                    ioRecord.setAttr1(fragmentPicure);
                    ioRecord.setGououtPhoto(picture);
                    ioRecord.setExitId(parkingIo.getParkingIoId());
                    ioRecordBiz.saveOrUpdate(ioRecord);
                    //??????????????????????????????????????????????????????????????????
                    String longTime = DateUtil.getDateDiff(order.getBeginDate(), DateUtil.getDateTime());
                    order.setParkingLongTime(longTime);   //??????????????????
                    order.setRealMoney(money);
                    order.setEndDate(DateUtil.getDateTime());
                    parkingOrdersBiz.saveOrUpdate(order);
                    log.info("  ---------------------------------??????????????????????????????????????????--------------------------------");
                    params.put("msg","???????????? ????????????,"+plate.getCarNumber()+","+typeMessage+"?????????"+order.getRealMoney()+"???");
                    RobotUtil.screenMessage(params,parkingIo);
                    RobotUtil.videoMessage(params,parkingIo);
                    Thread.sleep(5000);
                    Map<String,String> params3=new HashMap<>();
                    String url=combinePayBiz.qrCodeAliPay(order);
                    params3.put("payCode",url);
                    params3.put("payNum",order.getOrderNum());
                    params3.put("payWay","???????????????????????????");
                    params3.put("txtMsg",plate.getCarNumber()+","+order.getRealMoney()+"???,"+order.getParkingLongTime()+",?????????");
                    RobotUtil.payMsg(params3,parkingIo);
                    //???vip????????????????????????????????????????????????????????????
                    //??????????????????
                    //operateTollScreen(parkingIo.getParkingIoId(), plate, BaseConstants.admin_status.NORMAL_APPEARANCE, DevConstants.toolSrceen.CASUAL_USER,money);
                    //???????????????
                    publishMsgBiz.publishExitInformation(parking.getParkingId(), order, plate, ioRecord,parkingIo);
                    return ;
                }
            }else if(money.compareTo(BigDecimal.valueOf(0))>0){
                ioRecord.setAttr2(fragmentPicure);
                ioRecord.setGououtPhoto(picture);
                ioRecord.setExitId(parkingIo.getParkingIoId());
                ioRecordBiz.saveOrUpdate(ioRecord);
                log.info("  ---------------------------------??????????????????,??????????????????????????????????????????--------------------------------");
                operateTollScreen(parkingIo.getParkingIoId(), plate, BaseConstants.admin_status.NORMAL_APPEARANCE, DevConstants.toolSrceen.CASUAL_USER,money);
                //??????????????????????????????????????????????????????????????????
                String longTime = DateUtil.getDateDiff(order.getBeginDate(), DateUtil.getDateTime());
                order.setParkingLongTime(longTime);   //??????????????????
                order.setRealMoney(money);
                order.setEndDate(DateUtil.getDateTime());
                parkingOrdersBiz.saveOrUpdate(order);
                Map<String,String> params3=new HashMap<>();
                params.put("msg","???????????? ????????????,"+plate.getCarNumber()+","+typeMessage+"?????????"+order.getRealMoney()+"???");
                RobotUtil.screenMessage(params,parkingIo);
                RobotUtil.videoMessage(params,parkingIo);
                Thread.sleep(5000);
                String url=combinePayBiz.qrCodeAliPay(order);
                params3.put("payCode",url);
                params3.put("payNum",order.getOrderNum());
                params3.put("payWay","???????????????????????????");
                params3.put("txtMsg",plate.getCarNumber()+","+order.getRealMoney()+"???,"+order.getParkingLongTime()+",?????????");
                RobotUtil.payMsg(params3,parkingIo);
                //???????????????
                publishMsgBiz.publishExitInformation(parking.getParkingId(), order, plate, ioRecord,parkingIo);
                return;
            }
        }
        order.setEndDate(DateUtil.getDateTime());
        order.setOrderStatus(BaseConstants.OrderStatus.complete);  //???????????????????????????
        String longTime = DateUtil.getDateDiff(order.getBeginDate(), DateUtil.getDateTime());
        order.setParkingLongTime(longTime);   //??????????????????
        order.setPosition(BaseConstants.Position.leave);
        order.setPayStatus(BaseConstants.status.success);
        ordersBiz.saveOrUpdate(order);
        long time = DateUtil.getDiffTimeStamp(order.getChargeDate(),DateUtil.getDateTime());
        //??????15????????????vip?????????????????????????????? ???????????????????????????
        Map<String, String> dictMap =dictFeign.getDictValues("parking_config");
        long expCommon=Long.valueOf(dictMap.get("exp_common"));
        if(time > expCommon/1000)
        {
            ParkingOrders orders = new ParkingOrders();
            orders.setBeginDate(order.getChargeDate());
            orders.setChargeRulesTypeId(parking.getChargeRuleId());
            orders.setParkingId(parkingId);
            orders.setPlaId(plate.getPlaId());
            orders.setPosition(BaseConstants.Position.admission);
            if (plate.getUserId() != null) {
                orders.setUserId(plate.getUserId());
            }
            orders.setOrderStatus(BaseConstants.OrderStatus.running);
            orders.setOrderType(order.getOrderType());
            orders.setParkingBusType(parking.getParkingBusType());
            orders.setLpId(ioRecord.getLrId());
            orders.setOrderNum(DateUtil.getOrderNum());
            orders.setOrderNumMd5(MD5.MD5(orders.getOrderNum()));
            orders.setRealMoney(money);
            orders.setParkingId(parkingId);
            if(type==1){
                orders.setCreateOrderType(BaseConstants.createOrderType.free_automatic);
            }else {
                orders.setCreateOrderType(BaseConstants.createOrderType.machine);
            }
            orders.setPayStatus(BaseConstants.status.fail);
            orders.setParentId(order.getOrderId());
            ordersBiz.saveOrUpdate(orders);
            //operateTollScreen(parkingIo.getParkingIoId(), plate, BaseConstants.admin_status.OVER_TIME, DevConstants.toolSrceen.CASUAL_USER,money);
            return;
        }
        if (ioRecord != null) {
            ioRecord.setAttr2(fragmentPicure);
            ioRecord.setGououtDate(DateUtil.getDateTime());
            ioRecord.setGououtPhoto(picture);
            ioRecord.setExitId(parkingIo.getParkingIoId());
            ioRecordBiz.saveOrUpdate(ioRecord);
        }

        log.info("  ---------------------------------????????????--------------------------------");
        params.put("msg","???????????? ????????????,"+plate.getCarNumber()+","+typeMessage+"?????????"+order.getRealMoney()+"???");
        RobotUtil.screenMessage(params,parkingIo);
        RobotUtil.videoMessage(params,parkingIo);
        Thread.sleep(5000);
        Map maps=new HashMap();
        maps.put("payStatus","1");
        maps.put("payNum",order.getOrderNum());
        maps.put("screenMsg","??????????????????????????????????????????????????????");
        maps.put("invoiceURL","http://www.yx-sz.cn/");
        RobotUtil.payResult(maps,parkingIo);
        //OpereateAllDev(parkingIo.getParkingIoId(), plate, parkingId, ioRecord, BaseConstants.admin_status.NORMAL_APPEARANCE, order.getOrderType(),money);
        //???????????????????????????
        if(plate.getUserId() != null) {
            publishMsgBiz.publishConsumptionInformation(plate.getUserId(), money, longTime,parking);
        }
        //???????????????
        publishMsgBiz.publishExitInformation(parking.getParkingId(), order, plate, ioRecord,parkingIo);
    }
    private IoRecord QueryIorecord(String parkingId, String plaId) {
        Example ioRecordEx = new Example(IoRecord.class);
        ioRecordEx.createCriteria().andEqualTo("parkingId", parkingId).andEqualTo("plateId", plaId).andIsNull("gououtDate");
        List<IoRecord> ioRecords = ioRecordBiz.selectByExample(ioRecordEx);
        if (ioRecords.size() != 0) {
            IoRecord ioRecord = ioRecords.get(0);
            return ioRecord;
        } else {
            return null;
        }
    }

    /**
     * ????????????????????????
     *
     * @param color     ????????????
     * @param car_plate ????????????
     * @param picture   ????????????
     * @param parkingIo ???????????????
     * @param plate     ????????????
     * @param parkingId ?????????id
     */
    private void EntranceBusinessCannel(String color, String car_plate, String picture, ParkingIo parkingIo, Plate plate, String parkingId,String fragmentPicure,int type,Set set) throws Exception {
        log.info(color + car_plate + "??????");
        String plateId = plate.getPlaId();
        String typeMessage="?????????";
        IoRecord ioRecords = QueryIorecord(parkingId, plate.getPlaId());
        ParkingOrders parkingOrderEx = new ParkingOrders();
        //??????????????????????????????????????????????????????????????????????????????
        parkingOrderEx.setPlaId(plate.getPlaId());
        parkingOrderEx.setOrderStatus(BaseConstants.OrderStatus.running);
        ParkingOrders parkingOrders = ordersBiz.selectOne(parkingOrderEx);

        Map<String,String> params=new HashMap<>();
        if (parkingOrders != null) {
            if (!parkingOrders.getParkingId().equals(parkingId) && ! parkingOrders.getOrderType().equals(BaseConstants.OrderType.vip)) {
                log.info("---------------------------------??????????????????????????????????????????--------------------------------");
                params.put("msg","??????????????????????????????????????????");
                RobotUtil.screenMessage(params,parkingIo);
                RobotUtil.videoMessage(params,parkingIo);
                //operateTollScreen(parkingIo.getParkingIoId(), plate, BaseConstants.admin_status.NORMAL_ADMISSION, DevConstants.toolSrceen.CAR_INED,new BigDecimal(0));
                return;
            }
            if (ioRecords != null) {
                if (parkingOrders.getPosition().equals((BaseConstants.Position.admission))) {
                    log.info("---------------------------------?????????????????????,??????????????????--------------------------------");
                    params.put("msg","?????????????????????,??????????????????");
                    RobotUtil.screenMessage(params,parkingIo);
                    RobotUtil.videoMessage(params,parkingIo);
                    // ???????????????
                    //operateTollScreen(parkingIo.getParkingIoId(), plate, BaseConstants.admin_status.NORMAL_ADMISSION, DevConstants.toolSrceen.CAR_INED,new BigDecimal(0));
                    return;
                }
                return;
            }
            //vip????????????????????????
            if (parkingOrders.getOrderType().equals(BaseConstants.OrderType.vip)) {
                parkingOrders.setPosition(BaseConstants.Position.admission);
            }
        }
        //??????????????????
        ReservatRecord reservatRecord = reservatRecordMapper.selectReservatRecord(plateId, BaseConstants.proceStatus.running, parkingId);
        ParkingOrders orders = new ParkingOrders();
        orders.setPayStatus(BaseConstants.status.fail);
        orders.setOrderId(StringUtil.uuid());
        if (parkingOrders != null) {
            orders = parkingOrders;
            orders.setBeginDate(DateUtil.dateTimeToStr(new Date()));
        }
        ParkingSpace space=null;
        if (reservatRecord != null) {
             space = parkingSpaceBiz.selectById(reservatRecord.getSpaceId());
              orders.setReverseDate(reservatRecord.getBeginDate());
            //????????????
            if (space.getSpaceType().equals(BaseConstants.SpaceType.private1)) {
                orders.setOrderType(BaseConstants.OrderType.shared_lot);
                orders.setSpaceId(space.getSpaceId());
                orders.setUserId(plate.getUserId());
            } else if (space.getSpaceType().equals(BaseConstants.SpaceType.common)) {
                orders.setOrderType(BaseConstants.OrderType.common);
            } else if (space.getSpaceType().equals(BaseConstants.SpaceType.vip)) {
                typeMessage="vip??????";
                orders.setOrderType(BaseConstants.OrderType.vip);
                orders.setSpaceId(space.getSpaceId());
            }
            UserParkingSpace userParkingSpace=new UserParkingSpace();
            userParkingSpace.setSpaceId(space.getSpaceId());
            userParkingSpace.setAttr1(BaseConstants.enabledFlag.y);
            UserParkingSpace userParkingSpace1=userParkingSpaceBiz.selectOne(userParkingSpace);
            if(userParkingSpace1!=null){
                orders.setPrivateUserId(userParkingSpace1.getUserId());
            }
            reservatRecord.setEndDate(DateUtil.getDateTime());
            reservatRecord.setReservatState(BaseConstants.proceStatus.complete);
            reservatRecordBiz.saveOrUpdate(reservatRecord);
        } else {
            Example nvExample = new Example(UserMoncards.class);
            nvExample.createCriteria().andEqualTo("plateId", plate.getPlaId()).andEqualTo("parkingId", parkingId).andGreaterThan("endDate", DateUtil.getDateTime());
            List<UserMoncards> userMoncards = userMoncardsBiz.selectByExample(nvExample);
            //??????
            if (userMoncards.size() != 0) {
                typeMessage="????????????";
                orders.setOrderType(BaseConstants.OrderType.monthCard);
            }else if(type==1){
                orders.setOrderType(BaseConstants.OrderType.free_admission);
            } else {
                orders.setOrderType(BaseConstants.OrderType.common);
            }
        }
        Parking parking = parkingBiz.selectById(parkingId);
        parking=parkingSpaceBiz.querySpaceTotalCount(parking);
        long spaceCount=Long.valueOf(parking.getTotalNum());
        //????????????
        /*ParkingSpace parkingSpaceEx = new ParkingSpace();
        parkingSpaceEx.setSpaceType(BaseConstants.SpaceType.common);
        parkingSpaceEx.setLotType(BaseConstants.enabledFlag.n);
        parkingSpaceEx.setParkingId(parkingId);
        parkingSpaceEx.setEnabledFlag(BaseConstants.enabledFlag.y);
        long spaceCount = parkingSpaceBiz.selectCount(parkingSpaceEx);*/
        Example example=new tk.mybatis.mapper.entity.Example(IoRecord.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("parkingId",parking.getParkingId());
        criteria.andIsNull("gououtDate");
        long parkingCount=ioRecordBiz.selectCountByExample(example);
        spaceCount=spaceCount-parkingCount;
        //??????????????????????????????????????????????????????
        if (orders.getOrderType().equals(BaseConstants.OrderType.common)) {
            if (spaceCount == 0) {
                params.put("msg","????????????");
                RobotUtil.screenMessage(params,parkingIo);
                RobotUtil.videoMessage(params,parkingIo);
                //operateTollScreen(parkingIo.getParkingIoId(), plate, BaseConstants.admin_status.OVER_SPACE, DevConstants.toolSrceen.SPACE_FULL,new BigDecimal(0));
                log.info(" ---------------------------------????????????--------------------------------");
                set.remove(car_plate);
                return;
            }

        }
        String uuid = StringUtil.uuid();
        orders.setBeginDate(DateUtil.dateTimeToStr(new Date()));
        orders.setChargeRulesTypeId(parking.getChargeRuleId());
        orders.setParkingId(parkingId);
        orders.setPlaId(plate.getPlaId());
        orders.setChargeDate(DateUtil.format(new Date()));
        orders.setPosition(BaseConstants.Position.admission);
        if (plate.getUserId() != null) {
            typeMessage="????????????";
            orders.setUserId(plate.getUserId());
        }
        orders.setOrderStatus(BaseConstants.OrderStatus.running);
        orders.setParkingBusType(parking.getParkingBusType());
        orders.setLpId(uuid);
        orders.setOrderNum(DateUtil.getOrderNum());
        orders.setOrderNumMd5(MD5.MD5(orders.getOrderNum()));
        if(!orders.getOrderType().equals(BaseConstants.OrderType.vip)) {
            orders.setRealMoney(new BigDecimal(0));
        }
        orders.setParkingId(parkingId);
        if(type==1){
            orders.setCreateOrderType(BaseConstants.createOrderType.free_automatic);
        }else {
            orders.setCreateOrderType(BaseConstants.createOrderType.machine);
        }
        orders.setPayStatus(BaseConstants.status.fail);
        ordersBiz.saveOrUpdate(orders);
        //?????????????????????
        IoRecord ioRecord = new IoRecord();
        ioRecord.setLrId(uuid);
        ioRecord.setAccDate(DateUtil.dateTimeToStr(new Date()));
        ioRecord.setAccinId(parkingIo.getParkingIoId());
        ioRecord.setAccPhoto(picture);
        ioRecord.setParkingId(parkingId);
        ioRecord.setPlateId(plate.getPlaId());
        ioRecord.setAttr1(fragmentPicure);
        ioRecordBiz.saveOrUpdate(ioRecord);
        //???????????????????????????
        params.put("msg","????????????????????????,"+plate.getCarNumber()+","+typeMessage);
        RobotUtil.screenMessage(params,parkingIo);
        RobotUtil.videoMessage(params,parkingIo);
        //OpereateAllDev(parkingIo.getParkingIoId(), plate, parkingId, ioRecords, BaseConstants.admin_status.NORMAL_ADMISSION, orders.getOrderType(),orders.getRealMoney());
        //????????????
        publishMsgBiz.publishIoBusinessInformation(plate,space,parkingIo,orders);
        //???????????????
        publishMsgBiz.entrancePush(parking.getParkingId());
    }

    public void OpereateAllDev(String parkingIoId, Plate plate, String parkingId, IoRecord ioRecord, String ioType, String orderType,BigDecimal money) throws Exception {
        List<Onerankdev> onerankdevList = onerankdevMapper.queryBindOnerankdevByIoidAndDevType(parkingIoId, null);
        for (Onerankdev onerankdev : onerankdevList) {
            byte[] bt = new byte[0];
            //????????????
            if (onerankdev.getOnerankdevType().equals(String.valueOf(BaseConstants.devType.ROAD_GATE))) {
                bt = c_spd.OperateRoadGate1(1);
                operateDevByPost(onerankdev, bt);
            } else if (onerankdev.getOnerankdevType().equals(String.valueOf(BaseConstants.devType.TOOL_SCREEN))) {
                Timestamp beginDate = DateUtil.getCurrTimestamp();
                String stime = getTimes(beginDate);
                //money = orderRepository.findSumMoneyByLmId(String.valueOf(ioRecord.getLpId()), dataOwnedSys, "Y");
                money = money.setScale(2, BigDecimal.ROUND_HALF_DOWN);
                bt = c_spd.OperateToolScreen1(plate.getCarNumber(), ioType, orderType, String.valueOf(money), stime, 0);
                operateDevByPost(onerankdev, bt);
            }
        }
    }

    private String getTimes(Timestamp beginDate) {
        String stime = "000000";
        if (beginDate != null) {
            long time = System.currentTimeMillis() - beginDate.getTime();
            long h = (time) / (60 * 60 * 1000);
            long m = (time - h * 60 * 60 * 1000) / (60 * 1000);
            if (m < 10) {
                stime = h + "0" + m;
            } else {
                stime = h + m + "";
            }
            int length = stime.length();
            for (int i = 0; i < 6 - length; i++) {
                stime = "0" + stime;
            }
            log.info("??????-------???" + stime);
        }
        return stime;
    }

    /**
     * ???????????????
     *
     * @throws UnsupportedEncodingException
     */
    private void operateTollScreen(String parkingIoId, Plate plate, String ioType, int accessType,BigDecimal money) throws UnsupportedEncodingException {
        List<Onerankdev> onerankdevs = onerankdevMapper.queryBindOnerankdevByIoidAndDevType(parkingIoId, BaseConstants.devType.TOOL_SCREEN);
        if (onerankdevs.size() != 0) {
            Onerankdev onerankdev = onerankdevs.get(0);
            money = money.setScale(2, BigDecimal.ROUND_HALF_DOWN);
            byte[] bt = c_spd.OperateToolScreen1(plate.getCarNumber(), ioType, BaseConstants.OrderType.common, String.valueOf(money), "000000", accessType);
            operateDevByPost(onerankdev, bt);
        }
    }

    /**
     * ???????????????
     *
     * @param parkingId
     * @return
     * @throws Exception //
     */
    public void CarnelBootScreen(String parkingId) throws Exception {
        //???????????????
        byte[] bt = new byte[0];
        Parking parking=parkingBiz.selectById(parkingId);
        parking=parkingSpaceBiz.querySpaceTotalCount(parking);
        long spaceCount=Long.valueOf(parking.getTotalNum());
        //????????????
        /*ParkingSpace parkingSpaceEx = new ParkingSpace();
        parkingSpaceEx.setSpaceType(BaseConstants.SpaceType.common);
        parkingSpaceEx.setLotType(BaseConstants.enabledFlag.n);
        parkingSpaceEx.setParkingId(parkingId);
        parkingSpaceEx.setEnabledFlag(BaseConstants.enabledFlag.y);
        long spaceCount = parkingSpaceBiz.selectCount(parkingSpaceEx);*/
        Example example=new tk.mybatis.mapper.entity.Example(IoRecord.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("parkingId",parking.getParkingId());
        criteria.andIsNull("gououtDate");
        long parkingCount=ioRecordBiz.selectCountByExample(example);
        spaceCount=spaceCount-parkingCount;
        List<Onerankdev> onerankdevList = onerankdevMapper.queryBindOnerankdevByParkingIdAndDevType(parkingId, BaseConstants.devType.BOOT_SCREEN);
        for (Onerankdev onerankdev : onerankdevList) {
            bt = c_spd.OperateBootSrceen1(spaceCount, onerankdev.getOnerankdevTerminId());
            operateDevByPosts(onerankdev, bt);
        }
    }
    public String CarLockCannel(String spaceStatus, String spaceId) {
        Onerankdev onerankdev = onerankdevMapper.queryOnerankdevBySpaceidAndDevType(spaceId,BaseConstants.devType.CAR_LOCK);
        if(onerankdev != null) {
            byte[] bt = c_spd.OperateCarLock1(spaceStatus);
            log.info("---------------------------------------?????????????????????------------------------------------------");
            return operateDevByPost(onerankdev, bt);
        }
        return "404";
    }

    public String operateDevByPost(Onerankdev onerankdev, byte[] bt) {
        String packType = "U";
        if (onerankdev.getOnerankdevType().equals(String.valueOf(BaseConstants.devType.TOOL_SCREEN))) {
            packType = "M";

        }
        String requestBody = "{\"method\":\"sgmCommand\",\"params\":\"{\\\"id\\\":\\\"" + onerankdev.getOnerankdevTerminId() + "\\\",\\\"type\\\":\\\"sf\\\",\\\"quatity\\\":\\\"10\\\",\\\"packetType\\\":\\\"" + packType + "\\\",\\\"data\\\":\\\"" + c_spd.getByteStr(bt).replace(" ", "") + "\\\"}\"} ";
        log.info(requestBody);
        log.info(api_url + rpc_url + onerankdev.getOnerankdevDevSn());
        String rs = httpUtil.sendPost(api_url + rpc_url + onerankdev.getOnerankdevDevSn(), requestBody);
        return parseCode(rs);
    }

    public String operateDevByPosts(Onerankdev onerankdev, byte[] bt) {
        String packType = "U";
        if (onerankdev.getOnerankdevType().equals(String.valueOf(BaseConstants.devType.TOOL_SCREEN))) {
            packType = "M";
        }
        String requestBody = "{\"method\":\"sgmCommand\",\"params\":\"{\\\"id\\\":\\\"" + onerankdev.getOnerankdevTerminId() + "\\\",\\\"type\\\":\\\"sf\\\",\\\"quatity\\\":\\\"10\\\",\\\"packetType\\\":\\\"" + packType + "\\\",\\\"data\\\":\\\"" + c_spd.getByteStr(bt).replace(" ", "") + "\\\"}\"} ";
        log.info(requestBody);
        String devSn=onerankdev.getOnerankdevDevSn();
        log.info(api_url + rpc_url + devSn);
        String rs = httpUtil.sendPost(api_url + rpc_url + devSn, requestBody);
        return parseCode(rs);
    }

    private static String parseCode(String jsStr){
        String code = "404";
        if(StringUtils.isNotEmpty(jsStr)){
            JSONObject jsonObject = JSONObject.parseObject(jsStr);
            String data = jsonObject.getString("data");

            JSONObject jsonObjectData = JSONObject.parseObject(data);
            if(jsonObjectData!=null) {
                String result = jsonObjectData.getString("result");
                if ("0".equals(result) || "true".equals(result) ) {
                    code = "200";
                }
            }else{
                code = jsonObject.getString("code");
            }
        }
        return code;
    }
    private String getCarTypeByPlateColor(String plate) {
        if ("???".equals(plate)) {
            return "1";
        } else {
            return "2";
        }
    }

    public boolean DetermWhetherOperatLock(UserParkingSpace userParkingSpace) {
        int date = DateUtil.getWeekNumOfDate(null);
        int length = userParkingSpace.getRentalPeriod().split(",").length;
        String rentalPeriod = userParkingSpace.getRentalPeriod();
        boolean flag = false;
        for(int i = 0 ;i<length;i++)
        {
            String str = rentalPeriod.split(",")[i];
            if(Integer.valueOf(str)==date)
            {
                flag = true;
            }
        }
        long currHhmm = Long.parseLong(DateUtil.format(new Date(),"HH:mm").replace(":",""));
        long beginDate = Long.parseLong(userParkingSpace.getBeginDate().replace(":",""));
        long endDate = Long.parseLong(userParkingSpace.getEndDate().replace(":",""));
        if(currHhmm >= beginDate && currHhmm<= endDate && flag)
        {
            flag = true;
        }else
        {
            flag = false;
        }
        return flag;

    }

    public ObjectRestResponse<Object> placeVipReservatOrder(ParkingOrders parkingOrders) {
        AppUser appUser = appUserBiz.selectById(parkingOrders.getUserId());
        if(!appUser.getUserType().equals(BaseConstants.user_type.vip))
        {
            return new  ObjectRestResponse<Object>().BaseResponse(BaseConstants.StateConstates.SPACE_EXCEPTION_CODE,BaseConstants.StateConstates.SPACE_EXCEPTION_MSG);
        }
        //????????????????????????
        ReservatRecord example = new ReservatRecord();
        example.setReservatState(BaseConstants.proceStatus.running);
        example.setSpaceId(parkingOrders.getSpaceId());
        //??????????????????????????????
        ReservatRecord reservatRecord1 = reservatRecordBiz.selectOne(example);
        if(reservatRecord1 != null)
        {
            return new ObjectRestResponse<AppUser>().BaseResponse(BaseConstants.StateConstates.SPACE_BOOKED_CODE,BaseConstants.StateConstates.SPACE_BOOKED_MSG);
        }
        //???????????????????????????????????????
        ParkingOrders od1Example =  new ParkingOrders();
        od1Example.setUserId(parkingOrders.getUserId());
        od1Example.setOrderStatus(BaseConstants.OrderStatus.running);
        ParkingOrders parkingOrders1 = parkingOrdersBiz.selectOne(od1Example);
        if(parkingOrders1 != null)
        {
            return new ObjectRestResponse<AppUser>().BaseResponse(BaseConstants.StateConstates.CARNUM_IN_ORDER_CODE,BaseConstants.StateConstates.CARNUM_IN_ORDER_MSG);
        }
        ParkingSpace parkingSpace = parkingSpaceBiz.selectById(parkingOrders.getSpaceId());
        if(parkingSpace.getLotType().equals(BaseConstants.enabledFlag.y))
        {
            return new ObjectRestResponse<AppUser>().BaseResponse(BaseConstants.StateConstates.SPACE_BOOKED_CODE,BaseConstants.StateConstates.SPACE_BOOKED_MSG);
        }
        if(!parkingSpace.getSpaceType().equals(BaseConstants.SpaceType.vip))
        {
            return new ObjectRestResponse<AppUser>().BaseResponse(BaseConstants.StateConstates.MESSAGE_FAIL_CODE,BaseConstants.StateConstates.MESSAGE_FAIL_MSG);
        }
        String dateTime = DateUtil.getDateTime();
        BigDecimal currMoney = appUser.getMoney();
        Parking parking = parkingBiz.selectById(parkingOrders.getParkingId());
        ParkingOrders order = new ParkingOrders();
        order.setParkingId(parking.getParkingId());
        order.setChargeDate(DateUtil.dateTimeToStr(DateUtil.addHour(DateUtil.strToDate(order.getBeginDate(), DateUtil.YYYY_MM_DD_HH_MM_SS),1)));
        order.setChargeRulesTypeId(parking.getChargeRuleId());
        order.setCreateOrderType(BaseConstants.createOrderType.artificial);
        order.setOrderNum(DateUtil.getOrderNum());
        order.setOrderNumMd5(MD5.MD5(order.getOrderNum()));
        order.setOrderStatus(BaseConstants.OrderStatus.running);
        order.setOrderType(BaseConstants.OrderType.vip);
        order.setParkingBusType(parking.getParkingBusType());
        order.setPayStatus(BaseConstants.status.success);
        order.setPayType(BaseConstants.payType.balance);
        order.setPlaId(parkingOrders.getPlaId());
        order.setPosition(BaseConstants.Position.unapproach);
        order.setBeginDate(parkingOrders.getBeginDate());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("parkingId", parkingOrders.getParkingId());
        Plate plate = plateBiz.selectById(parkingOrders.getPlaId());
        /*String carType="";
        if(plate.getCarType().equals("2")){
            carType="auto";
        }else if(plate.getCarType().equals("1")){
            carType="truck";
        }*/
        map.put("carType",plate.getCarType() );
        map.put("chargeRuleType", parking.getChargeRuleId());
        map.put("beginDate", parkingOrders.getBeginDate());
        map.put("type", "vipSchedule");
        map.put("endDate", DateUtil.dateTimeToStr(DateUtil.addHour(DateUtil.strToDate(order.getBeginDate(), DateUtil.YYYY_MM_DD_HH_MM_SS),1)));

        BigDecimal money = chargeRulesTypeFeign.queryCostByIdAndParkingId(map).getData();
        if (currMoney.compareTo(money) == 1 || currMoney.compareTo(money) == 0) {   //????????????????????????
            currMoney = currMoney.subtract(money); //??????????????????
            //???????????????????????????
            appUser.setMoney(currMoney);
            parking.setParkingRevenue(parking.getParkingRevenue().add(money));
            appUserBiz.saveOrUpdate(appUser);
            parkingBiz.saveOrUpdate(parking);
        }else
        {
            return new  ObjectRestResponse<Object>().BaseResponse(BaseConstants.StateConstates.BALANCE_INSUFFICIENT_ID,BaseConstants.StateConstates.BALANCE_INSUFFICIENT_MSG);
        }
        order.setOrderId(StringUtil.uuid());
        order.setRealMoney(money);
        order.setReverseDate(parkingOrders.getBeginDate());
        order.setSpaceId(parkingOrders.getSpaceId());
        order.setUserId(parkingOrders.getUserId());
        this.insertSelective(order);
        ReservatRecord reservatRecord = new ReservatRecord();
        reservatRecord.setBeginDate(DateUtil.getDateTime());
        reservatRecord.setUserId(appUser.getId());
        reservatRecord.setEndDate(dateTime);
        reservatRecord.setReservatState(BaseConstants.proceStatus.running);
        reservatRecord.setSpaceId(order.getSpaceId());
        reservatRecord.setReservatDate(parkingOrders.getBeginDate());
        reservatRecord.setParkingId(parking.getParkingId());
        reservatRecord.setPlaId(order.getPlaId());
        reservatRecord.setId(StringUtil.uuid());
        reservatRecordBiz.insertSelective(reservatRecord);
        //???????????????????????????
        long time = 0;
        //vip????????????????????????????????????????????????????????????
        if(order != null) {
            time = DateUtil.getDiffTimeStamp(dateTime,order.getBeginDate())*1000 ;
        }
        HashMap<String,Object> reservatRecordMap = new  HashMap<String,Object>();
        reservatRecordMap.put("type","reservatRecord");
        reservatRecordMap.put("object",reservatRecord);
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(reservatRecordMap);
        rabbitTemplate.convertAndSend(QueueConfig.DELAY_QUEUE_PER_MESSAGE_TTL_NAME,
                (Object) (jsonObject.toString()), new ExpirationMessagePostProcessor(time));
        SendMsg.scheduledTimeout(appUser.getMobile(),parking.getParkingName(),parkingSpace.getSpaceNum(),parkingOrders.getBeginDate());
        parkingOrdersBiz.PutOrderIntoDelayQueue(order);
        reservatRecord.setAttr1(String.valueOf(time));
        HashMap<String,Object> objectHashMap = new HashMap<String,Object>();
        objectHashMap.put("order",order);
        objectHashMap.put("reservatRecord",reservatRecord);
        return new ObjectRestResponse<Object>().data(objectHashMap);

    }
    public TableResultResponse queryPrivateUserAndParking(ParkingOrders parkingOrders) throws InvocationTargetException, ExecutionException, IllegalAccessException, NoSuchMethodException, IOException {
        List<ParkingOrders> parkingOrders1 = parkingOrdersMapper.queryPrivateUserAndParking(parkingOrders.getPrivateUserId(),parkingOrders.getParkingId());
        String parkingId="";
        if(StringUtil.isNotNull(parkingOrders.getParkingId())){
            parkingId=parkingOrders.getParkingId();
        }
        TableResultResponse tableResultResponse= userParkingSpaceBiz.queryParkingByUserId(parkingOrders.getPrivateUserId(),parkingId);
        List<UserParkingSpaceVo> userParkingSpaces = tableResultResponse.getData().getRows();
        for(UserParkingSpaceVo userParkingSpace : userParkingSpaces)
        {
            userParkingSpace.setAttr1(String.valueOf(0));
            for(ParkingOrders order : parkingOrders1)
            {
                if(order.getSpaceId().equals(userParkingSpace.getSpaceId()))
                {
                    userParkingSpace.setAttr1(order.getAttr1());
                }
            }
        }

        return new TableResultResponse(userParkingSpaces.size(),userParkingSpaces);
    }

    public TableResultPageResponse queryPrivateUserSpaceOrder(Map<String, Object> params) {
        String spaceId=String.valueOf(params.get("spaceId"));
        int page=Integer.valueOf(String.valueOf(params.get("page")));
        int limit=Integer.valueOf(String.valueOf(params.get("limit")));
        String [] result = spaceId.split(",");
        int count=parkingOrdersMapper.queryPrivateUserSpaceOrderCount(result);
        List<ParkingOrders> parkingOrders1 = parkingOrdersMapper.queryPrivateUserSpaceOrder(result,page-1,limit);
        List<ParkingOrdersVo> parkingOrdersVoList= parkingOrdersRSBiz.parkingOrderListMap(parkingOrders1);
        return new TableResultPageResponse(count,parkingOrdersVoList,page,limit);
    }
}
