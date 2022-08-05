package com.smart.iot.parking.biz;

import com.alibaba.fastjson.JSONObject;
import com.smart.iot.parking.constant.BaseConstants;
import com.smart.iot.parking.entity.*;
import com.smart.iot.parking.feign.ChargeRulesTypeFeign;
import com.smart.iot.parking.feign.DictFeign;
import com.smart.iot.parking.mapper.ParkingOrdersMapper;
import com.smart.iot.parking.mapper.UserMoncardsMapper;
import com.smart.iot.parking.rabbitmq.ExpirationMessagePostProcessor;
import com.smart.iot.parking.rabbitmq.QueueConfig;
import com.smart.iot.parking.srever.TablePageResultParser;
import com.smart.iot.parking.utils.*;
import com.smart.iot.parking.vo.ParkingOrdersVo;
import com.smart.iot.roadside.biz.ParkingOrdersRSBiz;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yuncitys.smart.merge.annonation.MergeResult;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import com.yuncitys.smart.parking.common.msg.BaseResponse;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.msg.TableResultPageResponse;
import com.yuncitys.smart.parking.common.util.DateUtil;
import com.yuncitys.smart.parking.common.util.Query;
import com.yuncitys.smart.parking.common.util.StringUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

/**
 * 订单表
 *
 * @author Mr.AG
 * @email
 *@version 2022-08-07 14:53:11
 */
@Service
@Transactional
@Slf4j
public class ParkingOrdersBiz extends BusinessBiz<ParkingOrdersMapper,ParkingOrders> {
    @Autowired
    public  ParkingOrdersBiz ordersBiz;
    @Autowired
    public AppUserBiz appUserBiz;
    @Autowired
    public DictFeign dictFeign;
    @Value("${sysPath}")
    private String sysPath;

    @Value("${alipay.ali_app_partner}")
    private String ali_app_partner;
    @Value("${alipay.ali_app_seller_id}")
    private  String ali_app_seller_id;
    @Value("${alipay.ali_notify_url}")
    private  String ali_notify_url;
    @Value("${alipay.ali_app_id}")
    private  String ali_app_id;
    @Value("${alipay.ali_private_sign}")
    private  String ali_private_sign;

    @Value("${wcpay.wc_app_appid}")
    private   String wc_app_appid;
    @Value("${wcpay.wc_app_mchid}")
    private   String wc_app_mchid;
    @Value("${wcpay.wc_app_apikey}")
    private   String wc_app_apikey;
    @Value("${wcpay.wc_app_notify_url}")
    private   String wc_app_notify_url;

    @Value("${rpc.api_url}")
    private String api_url;
    @Value("${rpc.rpc_url}")
    private  String rpc_url;
    @Value("${rpc.get_devparams_url}")
    private  String get_devparams_url;
    @Value("${rpc.get_token_url}")
    private  String get_token_url;
    @Value("${rpc.username}")
    private  String username;
    @Value("${rpc.password}")
    private  String password;

