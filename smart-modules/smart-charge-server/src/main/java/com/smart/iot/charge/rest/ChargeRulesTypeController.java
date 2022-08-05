package com.smart.iot.charge.rest;

import com.smart.iot.charge.biz.ChargeByHourBiz;
import com.smart.iot.charge.biz.ChargeByHoursBiz;
import com.smart.iot.charge.biz.ChargeByTimePeriodBiz;
import com.smart.iot.charge.biz.ChargeRulesTypeBiz;
import com.smart.iot.charge.entity.ChargeByHour;
import com.smart.iot.charge.entity.ChargeByHours;
import com.smart.iot.charge.entity.ChargeByTimePeriod;
import com.smart.iot.charge.entity.ChargeRulesType;
import com.smart.iot.charge.feign.ParkingFeign;
import com.smart.iot.charge.server.ChangingByTimePeriodUtil;
import com.smart.iot.charge.server.ChargingByHoursUtil;
import com.smart.iot.charge.util.ChargeByHourUtil;
import com.smart.iot.charge.util.ChargeByTimePeriodUtil;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import com.yuncitys.smart.parking.common.util.BeanUtils2;
import com.yuncitys.smart.parking.common.util.CommonException;
import com.yuncitys.smart.parking.common.util.DateUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

@RestController
@RequestMapping("chargeRulesType")
public class ChargeRulesTypeController extends BaseController<ChargeRulesTypeBiz,ChargeRulesType,Integer> {
    @Autowired
    public ChargeRulesTypeBiz chargeRulesTypeBiz;
    @Autowired
    public ChargeByHourBiz chargeByHourBiz;
    @Autowired
    public ChargeByTimePeriodBiz chargeByTimePeriodBiz;
    @Autowired
    public ParkingFeign parkingFeign;
    @Autowired
    public ChargeByHoursBiz chargeByHoursBiz;
    @ApiOperation("获取收费规则")
    @RequestMapping(value = "/queryByIdAndParkingId", method = RequestMethod.GET)
    public ObjectRestResponse<Object> queryChargeRules(String chargeRuleId, String parkingId) {
        ChargeRulesType chargeRulesType = this.baseBiz.selectById(chargeRuleId);
        Object object = null;
        if(chargeRulesType.getTableName().equals("charge_by_time_period"))
        {
            Example  chargeByTimePeriodrExample = new Example(ChargeByTimePeriod.class);
            chargeByTimePeriodrExample.createCriteria().andEqualTo("parkingId",parkingId);
            List<ChargeByTimePeriod> chargeByTimePeriodlist = chargeByTimePeriodBiz.selectByExample(chargeByTimePeriodrExample);
            object = (Object)chargeByTimePeriodlist;
        }else if (chargeRulesType.getTableName().equals("charge_by_hour"))
        {
            Example  chargeByHourExample = new Example(ChargeByHour.class);
            chargeByHourExample.createCriteria().andEqualTo("parkingId",parkingId);
            List<ChargeByHour> chargeByHourlist = chargeByHourBiz.selectByExample(chargeByHourExample);
            object = (Object)chargeByHourlist;
        }
        else if (chargeRulesType.getTableName().equals("charge_by_hours"))
        {
            Example  chargeByHourExample = new Example(ChargeByHour.class);
            chargeByHourExample.createCriteria().andEqualTo("parkingId",parkingId);
            List<ChargeByHours> chargeByHourlist = chargeByHoursBiz.selectByExample(chargeByHourExample);
            object = (Object)chargeByHourlist;
        }
        return new ObjectRestResponse<Object>().data(object);
    }
    @RequestMapping(value = "/addOrUpdateChargeRule",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("新增收费规则对象")
    public ObjectRestResponse<Object> addOrUpdateChargeRule(@RequestBody Map<String, Object> params) throws CommonException {
        String chargeRuleId = String.valueOf(params.get("chargeRuleId"));
        String parkingId="";
        if(params.get("parkingId")!=null) {
             parkingId = String.valueOf(params.get("parkingId"));
        }
        ChargeRulesType chargeRulesType = this.baseBiz.selectById(chargeRuleId);
        List<Map<String,Object>> mapList = (List<Map<String,Object>>)params.get("params");
        Object object = null;
        if(chargeRulesType.getTableName().equals("charge_by_time_period"))
        {
            BeanUtils2<ChargeByTimePeriod> beanUtils2 = new BeanUtils2<ChargeByTimePeriod>();
            List<ChargeByTimePeriod> chargeByTimePeriodList = beanUtils2.ListMap2JavaBean(mapList, ChargeByTimePeriod.class);
            for(ChargeByTimePeriod chargeByTimePeriod:chargeByTimePeriodList)
            {
                if(chargeByTimePeriod.getCarType().equals("auto"))
                {
                    String dataPrefix = DateUtil.dateTimeToStr(new Date()).substring(0,13)+":";
                    String startTime = dataPrefix+chargeByTimePeriod.getOdStartTime();
                    Date data = DateUtil.strToDate(startTime,"yyyy-MM-dd HH:mm:ss");
                    Date endDate = DateUtil.addHour(data,1);
                    String endTime = DateUtil.dateTimeToStr(endDate);

                    //取小车类型第一个小时价格

                    Map<String,Object> map = new HashMap<String,Object>();
                    map.put("parkingId",chargeByTimePeriod.getParkingId());
                    map.put("firstHourPrice",chargeByTimePeriod.getOdFirstPrice());
                   // parkingFeign.updateFirstHourPrice(map);

                }
                if(StringUtil.isEmpty(chargeByTimePeriod.getLrId())) {
                    chargeByTimePeriod.setLrId(uuid());

                }
                if(!StringUtil.isEmpty(parkingId)){
                    chargeByTimePeriod.setParkingId(parkingId);
                }
            }
            chargeByTimePeriodBiz.saveOrUpdate(chargeByTimePeriodList);
        }else if (chargeRulesType.getTableName().equals("charge_by_hour"))
        {
            BeanUtils2<ChargeByHour> beanUtils2 = new BeanUtils2<ChargeByHour>();
            List<ChargeByHour> chargeByHourList = beanUtils2.ListMap2JavaBean(mapList, ChargeByHour.class);
            for(ChargeByHour chargeByHour:chargeByHourList)
            {
                if(chargeByHour.getCarType().equals("auto"))
                {
                    Map<String,Object> map = new HashMap<String,Object>();
                    map.put("parkingId",chargeByHour.getParkingId());
                    map.put("firstHourPrice",chargeByHour.getOdFirstPrice().multiply(new BigDecimal(4)));
                   //parkingFeign.updateFirstHourPrice(map);
                }
                if(StringUtil.isEmpty(chargeByHour.getLrId())) {
                    chargeByHour.setLrId(uuid());

                }
                if(!StringUtil.isEmpty(parkingId)){
                    chargeByHour.setParkingId(parkingId);
                }
            }
            chargeByHourBiz.saveOrUpdate(chargeByHourList);
        }
        else if (chargeRulesType.getTableName().equals("charge_by_hours"))
        {
            BeanUtils2<ChargeByHours> beanUtils2 = new BeanUtils2<ChargeByHours>();
            List<ChargeByHours> chargeByHourList = beanUtils2.ListMap2JavaBean(mapList, ChargeByHours.class);
            for(ChargeByHours chargeByHour:chargeByHourList)
            {
                if(chargeByHour.getCarType().equals("auto"))
                {
                    Map<String,Object> map = new HashMap<String,Object>();
                    map.put("parkingId",chargeByHour.getParkingId());
                    map.put("firstHourPrice",chargeByHour.getFirstPrice().multiply(new BigDecimal(4)));
                   //parkingFeign.updateFirstHourPrice(map);
                }
                if(StringUtil.isEmpty(chargeByHour.getLrId())) {
                    chargeByHour.setLrId(uuid());
                }
                if(!StringUtil.isEmpty(parkingId)){
                    chargeByHour.setParkingId(parkingId);
                }
            }
            chargeByHoursBiz.saveOrUpdate(chargeByHourList);
        }
        return new ObjectRestResponse<Object>().data(params);
    }
    @ApiOperation("获取当前收费金额")
    @RequestMapping(value = "/queryCostByIdAndParkingId", method = RequestMethod.GET)
    public ObjectRestResponse<BigDecimal> queryCostByIdAndParkingId(@RequestParam Map<String, Object> params) throws ParseException {
        String chargeRuleType = String.valueOf(params.get("chargeRuleType"));
        String parkingId = String.valueOf(params.get("parkingId"));
        String carType = String.valueOf(params.get("carType"));
        String beginDate = String.valueOf(params.get("beginDate"));
        String type = String.valueOf(params.get("type"));
        ChargeRulesType chargeRulesType = this.baseBiz.selectById(chargeRuleType);
        BigDecimal money = new BigDecimal(0);
        if(chargeRulesType.getTableName().equals("charge_by_time_period"))
        {
            ChargeByTimePeriod  chargeByTimePeriodExample = new ChargeByTimePeriod();
            chargeByTimePeriodExample.setParkingId(parkingId);
            chargeByTimePeriodExample.setCarType(carType);
            ChargeByTimePeriod chargeByTimePeriod=chargeByTimePeriodBiz.selectOne(chargeByTimePeriodExample);
            money=ChargeByTimePeriodUtil.CalculatingPrice(DateUtil.parse(beginDate,null),new Date(),chargeByTimePeriod);
        }else if (chargeRulesType.getTableName().equals("charge_by_hour"))
        {
            ChargeByHour  chargeByHourExample = new ChargeByHour();
            chargeByHourExample.setParkingId(parkingId);
            chargeByHourExample.setCarType(carType);
            ChargeByHour chargeByHour = chargeByHourBiz.selectOne(chargeByHourExample);
            money = ChargingByHoursUtil.getprice(chargeByHour,beginDate,DateUtil.dateTimeToStr(new Date()));
        }else if (chargeRulesType.getTableName().equals("charge_by_hours"))
        {
            ChargeByHours  chargeByHourExample = new ChargeByHours();
            chargeByHourExample.setParkingId(parkingId);
            chargeByHourExample.setCarType(carType);
            ChargeByHours chargeByHours = chargeByHoursBiz.selectOne(chargeByHourExample);
            if(null == chargeByHours){
                return new ObjectRestResponse<BigDecimal>().data(money);
            }
            try {
                Date dates=new Date();
                Date beginTime=DateUtil.parse(beginDate,DateUtil.YYYY_MM_DD_HH_MM_SS);
                if(type.equals("vipSchedule")){
                    dates=DateUtil.addMinute(beginTime,60);
                }else if(type.equals("vip")){
                    dates = new Date();
                }
                Map map=ChargeByHourUtil.CalculatingPrice(beginTime,dates,chargeByHours);
                money =BigDecimal.valueOf(Double.valueOf(String.valueOf(map.get("price"))));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return new ObjectRestResponse<BigDecimal>().data(money);
    }

    @RequestMapping(value = "/getWebCode",method = RequestMethod.POST)
    @ResponseBody
    public String getWebCode(@RequestParam("parkingId") String parkingId,@RequestParam("tableName") String tableName,@RequestParam("webCode")
            String webCode){
        String html="";
        webCode= StringEscapeUtils.unescapeHtml4(webCode);
        if(tableName.equals("charge_by_hours")){
            html=chargeByHoursBiz.splicingHtml(parkingId,webCode);
        }else if(tableName.equals("charge_by_time_period")){
            html=chargeByTimePeriodBiz.splicingHtml(parkingId,webCode);
        }
        System.out.println("======"+html);
        return html;
    }

    @RequestMapping(value = "/idInList",method = RequestMethod.POST)
    @ResponseBody
    public List<ChargeRulesType> idInList(@RequestParam("idList") List<String> idList){
        Example example=new Example(ChargeRulesType.class);
        example.createCriteria().andIn("id",idList);
        return chargeRulesTypeBiz.selectByExample(example);
    }

    public static String uuid()
    {
        String ret = UUID.randomUUID().toString();
        return ret.replaceAll("-", "");
    }

}
