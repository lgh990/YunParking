package com.smart.iot.parking.biz;

import com.smart.iot.parking.entity.AppUser;
import com.smart.iot.parking.entity.Parking;
import com.smart.iot.parking.entity.ParkingSpace;
import com.smart.iot.parking.entity.UserParkingSpace;
import com.smart.iot.parking.mapper.UserParkingSpaceMapper;
import com.smart.iot.parking.srever.TableResultParser;
import com.smart.iot.parking.vo.UserParkingSpaceVo;
import com.yuncitys.smart.merge.annonation.MergeResult;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import com.yuncitys.smart.parking.common.msg.TableResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户共享车位表
 *
 * @author Mr.AG
 * @email
 *@version 2022-08-16 14:17:02
 */
@Service
public class UserParkingSpaceBiz extends BusinessBiz<UserParkingSpaceMapper,UserParkingSpace> {
    @Autowired
    public UserParkingSpaceMapper userParkingSpaceMapper;
    @Autowired
    public ParkingBiz parkingBiz;
    @Autowired
    public AppUserBiz appUserBiz;
    @Autowired
    public ParkingSpaceBiz parkingSpaceBiz;
    public TableResultResponse queryParkingByUserId(String userId,String parkingId) {
        //查询列表数据
        List<UserParkingSpace> userParkingSpace1 = userParkingSpaceMapper.queryParkingByUserId(userId,parkingId);
        List<UserParkingSpaceVo> userParkingSpaceVoList = userParkingSpaceToUserParkingSpaceVo(userParkingSpace1);
        return new TableResultResponse(userParkingSpace1.size(),userParkingSpaceVoList);
    }

    public List<UserParkingSpaceVo> userParkingSpaceToUserParkingSpaceVo(List<UserParkingSpace> userParkingSpaceList){
        List<String> userIdList=new ArrayList<>();
        List<String> parkingIdList=new ArrayList<>();
        List<String> spaceIdList=new ArrayList<>();
        for(UserParkingSpace userParkingSpace:userParkingSpaceList){
            userIdList.add(userParkingSpace.getUserId());
            parkingIdList.add(userParkingSpace.getParkingId());
            spaceIdList.add(userParkingSpace.getSpaceId());
        }
        List<UserParkingSpaceVo> list=new ArrayList();
        List<AppUser> userList=appUserBiz.userIdInList(userIdList);
        List<Parking> parkingList=parkingBiz.parkingIdInList(parkingIdList);
        List<ParkingSpace> spaceList=parkingSpaceBiz.spaceIdInList(spaceIdList);
        for(UserParkingSpace userParkingSpace:userParkingSpaceList){
            UserParkingSpaceVo userParkingSpaceVo=new UserParkingSpaceVo(userParkingSpace);
            for(AppUser appUser:userList){
                if(appUser.getId().equals(userParkingSpace.getUserId())){
                    userParkingSpaceVo.setAppUser(appUser);
                }
            }
            for(Parking parking:parkingList){
                if(parking.getParkingId().equals(userParkingSpace.getParkingId())){
                    userParkingSpaceVo.setParking(parking);
                }
            }
            for(ParkingSpace space:spaceList){
                if(space.getSpaceId().equals(userParkingSpace.getSpaceId())){
                    userParkingSpaceVo.setParkingSpace(space);
                }
            }
            list.add(userParkingSpaceVo);
        }
        return list;
    }


}