    @Autowired
    public ChargeRulesTypeFeign chargeRulesTypeFeign;
    @Autowired
    public RabbitTemplate rabbitTemplate;
    @Autowired
    private UserMoncardsMapper userMoncards;
    @Autowired
    public WXapi wXapi;
    @Autowired
    public ParkingOrdersRSBiz parkingOrdersRSBiz;
    SpiderGateWay c_spd =  new SpiderGateWay();
    @MergeResult(resultParser = TablePageResultParser.class)
    @Override
    public TableResultPageResponse selectByPageQuery(Query query, String beginDate, String endDate)
    {
        TableResultPageResponse<ParkingOrders> tableResultPageResponse=super.selectByPageQuery(query,  beginDate, endDate);
        if(tableResultPageResponse!=null && tableResultPageResponse.getData()!=null){
            List<ParkingOrders> parkingOrdersList=tableResultPageResponse.getData().getRows();
            List<ParkingOrdersVo> parkingOrdersVoList=parkingOrdersRSBiz.parkingOrderListMap(parkingOrdersList);
            long total = tableResultPageResponse.getData().getTotal();
            long offset = tableResultPageResponse.getData().getOffset();
            long limit = tableResultPageResponse.getData().getLimit();
            TableResultPageResponse<ParkingOrdersVo> resultPageResponse=new TableResultPageResponse(total,parkingOrdersVoList,offset,limit);
            return resultPageResponse;
        }
        return tableResultPageResponse;
    }
    public TableResultPageResponse selectByPageQuery2(Query query, String beginDate, String endDate) {
        Class<ParkingOrders> clazz = (Class<ParkingOrders>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        Example example = new Example(clazz);
        Example.Criteria criteria=query2criteria2(query, example);
        if(StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate)) {
            criteria.andBetween("crtTime", beginDate, endDate);
        }
        example.orderBy("crtTime").desc();
        Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
        List<ParkingOrders> list = this.selectByExample(example);
        return new TableResultPageResponse(result.getTotal(), list ,result.getPageNum(),result.getPageSize());
    }
    public Example.Criteria query2criteria2(Query query, Example example) {
        Example.Criteria criteria=null;
        if (query.entrySet().size() > 0) {
            criteria = example.createCriteria();
            for (Map.Entry<String, Object> entry : query.entrySet()) {
                if("orderStatus".equals(entry.getKey()))
                {
                    List<String> a = new ArrayList<String>();
                    List<String> b = new ArrayList<String>();
                    if(entry.getValue().toString().equals(BaseConstants.OrderStatus.running)) {
                        a.add("running");
                        a.add("unpay");
                        criteria.andIn(entry.getKey(),a);
                        b.add(BaseConstants.OrderType.recharge);
                        b.add(BaseConstants.OrderType.vip_charge);
                        criteria.andNotIn("orderType",b);
                    }else if(entry.getValue().toString().equals(BaseConstants.OrderStatus.complete))
                    {
                        a.add("cancel");
                        a.add("exception");
                        a.add("complete");
                        criteria.andIn(entry.getKey(),a);
                    }
                }else
                {
                    criteria.andLike(entry.getKey(), "%" + entry.getValue().toString() + "%");
                }
            }
        }
        return criteria;
    }



