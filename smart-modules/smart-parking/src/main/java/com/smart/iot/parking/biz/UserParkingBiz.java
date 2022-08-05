package com.smart.iot.parking.biz;

import com.smart.iot.parking.constant.BaseConstants;
import com.smart.iot.parking.entity.Parking;
import com.smart.iot.parking.entity.UserParking;
import com.smart.iot.parking.feign.DepartFeign;
import com.smart.iot.parking.feign.UserFeign;
import com.smart.iot.parking.mapper.UserParkingMapper;
import com.smart.iot.parking.rest.UserParkingController;
import com.smart.iot.parking.srever.TablePageResultParser;
import com.smart.iot.parking.vo.UserParkingVo;
import com.yuncitys.smart.merge.annonation.MergeResult;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import com.yuncitys.smart.parking.common.msg.BaseResponse;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.msg.TableResultPageResponse;
import com.yuncitys.smart.parking.common.util.Query;
import com.yuncitys.smart.parking.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户与停车场关联
 *
 * @author Mr.AG
 * @email
 *@version 2022-07-31 16:25:16
 */
@Service
public class UserParkingBiz extends BusinessBiz<UserParkingMapper,UserParking> {
    @Autowired
    public UserFeign userFeign;
    @Autowired
    public UserParkingController userParkingController;
    @Autowired
    public  ParkingBiz parkingBiz;
/*
    @TxTransaction // 在整个事务的开头加
    @Transactional
    public ObjectRestResponse<UserParking> addUserParkingRole(Map<String, Object> params) {
        String parkingId = String.valueOf(params.get("parkingId"));
        String departId =  String.valueOf(params.get("departId"));
        params.remove("parkingId");
        Map<String, Object> user = userFeign.addUser(params);
        String userId= String.valueOf(user.get("userId"));
        UserParking userParking = new UserParking();
        userParking.setUserId(userId);
        userParking.setParkingId(Integer.valueOf(parkingId));
        userParkingController.add(userParking);
        return new ObjectRestResponse<UserParking>().data(userParking);
    }
*/

public BaseResponse addUserParkingRole(Map<String, Object> params) {
        String parkingId = String.valueOf(params.get("parkingId"));
        String userId= String.valueOf(params.get("userId"));
        if(StringUtil.isBlank(parkingId) || StringUtil.isBlank(userId))
        {
            return new BaseResponse(BaseConstants.StateConstates.PARAMETER_IS_EMPTY_ID,BaseConstants.StateConstates.PARAMETER_IS_EMPTY_MSG);
        }
        Example example = new Example(UserParking.class);
        example.createCriteria().andEqualTo("parkingId", parkingId).andEqualTo("userId", userId);
        List<UserParking> userParkings = this.selectByExample(example);
        if(userParkings.size() != 0)
        {
            return new BaseResponse(BaseConstants.StateConstates.MESSAGE_BIND_CODE,BaseConstants.StateConstates.MESSAGE_BIND_MSG);
        }
        UserParking userParking = new UserParking();
        userParking.setFpId(StringUtil.uuid());
        userParking.setUserId(userId);
        userParking.setParkingId(parkingId);
        userParkingController.add(userParking);
        return new ObjectRestResponse<UserParking>().data(userParking);
    }
    @MergeResult(resultParser = TablePageResultParser.class)
    @Override
    public TableResultPageResponse<UserParking> selectByPageQuery(Query query, String beginDate, String endDate)
    {
        return super.selectByPageQuery(query,  beginDate, endDate);
    }

    public List<String> queryParkingIdList(String userID){
        List<String>  parkingIdList=new ArrayList<>();
        Example nvExample = new Example(UserParking.class);
        nvExample.createCriteria().andEqualTo("userId", userID);
        List<UserParking> list=this.selectByExample(nvExample);
        for(UserParking userParking:list){
            parkingIdList.add(userParking.getParkingId());
        }
        return parkingIdList;
    }

    public List<UserParkingVo> userParkingToUserParkingVo(List<UserParking> userParkings){
        List<String> parkingIdList=new ArrayList();
        List<String> userIdList=new ArrayList();
        for(UserParking userParking:userParkings){
            parkingIdList.add(userParking.getParkingId());
            userIdList.add(userParking.getUserId());
        }
        List<Map> maps=userFeign.userInList(userIdList);
        List<Parking> parkingList=parkingBiz.parkingIdInList(parkingIdList);
        List<UserParkingVo> list=new ArrayList<>();
        for(UserParking userParking:userParkings){
            UserParkingVo userParkingVo=new UserParkingVo(userParking);
            for(Parking parking:parkingList) {
                if (userParking.getParkingId().equals(parking.getParkingId())) {
                    userParkingVo.setParking(parking);
                }
            }
            for(Map m:maps) {
                if (userParking.getUserId().equals(m.get("id"))) {
                    userParkingVo.setUser(m);
                }
            }
            list.add(userParkingVo);
        }
        return list;
    }
}
