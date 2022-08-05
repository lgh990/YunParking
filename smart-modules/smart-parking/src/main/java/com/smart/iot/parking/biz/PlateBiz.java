package com.smart.iot.parking.biz;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smart.iot.parking.entity.ParkingSpace;
import com.smart.iot.parking.entity.Plate;
import com.smart.iot.parking.mapper.PlateMapper;
import com.smart.iot.parking.utils.HttpUtils;
import com.smart.iot.parking.utils.JsonUtil;
import com.smart.iot.parking.utils.VehicleServiceUtil;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 车牌表
 *
 * @author Mr.AG
 * @email
 *@version 2022-08-08 11:34:08
 */
@Service
@Slf4j
public class PlateBiz extends BusinessBiz<PlateMapper,Plate> {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    public static long time = 24 * 60 * 60 * 1000;
    public List<Plate> PlaIdInList(List<String> plaIdList){
        if(plaIdList!=null && plaIdList.size()>0) {
            Example example = new Example(Plate.class);
            example.createCriteria().andIn("plaId", plaIdList);
            return this.selectByExample(example);
        }
        return new ArrayList<>();
    }

    /**
     * 添加车牌
     * @param map
     * @return
     */
    /*public ObjectRestResponse  addPlate(Map<String,String> map){
        String plateStr=VehicleServiceUtil.VerificationVehicleInfo(map);
        log.info(plateStr);
        Map<String,Object> plate= JsonUtil.JsonStringToMap(plateStr);
        if (plate!=null) {
            Map<String,Object> map1= (Map<String, Object>) plate.get("data");
            Integer verifystatus=(int)plate.get("code");
            if(verifystatus!=0 ){
                return new ObjectRestResponse().BaseResponse(500,plate.get("message").toString());
            }
            Plate params = new Plate();
            params.setCarNumber(map.get("carNo"));
            Plate p = this.selectOne(params);
            if (p != null ) {
                if(!StringUtil.isEmpty(p.getUserId()) && !p.getUserId().equals("0")) {
                    ObjectRestResponse objectRestResponse = new ObjectRestResponse();
                    objectRestResponse.setStatus(500);
                    objectRestResponse.setMessage("车牌已存在");
                    return objectRestResponse;
                }else{
                    p.setUserId(map.get("userId"));
                    this.saveOrUpdate(p);
                    Map result=(Map<String, Object>) plate.get("result");
                    return new ObjectRestResponse().data(result);
                }
            }
            Plate plate1 = new Plate();
            plate1.setPlaId(StringUtil.uuid());
            plate1.setFrameno(map.get("frameno"));
            plate1.setEnginno(map.get("enginNo"));
            plate1.setCarNumber(map.get("carNo"));
            int type=Integer.valueOf(map.get("type"));
            plate1.setCarType(type+"");
            plate1.setUserId(map.get("userId"));
            this.saveOrUpdate(plate1);
            Map result=(Map<String, Object>) plate.get("result");
            return new ObjectRestResponse().data(result);
        }
        return new ObjectRestResponse().BaseResponse(500,"运营商服务器错误！！");
    }*/

    /**
     * 添加车牌
     * caoyingde 注销第三方的车牌验证
     * @param map
     * @return
     */
    public ObjectRestResponse  addPlate(Map<String,String> map){
        //String plateStr=VehicleServiceUtil.VerificationVehicleInfo(map);
        //log.info(plateStr);
        //Map<String,Object> plate= JsonUtil.JsonStringToMap(plateStr);
        //if (plate!=null) {
            //Map<String,Object> map1= (Map<String, Object>) plate.get("data");
            //Integer verifystatus=(int)plate.get("code");
            //if(verifystatus!=0 ){
            //    return new ObjectRestResponse().BaseResponse(500,plate.get("message").toString());
            //}
            Plate params = new Plate();
            params.setCarNumber(map.get("carNo"));
            Plate p = this.selectOne(params);
            if (p != null ) {
                if(!StringUtil.isEmpty(p.getUserId()) && !p.getUserId().equals("0")) {
                    ObjectRestResponse objectRestResponse = new ObjectRestResponse();
                    objectRestResponse.setStatus(500);
                    objectRestResponse.setMessage("车牌已存在");
                    return objectRestResponse;
                }else{
                    p.setUserId(map.get("userId"));
                    this.saveOrUpdate(p);
                    return new ObjectRestResponse().BaseResponse(200,"添加成功!");
                }
            }
            Plate plate1 = new Plate();
            plate1.setPlaId(StringUtil.uuid());
            plate1.setFrameno(map.get("frameno"));
            plate1.setEnginno(map.get("enginNo"));
            plate1.setCarNumber(map.get("carNo"));
            int type=Integer.valueOf(map.get("type"));
            plate1.setCarType(type+"");
            plate1.setUserId(map.get("userId"));
            this.saveOrUpdate(plate1);
            //Map result=(Map<String, Object>) plate.get("result");
            return new ObjectRestResponse().BaseResponse(200,"添加成功!");
        //}
        //return new ObjectRestResponse().BaseResponse(500,"运营商服务器错误！！");
    }