    public BigDecimal queryCurrCostMoney(String endTime,Parking parking,Plate plate,ParkingOrders order) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("parkingId", parking.getParkingId());
        /*String carType="";
        if(plate.getCarType().equals("2")){
            carType="auto";
        }else if(plate.getCarType().equals("1")){
            carType="truck";
        }*/
        map.put("carType", plate.getCarType());
        map.put("chargeRuleType", parking.getChargeRuleId());
        map.put("beginDate", order.getBeginDate());
        map.put("endDate", endTime);
        map.put("orderNum", order.getOrderNum());
        BigDecimal money = chargeRulesTypeFeign.queryCostByIdAndParkingId(map).getData();
        return money;
    }
    public void PutOrderIntoDelayQueue(ParkingOrders order) {
        Map<String, String> dictMap =dictFeign.getDictValues("parking_config");
        long expCommon=Long.valueOf(dictMap.get("exp_common"));
//        long time = BaseConstants.SpaceExpirate.expCommon ;
        long time = expCommon;
        log.info("数据推进延迟队列，延迟时间:"+time);
        HashMap<String,Object> ordersMap = new  HashMap<String,Object>();
        ordersMap.put("type","order");
        ordersMap.put("object",order);
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(ordersMap);
        rabbitTemplate.convertAndSend(QueueConfig.DELAY_QUEUE_PER_MESSAGE_TTL_NAME,
                (Object) (jsonObject.toString()), new ExpirationMessagePostProcessor(time));
    }


    /**
     * 充值
     *
     * @param money 充值金额
     */
    @Transactional
    public  BaseResponse recharge(String useId, String money, String payType, String orderType) throws Exception {
        if (useId == null || money == null || payType == null)
        {
            return new BaseResponse(BaseConstants.StateConstates.PARAMETER_IS_EMPTY_ID, BaseConstants.StateConstates.PARAMETER_IS_EMPTY_MSG);
        }
        else
        {
            if(!(orderType.equals(BaseConstants.OrderType.recharge) || orderType.equals(BaseConstants.OrderType.vip_charge))) {
                return new BaseResponse(BaseConstants.StateConstates.PARAM_ERROR_ID, BaseConstants.StateConstates.PARAM_ERROR_MSG);
            }
            AppUser user = appUserBiz.selectById(useId);
            if (user != null)
            {
                String id = String.valueOf(user.getId());
                ParkingOrders chargeOrder = null;
                chargeOrder = new ParkingOrders();
                chargeOrder.setUserId(useId);
                chargeOrder.setOrderStatus(BaseConstants.OrderStatus.unpay);
                chargeOrder.setPayType(payType);
                chargeOrder.setOrderType(orderType);
                Example example=new Example(ParkingOrders.class);
                Example.Criteria criteria=example.createCriteria();
                criteria.andEqualTo("userId",useId);
                criteria.andEqualTo("orderStatus",BaseConstants.OrderStatus.unpay);
                criteria.andEqualTo("payType",payType);
                criteria.andEqualTo("orderType",orderType);
                example.orderBy("crtTime").desc();
                List<ParkingOrders> orderList=ordersBiz.selectByExample(example);
                if(orderList != null && orderList.size()>0)
                {
                    chargeOrder = orderList.get(0);
                }else
                {
                    chargeOrder = new ParkingOrders();
                }
                chargeOrder.setOrderNum(DateUtil.getOrderNum());
                chargeOrder.setOrderNumMd5(MD5.MD5(chargeOrder.getOrderNum()));
                chargeOrder.setUserId(id);
                chargeOrder.setOrderType(orderType);
                chargeOrder.setOrderStatus(BaseConstants.OrderStatus.unpay);
                if(orderType.equals(BaseConstants.OrderType.vip_charge)) {
                    Map<String, String> dictMap = dictFeign.getDictValues("parking_config");
                    money=dictMap.get("vip_charge_price");
                }
                chargeOrder.setRealMoney(new BigDecimal(money));
                chargeOrder.setPayType(payType);
                chargeOrder.setOrderId(StringUtil.uuid());
                ordersBiz.saveOrUpdate(chargeOrder);
                GetPayMent payMent = new GetPayMent();
                if (payType.equals(BaseConstants.payType.alipay))
                {
                    payMent.setInput_charset("utf-8");
                    payMent.setBody("停车充值");
                    payMent.setNotify_url(ali_notify_url);
                    payMent.setOut_trade_no(chargeOrder.getOrderNum());
                    payMent.setPartner(ali_app_partner);
                    payMent.setPayment_type("1");
                    payMent.setSeller_id(ali_app_seller_id);
                    payMent.setService("mobile.parkingpay.pay");
                    payMent.setSign_type("RSA2");
                    payMent.setSubject("停车充值");
                    payMent.setTotal_fee(money);
                    String[] parameters = { "_input_charset=\"" + payMent.getInput_charset() + "\"", "body=\"订单说明\"",
                            "notify_url=\"" + payMent.getNotify_url() + "\"",// 通知地址
                            "out_trade_no=\"" + payMent.getOut_trade_no() + "\"",// 商户内部订单号
                            "partner=\"" + payMent.getPartner() + "\"",// 合作身份者ID（16位）
                            "payment_type=\"1\"",// 固定值
                            "seller_id=\"" + payMent.getSeller_id() + "\"",// 账户邮箱
                            "service=\"" + payMent.getService() + "\"",// 固定值（手机快捷支付）
                            "subject=\"" + payMent.getSubject() + "\"",// 测试
                            "total_fee=\"" + payMent.getTotal_fee() + "\""// 支付金额（元）
                    };
                    StringBuffer sb = new StringBuffer("");
                    for (int i = 0; i < parameters.length; i++)
                    {
                        if (i == (parameters.length - 1))
                        {
                            sb.append(parameters[i]);
                        }
                        else
                        {
                            sb.append(parameters[i] + "&");
                        }
                    }
                    Map<String, String> params = buildOrderParamMap(ali_app_id, payMent);
                    String orderParam = buildOrderParam(params);
                    String sign = getSign(params, ali_private_sign, true);
                    payMent.setSign(sign);
                    payMent.setOrderInfo(orderParam + "&" + sign);
                    return new ObjectRestResponse<>().data(payMent);
                }
                else if (payType.equals(BaseConstants.payType.wechat))
                {
                    Map<String, Object> map = wXapi.weixiPay(chargeOrder.getOrderNum(), chargeOrder.getRealMoney(), "智慧停车");
                    map.put("out_trade_no", chargeOrder.getOrderNum());
                    return new ObjectRestResponse<>().data(map);
                }

            }
            else
            {
                return new ObjectRestResponse<>().data(false);
            }
        }
        return new ObjectRestResponse<>().data(false);
    }
    /**
     * 对支付参数信息进行签名
     *
     * @param map
     *            待签名授权信息
     *
     * @return
     */
    public static String getSign(Map<String, String> map, String rsaKey, boolean rsa2)
    {
        List<String> keys = new ArrayList<String>(map.keySet());
        // key排序
        Collections.sort(keys);

        StringBuilder authInfo = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++)
        {
            String key = keys.get(i);
            String value = map.get(key);
            authInfo.append(buildKeyValue(key, value, false));
            authInfo.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        authInfo.append(buildKeyValue(tailKey, tailValue, false));

        String oriSign = SignUtils.sign(authInfo.toString(), rsaKey, rsa2);
        String encodedSign = "";

        try
        {
            encodedSign = URLEncoder.encode(oriSign, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return "sign=" + encodedSign;
    }
    /**
     * 拼接键值对
     *
     * @param key
     * @param value
     * @param isEncode
     * @return
     */
    private static String buildKeyValue(String key, String value, boolean isEncode)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append("=");
        if (isEncode)
        {
            try
            {
                sb.append(URLEncoder.encode(value, "UTF-8"));
            }
            catch (UnsupportedEncodingException e)
            {
                sb.append(value);
            }
        }
        else
        {
            sb.append(value);
        }
        return sb.toString();
    }
    /**
     * 构造支付订单参数列表
     * @param app_id
     * @param payMent
     * @return
     */
    public static Map<String, String> buildOrderParamMap(String app_id, GetPayMent payMent)
    {
        Map<String, String> keyValues = new HashMap<String, String>();

        keyValues.put("app_id", app_id);

        keyValues.put(
                "biz_content",
                "{\"timeout_express\":\"30m\",\"product_code\":\"QUICK_Mparking_PAY\",\"total_amount\":\""
                        + payMent.getTotal_fee() + "\",\"subject\":\"" + payMent.getSubject() + "\",\"body\":\""
                        + payMent.getBody() + "\",\"out_trade_no\":\"" + payMent.getOut_trade_no() + "\"}");

        keyValues.put("charset", payMent.getInput_charset());

        keyValues.put("method", "alipay.trade.app.pay");

        keyValues.put("sign_type", "RSA2");

        keyValues.put("timestamp", DateUtil.dateTimeToStr(new Date()));
        keyValues.put("notify_url", payMent.getNotify_url());
        keyValues.put("version", "1.0");

        return keyValues;
    }
    /**
     * 构造支付订单参数信息
     *
     * @param map
     *            支付订单参数
     * @return
     */
    public static String buildOrderParam(Map<String, String> map)
    {
        List<String> keys = new ArrayList<String>(map.keySet());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++)
        {
            String key = keys.get(i);
            String value = map.get(key);
            sb.append(buildKeyValue(key, value, true));
            sb.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        sb.append(buildKeyValue(tailKey, tailValue, true));

        return sb.toString();
    }

    public Map pandect(Map params) {
        Map returnMap = Maps.newHashMap();
        Long parkingTimesCount = mapper.parkingTimesCount(params);
        Double totalRevenueByAllCount = mapper.totalRevenueByAllCount(params);
        Map queryOrdersTimeByAllCount = mapper.queryOrdersTimeByAllCount(params);
        double countTime = Double.parseDouble(queryOrdersTimeByAllCount.get("countTime")==null?"0":queryOrdersTimeByAllCount.get("countTime").toString());
        double orderNum = Double.parseDouble(queryOrdersTimeByAllCount.get("orderNum")==null?"0":queryOrdersTimeByAllCount.get("orderNum").toString());
        Long queryMonthCardsCountByUsing = userMoncards.queryMonthCardsCountByUsing(params);
        returnMap.put("parking", parkingTimesCount);
        returnMap.put("totalRevenue", totalRevenueByAllCount);
        returnMap.put("average", countTime / orderNum);
        returnMap.put("monthCards", queryMonthCardsCountByUsing);
        return returnMap;
    }
    public Map countOrders(Map params) {
        Map returnMap = Maps.newHashMap();
        List begin = mapper.countOrdersByBegin(params);
        List end = mapper.countOrdersByEnd(params);
        returnMap.put("begin", begin);
        returnMap.put("end", end);
        return returnMap;
    }

    public List sumMoneyByEnd(Map params) {
        return mapper.sumMoneyByEnd(params);
    }

    public Map queryOrdersAllCount(Map params) {
        Map resultMap = Maps.newHashMap();
        resultMap.put("count", mapper.queryOrdersAllCount(params));
        return resultMap;
    }
    public TableResultPageResponse queryOrdersByPage(Map params){
        PageUtil.makeStartPoint(params);
        List list = mapper.queryOrdersByPage(params);
        for (int i=0;i<list.size();i++){
            Map map = (Map) list.get(i);

            Object order_type = map.get("order_type");
            if(order_type!=null){
                String orderTypeValue = order_type.toString();
                String orderTypeCode = "order_type_"+orderTypeValue;
                Map orderTypeKeyValue =  dictFeign.getDictValues(orderTypeCode);
                map.put("order_type",orderTypeKeyValue.get(orderTypeValue));
            }

            Object pay_type = map.get("pay_type");
            if(pay_type!=null){
                String payTypeValue = pay_type.toString();
                String payTypeCode = "pay_type_"+payTypeValue;
                Map payTypeKeyValue =  dictFeign.getDictValues(payTypeCode);
                map.put("pay_type",payTypeKeyValue.get(payTypeValue));
            }

            Object pay_status = map.get("pay_status");
            if(pay_status!=null){
                String payStatusValue = pay_status.toString();
                String payStatusCode = "pay_status_"+payStatusValue;
                Map payStatusKeyValue =  dictFeign.getDictValues(payStatusCode);
                map.put("pay_status",payStatusKeyValue.get(payStatusValue));
            }

        }

        return new TableResultPageResponse(mapper.queryOrdersByCount(params),
                list,Long.parseLong(params.get("startPoint").toString()),Long.parseLong(params.get("limit").toString()) );
    }
    public Object queryOrdersCountByTime(Map params) {
        String queryType = params.get("queryType").toString();
        String dateStr = params.get("queryDate").toString();
        if (queryType.equals("Year")) {
            params.put("queryYear", dateStr);
            return mapper.queryEveryMonthCountByYear(params);
        } else {
            String[] dates = dateStr.split("-");
            if (dates.length >= 2) {
                params.put("queryYear", dates[0]);
                params.put("queryMonth", dates[1]);
            }
            return mapper.queryEveryDayCountByMouth(params);
        }
    }

    public Map userActiveTimes() {
        return mapper.userActiveTimes();
    }

    public Map queryOrdersTimeByTime(Map params) {
        Map resultMap = Maps.newHashMap();
        List list;
        if ("Year".equals(params.get("queryType").toString())) {
            list = mapper.queryOrdersTimeByYear(params);
        } else {
            list = mapper.queryOrdersTimeByMonth(params);
        }
        for (int i = 0; i < list.size(); i++) {
            Map result = (Map) list.get(i);
            double countTime = Double.parseDouble(result.get("countTime")==null?"0":result.get("countTime").toString());
            double orderNum = Double.parseDouble(result.get("orderNum").toString());
            double average = countTime / orderNum / 60;
            resultMap.put(result.get("date"), average);
        }
        return resultMap;
    }


    public Map queryMonthCount(Map primary, Map secondary) {
        Long primaryLong = mapper.queryMonthCount(primary);
        Long secondaryLong = mapper.queryMonthCount(secondary);
        double primaryCount;
        double secondaryCount;
        if (primaryLong == null || primaryLong == 0) {
            primaryCount = 0;
        } else {
            primaryCount = Double.parseDouble(mapper.queryMonthCount(primary) + "");
        }
        if (secondaryLong == null || secondaryLong == 0) {
            secondaryCount = 0;
        } else {
            secondaryCount = Double.parseDouble(mapper.queryMonthCount(secondary) + "");
        }
        Map resultMap = Maps.newHashMap();
        double monthOnMonth;
        if(secondaryCount==0||primaryCount==0){
            monthOnMonth = 0.0;
            resultMap.put("flag", "+");
        }else{
            if (secondaryCount > primaryCount) {
                monthOnMonth = (secondaryCount - primaryCount) / secondaryCount;
                resultMap.put("flag", "-");
            } else {
                monthOnMonth = (primaryCount - secondaryCount) / secondaryCount;
                resultMap.put("flag", "+");
            }
        }

        resultMap.put("monthOnMonth", monthOnMonth);
        resultMap.put("primary",primaryCount);
        resultMap.put("secondary",secondaryCount);
        return resultMap;
    }

    public List totalRevenueCount(Map params) {
        String queryType = params.get("queryType").toString();
        if ("Year".equals(queryType)) {
            return mapper.totalRevenueMonthCount(params);
        } else {
            return mapper.totalRevenueDayCount(params);
        }
    }

    public Object payTypeChart(Map params) {
        Map resultMap = Maps.newHashMap();
        String queryType = params.get("queryType").toString();
        List balance;
        List alipay;
        List cash;
        List wechat;
        if ("Year".equals(queryType)) {
            params.put("payType", "balance");
            balance = mapper.payTypeChartByYear(params);
            params.put("payType", "alipay");
            alipay = mapper.payTypeChartByYear(params);
            params.put("payType", "cash");
            cash = mapper.payTypeChartByYear(params);
            params.put("payType", "wechat");
            wechat = mapper.payTypeChartByYear(params);
        } else {
            params.put("payType", "balance");
            balance = mapper.payTypeChartByMonth(params);
            params.put("payType", "alipay");
            alipay = mapper.payTypeChartByMonth(params);
            params.put("payType", "cash");
            cash = mapper.payTypeChartByMonth(params);
            params.put("payType", "wechat");
            wechat = mapper.payTypeChartByMonth(params);
        }
        resultMap.put("balance", fillPayType(params.get("queryType").toString(), balance));
        resultMap.put("alipay", fillPayType(params.get("queryType").toString(), alipay));
        resultMap.put("cash", fillPayType(params.get("queryType").toString(), cash));
        resultMap.put("wechat", fillPayType(params.get("queryType").toString(), wechat));
        return resultMap;
    }

    public List<Map> queryDataByDate(Map params) {
        return mapper.queryDataByDate(params);
    }

    public List fillPayType(String queryType, List queryResult) {
        List list = Lists.newArrayList();
        if (queryResult.size() > 0) {
            Map fullMap = fullDate(queryType, ((Map) queryResult.get(0)).get("date").toString());
            if (queryResult.size() < fullMap.size()) {
                for (int i = 0; i < queryResult.size(); i++) {
                    Map map = (Map) queryResult.get(i);
                    fullMap.remove(map.get("date").toString());
                }
                Set keys = fullMap.keySet();
                Iterator iterator = keys.iterator();
                while (iterator.hasNext()) {
                    queryResult.add(fullMap.get(iterator.next().toString()));
                }
                for(int i=0;i<queryResult.size();i++){
                    boolean isFound = false;
                    int j = 0;
                    while(!isFound){
                        Map map = (Map)queryResult.get(j);
                        String dataDate = map.get("date").toString();
                        String date = dataDate.substring(dataDate.lastIndexOf("-") + 1);
                        if(Integer.parseInt(date)==i){
                            list.add(map);
                            isFound = true;
                        }
                        j++;
                        if (j==queryResult.size()) {
                            isFound = true;
                        }
                    }
                }

            }
        }
        return list;
    }

    public Map fullDate(String queryType, String date) {
        int length;
        if("Year".equals(queryType)){
            length = 13;
        }else{
            length = 32;
        }
        String newDate = date.substring(0,date.lastIndexOf("-") + 1);
        Map maps = Maps.newHashMap();
        for (int i = 1; i <= length; i++) {
            Map map = Maps.newHashMap();
            String putDate;
            if(i<10){
                putDate = newDate +"0"+ i;
            }else {
                putDate = newDate + i;
            }
            map.put("date", putDate);
            map.put("money", 0.00);
            maps.put(putDate, map);
        }
        return maps;
    }

    public void quickSort(int[] arr,int low,int high){
        int i,j,temp,t;
        if(low>high){
            return;
        }
        i=low;
        j=high;
        //temp就是基准位
        temp = arr[low];

        while (i<j) {
            //先看右边，依次往左递减
            while (temp<=arr[j]&&i<j) {
                j--;
            }
            //再看左边，依次往右递增
            while (temp>=arr[i]&&i<j) {
                i++;
            }
            //如果满足条件则交换
            if (i<j) {
                t = arr[j];
                arr[j] = arr[i];
                arr[i] = t;
            }

        }
        //最后将基准为与i和j相等位置的数字交换
        arr[low] = arr[i];
        arr[i] = temp;
        //递归调用左半数组
        quickSort(arr, low, j-1);
        //递归调用右半数组
        quickSort(arr, j+1, high);
    }
}
