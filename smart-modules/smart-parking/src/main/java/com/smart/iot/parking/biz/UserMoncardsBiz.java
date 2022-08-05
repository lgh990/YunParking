package com.smart.iot.parking.biz;

import com.alibaba.fastjson.JSONObject;
import com.smart.iot.parking.entity.*;
import com.smart.iot.parking.mapper.UserMoncardsMapper;
import com.smart.iot.parking.srever.TableResultParser;
import com.smart.iot.parking.utils.JsonUtil;
import com.smart.iot.parking.utils.PageUtil;
import com.smart.iot.parking.vo.UserFeedbackVo;
import com.smart.iot.parking.vo.UserMoncardsVo;
import com.yuncitys.smart.merge.annonation.MergeResult;
import com.yuncitys.smart.merge.core.MergeCore;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import com.yuncitys.smart.parking.common.msg.TableResultPageResponse;
import com.yuncitys.smart.parking.common.msg.TableResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springfox.documentation.spring.web.json.Json;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 *
 *
 * @author Mr.AG
 * @email
 *@version 2022-08-15 17:31:58
 */
@Service
public class UserMoncardsBiz extends BusinessBiz<UserMoncardsMapper,UserMoncards> {
    @Autowired
    public UserMoncardsMapper userMoncardsMapper;
    @Autowired
    public MergeCore mergeCore;
    @Autowired
    public ParkingBiz parkingBiz;
    @Autowired
    public PlateBiz plateBiz;
    public TableResultResponse queryMonthCardByUserId(String userId) {
        List<UserMoncards> userMoncards = userMoncardsMapper.queryMonthCardByUserId(userId);
        List<UserMoncardsVo> userMoncardsVoList=userMoncardsToUserMoncardsVo(userMoncards);
    /*   for(UserMoncards userMoncards1:userMoncards){
            Parking parking=JSONObject.parseObject(userMoncards1.getParkingId(), Parking.class);
           parking.setLeftNum("0");
           parking.setTotalNum("0");
           try {
                userMoncards1.setParkingId(JsonUtil.objectToJson(parking));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        return new TableResultResponse(userMoncards.size(),userMoncardsVoList);
    }

    public List<UserMoncardsVo> userMoncardsToUserMoncardsVo(List<UserMoncards> userMoncardsList){
        List<String> parkingIdList=new ArrayList<>();
        List<String> plaIdList=new ArrayList<>();
        for(UserMoncards userMoncards:userMoncardsList){
            parkingIdList.add(userMoncards.getParkingId());
            plaIdList.add(userMoncards.getPlateId());
        }
        List<UserMoncardsVo> list1=new ArrayList();
        List<Parking> parkingList=parkingBiz.parkingIdInList(parkingIdList);
        List<Plate> plateList=plateBiz.PlaIdInList(plaIdList);
        for(UserMoncards userMoncards:userMoncardsList) {
            UserMoncardsVo userMoncardsVo = new UserMoncardsVo(userMoncards);
            for (Parking parking : parkingList) {
                if (parking.getParkingId().equals(userMoncards.getParkingId())) {
                    userMoncardsVo.setParking(parking);
                }
            }
            for (Plate plate : plateList) {
                if (plate.getPlaId().equals(userMoncards.getPlateId())) {
                    userMoncardsVo.setPlate(plate);
                }
            }
            list1.add(userMoncardsVo);
        }
        return list1;
    }

    public TableResultPageResponse<Object> queryMonthCards(Map params) {
        PageUtil.makeStartPoint(params);
        List<Object> moncards = userMoncardsMapper.queryMonthCards(params);
        return new TableResultPageResponse(userMoncardsMapper.queryMonthCardsCount(params),
                moncards,
                Long.parseLong(params.get("startPoint").toString()),
                Long.parseLong(params.get("limit").toString()) );
    }
}
