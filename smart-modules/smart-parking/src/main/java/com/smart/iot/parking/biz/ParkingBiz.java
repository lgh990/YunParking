package com.smart.iot.parking.biz;

import com.smart.iot.parking.constant.BaseConstants;
import com.smart.iot.parking.entity.*;
import com.smart.iot.parking.feign.ChargeRulesTypeFeign;
import com.smart.iot.parking.feign.UserFeign;
import com.smart.iot.parking.mapper.ParkingMapper;
import com.smart.iot.parking.srever.TablePageResultParser;
import com.smart.iot.parking.vo.ParkingVo;
import com.yuncitys.ag.core.context.BaseContextHandler;
import com.yuncitys.smart.merge.annonation.MergeResult;
import com.yuncitys.smart.merge.core.MergeCore;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.msg.TableResultPageResponse;
import com.yuncitys.smart.parking.common.util.Query;
import com.yuncitys.smart.parking.common.util.StringUtil;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 停车场
 *
 * @author Mr.AG
 * @email
 *@version 2022-07-30 16:18:04
 */
@Service
public class ParkingBiz extends BusinessBiz<ParkingMapper,Parking>  {
    @Autowired
    public ParkingAreaBiz parkingAreaBiz;
    @Autowired
    public ParkingSpaceBiz parkingSpaceBiz;
    @Autowired
    public ParkingIoBiz parkingIoBiz;
    @Autowired
    public ParkingComponentBiz parkingComponentBiz;
    @Autowired
    public ParkingComponentDataBiz parkingComponentDataBiz;
    @Autowired
    public MergeCore mergeCore;
    @Autowired
    public UserParkingBiz userParkingBiz;
    @Autowired
    public ChargeRulesTypeFeign chargeRulesTypeFeign;
    @Autowired
    public UserFeign userFeign;
    @Autowired
    public ParkingBusinessTypeBiz parkingBusinessTypeBiz;
    public ObjectRestResponse<Map<String,Object>> queryMapInfo(String areaId) {


        Map<String, Object> areaobj = new HashMap<String, Object>();
        //当前区层

        ParkingArea parkingArea = parkingAreaBiz.selectById(areaId);
        areaobj.put("parkingArea", parkingArea);

        Example spaceExample = new Example(ParkingSpace.class);
        spaceExample.createCriteria().andEqualTo("areaId",areaId);
        spaceExample.setOrderByClause("space_num Asc");
        List<ParkingSpace> spacelist = parkingSpaceBiz.selectByExample(spaceExample);
        //获取该区层的所有车位
        areaobj.put("parking_space_list", spacelist);


        Example ioExample = new Example(ParkingIo.class);
        ioExample.createCriteria().andEqualTo("parkingAreaId",areaId);
        List<ParkingIo> iolist = parkingIoBiz.selectByExample(ioExample);
        //获取该区层所有出入口
        areaobj.put("parking_io_list", iolist);


        // 自定义部件列表
        List<ParkingComponent> parkingComponentkList = parkingComponentBiz.selectListAll();
        areaobj.put("parking_component_list", parkingComponentkList);
        Example parkingComponentDataExample = new Example(ParkingComponentData.class);
        parkingComponentDataExample.createCriteria().andEqualTo("parkingAreaId",areaId);
        List<ParkingComponentData> parkingComponentDatakList = parkingComponentDataBiz.selectByExample(parkingComponentDataExample);
        //其他部件分布列表
        areaobj.put("parking_component_data", parkingComponentDatakList);
        //图片IP
/*
        areaobj.put("hostIp","http://"+ConfigureBean.FTP_HOSTNAME.toString()+ Constants.SYSPATH);
*/
        return new ObjectRestResponse<Map<String,Object>>().data(areaobj);
    }
    public List<Parking> queryNearbyParking(Map<String, Object> params) {
        String longitude = String.valueOf(params.get("longitude"));
        String latitude = String.valueOf(params.get("latitude"));
        String spaceType = null;
        if(!StringUtil.isBlank(String.valueOf(params.get("spaceType"))))
        {
             spaceType = String.valueOf(params.get("spaceType"));
        }
        String chargePile = null;
        if(!StringUtil.isBlank(String.valueOf(params.get("chargePile"))))
        {
            chargePile = String.valueOf(params.get("chargePile"));
        }
        Double addlng = Double.parseDouble(longitude) + BaseConstants.Coordinate.lng;
        Double reducelng = Double.parseDouble(longitude) - BaseConstants.Coordinate.lng;
        Double  addlat= Double.parseDouble(latitude) + BaseConstants.Coordinate.lat;
        Double  reducelat = Double.parseDouble(latitude) - BaseConstants.Coordinate.lat;
        List<Parking> parkingList = this.mapper.queryNearbyParking(spaceType,addlat,reducelat,addlng,reducelng,chargePile);
        try {
            mergeCore.mergeResult(Parking.class,parkingList);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if(parkingList!=null&&parkingList.size()>0){
            Parking parking=null;
            List<Parking> parkings=null;
            String[] ids=new String[parkingList.size()];
            for(int i=0;i<parkingList.size();i++){
                parking=parkingList.get(i);
                ids[i]=parking.getParkingId();
            }
            parkings=this.mapper.querySpaceCountByParkingIds(spaceType,ids);
            for(int i=0;i<parkingList.size();i++){
                parking=parkingList.get(i);
                if(StringUtil.isInt(parking.getLeftNum())){
                    setParkingLeftNum(parkings,parking);
                }else{
                    parking.setLeftNum("0");
                }

            }
        }
        return parkingList;
    }

    private void setParkingLeftNum(List<Parking> parkings,Parking parking){
        Parking parking2=null;
        for(int i=0;i<parkings.size();i++){
            parking2=parkings.get(i);
            if(parking2.getParkingId().equals(parking.getParkingId())){
                if(StringUtil.isInt(parking2.getLeftNum())) {
                    parking.setLeftNum(parking2.getLeftNum());
                }else{
                    parking.setLeftNum("0");
                }
            }
        }
    }


    @MergeResult(resultParser = TablePageResultParser.class)
    @Override
    public TableResultPageResponse<Parking> selectByPageQuery(Query query, String beginDate, String endDate)
    {
        return super.selectByPageQuery(query,  beginDate, endDate);
    }
    /**
     * 根据ID批量获取实体
     *
     * @param iDs
     * @return
     */
    public Map<String, String> queryLeftSpaceNum(String iDs) {
        if (org.apache.commons.lang3.StringUtils.isBlank(iDs)) {
            return new HashMap<>();
        }
        String[] idList = new String[iDs.split(",").length];
        for(int i = 0;i<iDs.split(",").length;i++)
        {
            idList[i] = iDs.split(",")[i];
        }
        List<Parking> parkings = this.mapper.querySpaceCountByIds(idList);
        Map<String, String> map = new HashMap<String, String>();
        for (Parking parking : parkings) {
            map.put(parking.getParkingId(),parking.getLeftNum());
        }
        return map;
    }
    /**
     * 根据ID批量获取实体
     *
     * @param iDs
     * @return
     */
    public Map<String, String> querySpaceNumTotal(String iDs) {
        if (org.apache.commons.lang3.StringUtils.isBlank(iDs)) {
            return new HashMap<>();
        }
        String[] idList = new String[iDs.split(",").length];
        for(int i = 0;i<iDs.split(",").length;i++)
        {
            idList[i] = iDs.split(",")[i];
        }
        List<Parking> parkings = this.mapper.querySpaceSumByIds(idList);
        Map<String, String> map = new HashMap<String, String>();
        for (Parking parking : parkings) {
            map.put(parking.getParkingId(),parking.getTotalNum());
        }
        return map;
    }

    public ObjectRestResponse<Parking> add(Parking parking) {
        this.insertSelective(parking);
        //设置停车场剩余车位为停车场id,用于处理数据聚合
        parking = this.selectById(parking.getParkingId());
        parking.setLeftNum(parking.getParkingId());
        parking.setTotalNum(parking.getParkingId());
        this.saveOrUpdate(parking);
        String userId = BaseContextHandler.getUserID();
        UserParking userParking=new UserParking();
        userParking.setFpId(StringUtil.uuid());
        userParking.setUserId(userId);
        userParking.setParkingId(parking.getParkingId());
        userParkingBiz.saveOrUpdate(userParking);
        return new ObjectRestResponse<Parking>().data(parking);

    }

    public List<Parking> parkingIdInList(List<String> parkingIdList){
        List<Parking> parkingList=new ArrayList<>();
        if(parkingIdList!=null && parkingIdList.size()>0) {
            Example example = new Example(Parking.class);
            example.createCriteria().andIn("parkingId", parkingIdList);
             parkingList = this.selectByExample(example);
        }
        return parkingList;
    }

    public List<ParkingVo> parkingToParkingVo(List<Parking> parkingList){
        List<String> parkingBusType=new ArrayList<>();
        List<String> userIdList=new ArrayList<>();
        List<String> chargeRuleIdList=new ArrayList<>();
        for(Parking parking:parkingList){
            parkingBusType.add(parking.getParkingBusType());
            userIdList.add(parking.getUserId());
            chargeRuleIdList.add(parking.getChargeRuleId());
        }
        List<ParkingVo> list=new ArrayList();
        List<ParkingBusinessType> parkingBusinessTypeList=parkingBusinessTypeBiz.businessTypeInList(parkingBusType);
        List<Map> userList=new ArrayList<>();
        if(userIdList!=null && userIdList.size()>0){
            userList =userFeign.userInList(userIdList);
        }
        List<Map> chargeRuleList=new ArrayList<>();
        if(chargeRuleIdList!=null && chargeRuleIdList.size()>0){
            chargeRuleList= chargeRulesTypeFeign.idInList(chargeRuleIdList);
        }
        for(Parking parking:parkingList){
            ParkingVo parkingPage=new ParkingVo(parking);
            for(ParkingBusinessType parkingBusinessType:parkingBusinessTypeList){
                if(parkingBusinessType.getId().equals(parking.getParkingBusType())){
                    parkingPage.setParkingBusTypeEntity(parkingBusinessType);
                }
            }
            for(Map user:userList){
                if(user.get("id").equals(parking.getUserId())){
                    parkingPage.setUser(user);
                }
            }
            for(Map chargeRule:chargeRuleList){
                if(chargeRule.get("id").equals(parking.getChargeRuleId())){
                    String web=String.valueOf(chargeRule.get("webCode"));
                    String table=String.valueOf(chargeRule.get("tableName"));
                    String parkingId=parking.getParkingId();
                    System.out.println("web======  "+web);
                    System.out.println("parking======  "+parkingId);
                    System.out.println("table======  "+table);
                    String webCode=chargeRulesTypeFeign.getWebCode(parkingId,table,web);
                    chargeRule.put("webCode",webCode);
                    parkingPage.setChargeRule(chargeRule);
                }
            }

            list.add(parkingPage);
        }
        return list;
    }


    public List<ParkingVo> parkingToParkingVos(List<Parking> parkingList){
        List<String> parkingBusType=new ArrayList<>();
        List<String> userIdList=new ArrayList<>();
        List<String> chargeRuleIdList=new ArrayList<>();
        for(Parking parking:parkingList){
            parkingBusType.add(parking.getParkingBusType());
            userIdList.add(parking.getUserId());
            chargeRuleIdList.add(parking.getChargeRuleId());
        }
        List<ParkingVo> list=new ArrayList();
        List<ParkingBusinessType> parkingBusinessTypeList=parkingBusinessTypeBiz.businessTypeInList(parkingBusType);
        List<Map> userList=new ArrayList<>();
        if(userIdList!=null && userIdList.size()>0){
            userList =userFeign.userInList(userIdList);
        }
        List<Map> chargeRuleList=new ArrayList<>();
        if(chargeRuleIdList!=null && chargeRuleIdList.size()>0){
            chargeRuleList= chargeRulesTypeFeign.idInList(chargeRuleIdList);
        }
        for(Parking parking:parkingList){
            ParkingVo parkingPage=new ParkingVo(parking);
            for(ParkingBusinessType parkingBusinessType:parkingBusinessTypeList){
                if(parkingBusinessType.getId().equals(parking.getParkingBusType())){
                    parkingPage.setParkingBusTypeEntity(parkingBusinessType);
                }
            }
            for(Map user:userList){
                if(user.get("id").equals(parking.getUserId())){
                    parkingPage.setUser(user);
                }
            }
            for(Map chargeRule:chargeRuleList){
                if(chargeRule.get("id").equals(parking.getChargeRuleId())){
                    String webCode=chargeRulesTypeFeign.getWebCode(parking.getParkingId(),String.valueOf(chargeRule.get("tableName")),String.valueOf(chargeRule.get("webCode")));
                    HashMap<String, Object> map=chargeRulesTypeFeign.queryByIdAndParkingId(parking.getParkingId(),parking.getChargeRuleId());
                    chargeRule.put("webCode",webCode);
                    List<HashMap> lists= (List<HashMap>) map.get("data");
                    HashMap maps=new HashMap();
                    for(HashMap map1:lists){
                        if(map1.get("carType").toString().equals("truck")) {
                            maps = map1;
                        }
                    }
                    chargeRule.put("map",maps);
                    parkingPage.setChargeRule(chargeRule);
                    if(maps.get("openFlag")!=null && maps.get("openFlag").toString().equals(BaseConstants.enabledFlag.y)){
                        list.add(parkingPage);
                    }
                }
            }


        }
        return list;
    }

    public Map queryEveryMonthCountByYear(Map params){
        return mapper.queryEveryMonthCountByYear(params);
    }

    public Map queryMonthCount(Map primary,Map secondary){
        Long primaryLong = mapper.queryMonthCount(primary);
        Long secondaryLong = mapper.queryMonthCount(secondary);
        double primaryCount;
        double secondaryCount;
        if(primaryLong==null||primaryLong==0){
            primaryCount = 0;
        }else{
            primaryCount = Double.parseDouble(primaryLong+"");
        }
        if(secondaryLong==null||secondaryLong==0){
            secondaryCount = 0;
        }else{
            secondaryCount =Double.parseDouble(secondaryLong+"");
        }
        Map resultMap = Maps.newHashMap();
        double monthOnMonth;
        if(secondaryCount>primaryCount) {
            monthOnMonth =(secondaryCount - primaryCount) / secondaryCount;
            resultMap.put("flag","-");
        }else{
            monthOnMonth = (primaryCount-secondaryCount)/secondaryCount;
            resultMap.put("flag","+");
        }
        resultMap.put("monthOnMonth",monthOnMonth);
        resultMap.put("primary",primaryLong);
        resultMap.put("secondary",secondaryLong);
        return resultMap;
    }

    @Bean
    @Lazy
    public Map initAreaParams(){
        List<Parking> list = mapper.selectAll();
        Map map = Maps.newHashMap();
        for (Parking item:list) {
            map.put(item.getParkingId(), Maps.newHashMap());
        }
        return map;
    }

}
