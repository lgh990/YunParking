package com.smart.iot.parking.biz;

import com.smart.iot.parking.constant.BaseConstants;
import com.smart.iot.parking.entity.*;
import com.smart.iot.parking.mapper.LotMsgMapper;
import com.smart.iot.parking.mapper.ParkingMapper;
import com.smart.iot.parking.mapper.ParkingSpaceMapper;
import com.smart.iot.parking.srever.TablePageResultParser;
import com.smart.iot.parking.utils.PageUtil;
import com.smart.iot.parking.vo.ParkingSpaceVo;
import com.yuncitys.smart.merge.annonation.MergeResult;
import com.yuncitys.smart.merge.core.MergeCore;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.msg.TableResultPageResponse;
import com.yuncitys.smart.parking.common.util.Query;
import com.yuncitys.smart.parking.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 车位表
 *
 * @author Mr.AG
 * @email
 *@version 2022-07-30 14:30:12
 */
@Transactional
@Service
public class ParkingSpaceBiz extends BusinessBiz<ParkingSpaceMapper,ParkingSpace> {
    @Autowired
    public ParkingMapper parkingMapper;
    @Autowired
    public ParkingBiz parkingBiz;
    @Autowired
    public ParkingSpaceBiz parkingSpaceBiz;
    @Autowired
    public ParkingAreaBiz parkingAreaBiz;
    @Autowired
    public MergeCore mergeCore;
    @Autowired
    public AppUserBiz appUserBiz;
    @Autowired
    public UserParkingSpaceBiz userParkingSpaceBiz;
    @Autowired
    private ParkingSpaceMapper parkingSpaceMapper;
    @MergeResult(resultParser = TablePageResultParser.class)
    @Override
    public TableResultPageResponse<ParkingSpace> selectByPageQuery(Query query, String beginDate, String endDate)
    {
        return super.selectByPageQuery(query,  beginDate, endDate);
    }    //获取车位总数
    public Parking querySpaceLeftCount(Parking parking) {
        ParkingSpace parkingSpaceEx = new ParkingSpace();
        parkingSpaceEx.setLotType(BaseConstants.enabledFlag.n);
        parkingSpaceEx.setSpaceStatus(BaseConstants.errorType.normal);
        parkingSpaceEx.setParkingId(parking.getParkingId());
        long spaceCount = this.selectCount(parkingSpaceEx);
        parking.setLeftNum(String.valueOf(spaceCount));
        return parking;
    }

