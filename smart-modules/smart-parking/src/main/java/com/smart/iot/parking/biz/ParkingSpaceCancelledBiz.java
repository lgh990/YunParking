package com.smart.iot.parking.biz;

import com.smart.iot.parking.constant.BaseConstants;
import com.smart.iot.parking.entity.ParkingSpace;
import com.smart.iot.parking.mapper.ParkingMapper;
import com.smart.iot.parking.mapper.ParkingSpaceMapper;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart.iot.parking.entity.ParkingSpaceCancelled;
import com.smart.iot.parking.mapper.ParkingSpaceCancelledMapper;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 *
 *车位注销申请记录
 * @author heyaohuan
 */
@Service
public class ParkingSpaceCancelledBiz extends BusinessBiz<ParkingSpaceCancelledMapper,ParkingSpaceCancelled> {

    @Autowired
    public ParkingMapper parkingMapper;

    @Autowired
    private ParkingSpaceMapper parkingSpaceMapper;

    public ObjectRestResponse<ParkingSpaceCancelled> add(ParkingSpaceCancelled parkingSpaceCancelled){

        String userId=parkingSpaceCancelled.getUserId();
        String parkingId=parkingSpaceCancelled.getParkingId();
        String spaceId=parkingSpaceCancelled.getSpaceId();

        if(StringUtil.isEmpty(userId)){
            return new ObjectRestResponse().BaseResponse(BaseConstants.StateConstates.PARAMETER_IS_EMPTY_ID,
                    "userId:"+BaseConstants.StateConstates.PARAMETER_IS_EMPTY_MSG);
        }
        if(StringUtil.isEmpty(parkingId)){
            return new ObjectRestResponse().BaseResponse(BaseConstants.StateConstates.PARAMETER_IS_EMPTY_ID,
                    "parkingId:"+BaseConstants.StateConstates.PARAMETER_IS_EMPTY_MSG);
        }
        if(StringUtil.isEmpty(spaceId)){
            Example example=new Example(ParkingSpace.class);
            example.createCriteria().andEqualTo("userId",userId).andEqualTo("parkingId",parkingId);
            List<ParkingSpace> cancelledList= parkingSpaceMapper.selectByExample(example);
            if(cancelledList==null){
                return new ObjectRestResponse().BaseResponse(BaseConstants.StateConstates.SPACE_NUMBER_ERRER_ID,
                        "没有查询到您的车位！");//没有车位
            }
            ParkingSpace parkingSpace=null;
            Example example2=new Example(ParkingSpaceCancelled.class);
            Example.Criteria criteria=example2.createCriteria();
            for(int i=0;i<cancelledList.size();i++){
                parkingSpace=cancelledList.get(i);
                parkingSpaceCancelled.setSpaceId(parkingSpace.getSpaceId());
                criteria.andEqualTo("spaceId",parkingSpaceCancelled.getSpaceId())
                        .andEqualTo("status",0)
                        .andEqualTo("userId",userId);
                List<ParkingSpaceCancelled> list=this.selectByExample(example2);
                if(list==null||list.size()<1){//已经提交审核
                    this.insertSelective(parkingSpaceCancelled);
                }
            }
        }else{
            Example example=new Example(ParkingSpace.class);
            example.createCriteria().andEqualTo("userId",userId)
                    .andEqualTo("parkingId",parkingId)
                    .andEqualTo("spaceId",spaceId);
            ParkingSpace parkingSpace=parkingSpaceMapper.selectOneByExample(example);
            if(parkingSpace!=null){
                Example example2=new Example(ParkingSpaceCancelled.class);
                example2.createCriteria().andEqualTo("spaceId",spaceId)
                        .andEqualTo("status",0)
                        .andEqualTo("userId",userId);
                List<ParkingSpaceCancelled> list=this.selectByExample(example2);
                if(list==null||list.size()<1){//已经提交审核
                    this.insertSelective(parkingSpaceCancelled);
                }else{
                    return new ObjectRestResponse().BaseResponse(BaseConstants.StateConstates.IS_HAVE_ID,
                            BaseConstants.StateConstates.IS_HAVE_MSG);//已存在记录
                }
            }else{
                return new ObjectRestResponse().BaseResponse(BaseConstants.StateConstates.SPACE_NUMBER_ERRER_ID,
                        "没有查询到您的车位！");//没有车位
            }
        }
        return new ObjectRestResponse();
    }

}