    public  ObjectRestResponse queryCity() {
        String city=redisTemplate.opsForValue().get("city");
        if(StringUtil.isEmpty(city)) {
            city=VehicleServiceUtil.queryCity();
            Map<String,Object> map= JsonUtil.JsonStringToMap(city);
            if(map!=null && map.get("msg").equals("ok")) {
                List result= (List) map.get("result");
                redisTemplate.opsForValue().set("city", result.toString());
                return new ObjectRestResponse().data(result);
            }else{
                return new ObjectRestResponse().BaseResponse(500,map.get("msg").toString());
            }
        }
        return new ObjectRestResponse().data(JSON.parse(city));
    }

    /**
     * 城市限行查询接口
     * @param map
     * @return
     */
    public  ObjectRestResponse qureyCityLimitRow(Map<String,String> map) {
        String city=redisTemplate.opsForValue().get("CITY:"+map.get("date")+":"+map.get("city"));
        if(StringUtil.isEmpty(city)) {
            city=VehicleServiceUtil.qureyCityLimitRow(map);
            Map<String,Object> maps= JsonUtil.JsonStringToMap(city);
            if(maps!=null && maps.get("msg").equals("ok")) {
                Map result=(Map<String, Object>) maps.get("result");
                redisTemplate.opsForValue().set("CITY:" +map.get("date")+":"+ map.get("city"), result.toString());
                return new ObjectRestResponse().data(result);
            }else{
                return new ObjectRestResponse().BaseResponse(500,maps.get("msg").toString());
            }
        }
        log.info(city);
        return new ObjectRestResponse().data(JSON.parse(city));
    }

    /**
     * 驾驶证扣分查询
     * @param params
     * @return
     */
    public  ObjectRestResponse scoreDeductionInquiry(Map<String,String> params) {
        String scoreDeductionInquiry=redisTemplate.opsForValue().get("DrivingLicenseDeduction:"+params.get("licensenumber"));
        if(StringUtil.isEmpty(scoreDeductionInquiry)) {
            scoreDeductionInquiry=VehicleServiceUtil.scoreDeductionInquiry(params);
            Map<String,Object> maps= JsonUtil.JsonStringToMap(scoreDeductionInquiry);
            if(maps!=null && maps.get("msg").equals("ok")) {
                Map result=(Map<String, Object>) maps.get("result");
                redisTemplate.opsForValue().set("DrivingLicenseDeduction:" + params.get("licensenumber"), result.toString());
                return new ObjectRestResponse().data(result);
            }else{
                return new ObjectRestResponse().BaseResponse(500,maps.get("msg").toString());
            }
        }
        return new ObjectRestResponse().data(JSON.parse(scoreDeductionInquiry));
    }

    /**
     * 全国车辆违章记录查询 （支持新能源车、大型货车查询）
     * @param params
     * @return
     */
    public  ObjectRestResponse recordsOfViolations(Map<String,String> params){
        String recordsOfViolations=redisTemplate.opsForValue().get("VIOLATIONS:"+params.get("car_no"));
        if(StringUtil.isEmpty(recordsOfViolations)) {
            try {
                recordsOfViolations=VehicleServiceUtil.recordsOfViolations(params);
                Map<String,Object> map= JsonUtil.JsonStringToMap(recordsOfViolations);
                if(map!=null && map.get("result")!=null) {
                    Map<String,Object> map1= (Map<String, Object>) map.get("result");
                    String res=(String)map1.get("res");
                    if(res.equals("1")) {
                        redisTemplate.opsForValue().set("VIOLATIONS:" + params.get("car_no"), map1.toString());
                        return new ObjectRestResponse().data(map1);
                    }else{
                        return new ObjectRestResponse().BaseResponse(500,map1.get("description").toString());
                    }
                }else{
                    return new ObjectRestResponse().BaseResponse(500,map.get("msg").toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ObjectRestResponse().data(JSON.parse(recordsOfViolations));
    }
}