    public Parking querySpaceTotalCount(Parking parking) {
        Example example=new tk.mybatis.mapper.entity.Example(ParkingSpace.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("spaceStatus",BaseConstants.errorType.normal);
        criteria.andEqualTo("parkingId",parking.getParkingId());
        criteria.andNotEqualTo("abscissa",0);
        criteria.andNotEqualTo("ordinate",0);
        long spaceCount = this.selectCountByExample(example);
        parking.setTotalNum(String.valueOf(spaceCount));
        return parking;
    }
    public ObjectRestResponse<ParkingSpace> add(ParkingSpace parkingSpace) {
        String spaceNum = parkingSpace.getSpaceNum();
        String parkingId = parkingSpace.getParkingId();
        Parking parking = parkingBiz.selectById(parkingId);
        String uuid = StringUtil.uuid();
        parkingSpace.setSpaceId(uuid);
        String cityCode = parking.getCityId();
        Parking parking2 =parkingMapper.queryParkingBySpaceNum(spaceNum,cityCode);
        if(parking2 != null)
        {
            return new ObjectRestResponse().BaseResponse(BaseConstants.StateConstates.SPACE_EXIT_CODE,BaseConstants.StateConstates.SPACE_EXIT_MSG);
        }
        if(parkingSpace.getSpaceType().equals(BaseConstants.SpaceType.private1) && !StringUtil.isBlank(parkingSpace.getUserId()))
        {
            AppUser appUser = appUserBiz.selectById(parkingSpace.getUserId());
            if(appUser != null) {
                UserParkingSpace userParkingSpace = new UserParkingSpace();
                userParkingSpace.setId(StringUtil.uuid());
                userParkingSpace.setParkingId(parkingSpace.getParkingId());
                userParkingSpace.setUserId(parkingSpace.getUserId());
                userParkingSpace.setSpaceId(parkingSpace.getSpaceId());
                userParkingSpace.setRentalPeriod("1,2,3,4,5,6,7");
                userParkingSpace.setBeginDate("00:00");
                userParkingSpace.setEndDate("23:59");
                userParkingSpace.setAttr1(BaseConstants.enabledFlag.y);
                userParkingSpaceBiz.saveOrUpdate(userParkingSpace);
            }else
            {
                return new ObjectRestResponse().BaseResponse(BaseConstants.StateConstates.MESSAGE_FAIL_CODE,BaseConstants.StateConstates.MESSAGE_FAIL_MSG);
            }
        }
        this.insertSelective(parkingSpace);
        return new ObjectRestResponse<ParkingSpace>().data(parkingSpace);

    }

    public ObjectRestResponse<ParkingSpace> update(ParkingSpace parkingSpace) {
        if(parkingSpace.getVersion() == null)
        {
            ParkingSpace space = this.selectById(parkingSpace.getSpaceId());
            if(!StringUtil.isEmpty(parkingSpace.getSpaceNum())) {
                ParkingSpace spaceParams = new ParkingSpace();
                spaceParams.setSpaceNum(parkingSpace.getSpaceNum());
                ParkingSpace space1 = this.selectOne(spaceParams);
                if (space1!=null &&!space.getSpaceId().equals(space1.getSpaceId())) {
                    return new ObjectRestResponse().BaseResponse(BaseConstants.StateConstates.SPACENUM_ISIN_ID, BaseConstants.StateConstates.SPACENUM_ISIN_MSG);
                }
            }
            parkingSpace.setVersion(space.getVersion());
            if(parkingSpace.getParkingId() != null) {
                UserParkingSpace params = new UserParkingSpace();
                params.setSpaceId(parkingSpace.getSpaceId());
                if (space.getSpaceType().equals(BaseConstants.SpaceType.private1)) {
                    UserParkingSpace userParkingSpace = userParkingSpaceBiz.selectOne(params);
                    if (userParkingSpace != null) {
                        userParkingSpace.setUserId(parkingSpace.getUserId());
                        userParkingSpaceBiz.updateById(userParkingSpace);
                    } else {
                        userParkingSpace = new UserParkingSpace();
                        userParkingSpace.setBeginDate("00:00");
                        userParkingSpace.setEndDate("23:59");
                        userParkingSpace.setId(StringUtil.uuid());
                        userParkingSpace.setAttr1(BaseConstants.enabledFlag.y);
                        userParkingSpace.setUserId(parkingSpace.getUserId());
                        userParkingSpace.setParkingId(space.getParkingId());
                        userParkingSpace.setSpaceId(space.getSpaceId());
                        userParkingSpace.setRentalPeriod("1,2,3,4,5,6,7");
                        userParkingSpace.setEnabledFlag(BaseConstants.enabledFlag.y);
                        userParkingSpaceBiz.insertSelective(userParkingSpace);
                    }
                }
            }
        }
        this.updateSelectiveById(parkingSpace);
        return new ObjectRestResponse<ParkingSpace>().data(parkingSpace);
    }

    public List<ParkingSpace> spaceIdInList(List<String> spaceIdList){
        if(spaceIdList!=null && spaceIdList.size()>0) {
            Example example = new Example(ParkingSpace.class);
            example.createCriteria().andIn("spaceId", spaceIdList);
            return this.selectByExample(example);
        }
        return new ArrayList<>();
    }

    public List<ParkingSpaceVo> parkingSpaceListToParkingSpaceVoList(List<ParkingSpace> parkingSpaceList){
        List<String> areaIdList=new ArrayList<>();
        List<String> userIdList=new ArrayList<>();
        List<String> parkingIdList=new ArrayList<>();
        for(ParkingSpace parkingSpace:parkingSpaceList){
            userIdList.add(parkingSpace.getUserId());
            parkingIdList.add(parkingSpace.getParkingId());
            areaIdList.add(parkingSpace.getAreaId());
        }
        List<ParkingSpaceVo> list=new ArrayList();
        List<AppUser> userList=appUserBiz.userIdInList(userIdList);
        List<Parking> parkingList=parkingBiz.parkingIdInList(parkingIdList);
        List<ParkingArea> areaList=parkingAreaBiz.areaIdInList(areaIdList);
        for(ParkingSpace parkingSpace:parkingSpaceList){
            ParkingSpaceVo parkingSpaceVo=new ParkingSpaceVo(parkingSpace);
            for(AppUser appUser:userList){
                if(appUser.getId().equals(parkingSpace.getUserId())){
                    parkingSpaceVo.setAppUser(appUser);
                }
            }
            for(Parking parking:parkingList){
                if(parking.getParkingId().equals(parkingSpace.getParkingId())){
                    parkingSpaceVo.setParking(parking);
                }
            }
            for(ParkingArea parkingArea:areaList){
                if(parkingArea.getAreaId().equals(parkingSpace.getAreaId())){
                    parkingSpaceVo.setParkingArea(parkingArea);
                }
            }
            list.add(parkingSpaceVo);
        }
        return list;
    }


    public ParkingSpaceVo spaceToParkingSpaceVo(ParkingSpace parkingSpace){
        ParkingSpaceVo parkingSpaceVo=new ParkingSpaceVo(parkingSpace);
        Parking parking = parkingBiz.selectById(parkingSpace.getParkingId());
        ParkingArea parkingArea=parkingAreaBiz.selectById(parkingSpace.getAreaId());
        AppUser appUser=appUserBiz.selectById(parkingSpace.getUserId());
        parkingSpaceVo.setParkingArea(parkingArea);
        parkingSpaceVo.setParking(parking);
        if(appUser!=null) {
            parkingSpaceVo.setAppUser(appUser);
        }
        return parkingSpaceVo;
    }

    /**
     * 车位管理查询
     * @param params
     * @return
     */
    public TableResultPageResponse<Object> queryParkingSpace(Map params) {
        PageUtil.makeStartPoint(params);
        List<Object> parkingSpace = parkingSpaceMapper.queryParkingSpace(params);
        return new TableResultPageResponse(parkingSpaceMapper.queryParkingSpaceCount(params),
                parkingSpace,
                Long.parseLong(params.get("startPoint").toString()),
                Long.parseLong(params.get("limit").toString()) );
    }
}
