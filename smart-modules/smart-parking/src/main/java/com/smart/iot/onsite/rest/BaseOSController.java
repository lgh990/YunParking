package com.smart.iot.onsite.rest;

import com.aliyuncs.exceptions.ClientException;
import com.smart.iot.parking.biz.LotMsgBiz;
import com.smart.iot.parking.biz.ParkingSpaceBiz;
import com.smart.iot.parking.biz.PlateBiz;
import com.smart.iot.parking.biz.PublishMsgBiz;
import com.smart.iot.parking.constant.BaseConstants;
import com.smart.iot.parking.entity.LotMsg;
import com.smart.iot.parking.entity.ParkingSpace;
import com.smart.iot.parking.entity.Plate;
import com.smart.iot.parking.mapper.LotMsgMapper;
import com.smart.iot.parking.utils.SendMsg;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Administrator on 2018/8/26 0026.
 */
@RestController
@RequestMapping("base")
@CheckClientToken
@CheckUserToken
@Api(tags = "室内挪车")
public class BaseOSController {
    @Autowired
    public LotMsgBiz lotMsgBiz;
    @Autowired
    public PlateBiz plateBiz;
    @Autowired
    public ParkingSpaceBiz parkingSpaceBiz;
    @Autowired
    public PublishMsgBiz publishMsgBiz;
    @Autowired
    public LotMsgMapper lotMsgMapper;

    @ApiOperation("挪车")
    @RequestMapping(value = "/moveCar",method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<ParkingSpace> moveCar(@RequestBody ParkingSpace parkingSpaceEx){
        LotMsg lotMsg1 = lotMsgMapper.queryRunLotMsgBySpaceId(parkingSpaceEx.getSpaceId());
        ParkingSpace parkingSpace = parkingSpaceBiz.selectById(parkingSpaceEx.getSpaceId());
        if (lotMsg1 != null &&parkingSpace.getLotType().equals(BaseConstants.enabledFlag.y))
        {
            if (parkingSpace.getSpaceType().equals(BaseConstants.SpaceType.temporary) && !StringUtil.isBlank(lotMsg1.getPhone()))
            {
                if(lotMsg1.getPlateId() != null)
                {
                    Plate plate = plateBiz.selectById(lotMsg1.getPlateId());
                    try {
                        SendMsg.singlecall(lotMsg1.getPhone(), plate.getCarNumber());
                    } catch (ClientException e) {
                        e.printStackTrace();
                    }
                }
                return ObjectRestResponse.ok();
            }
            else
            {
                return new ObjectRestResponse().BaseResponse(BaseConstants.StateConstates.IS_NOT_TEMPORARY_CODE, BaseConstants.StateConstates.IS_NOT_TEMPORARY_MSG);
            }
        }
        else
        {
            return new ObjectRestResponse().BaseResponse(BaseConstants.StateConstates.IS_NOT_CAR_CODE, BaseConstants.StateConstates.IS_NOT_CAR_MSG);
        }
    }

}
