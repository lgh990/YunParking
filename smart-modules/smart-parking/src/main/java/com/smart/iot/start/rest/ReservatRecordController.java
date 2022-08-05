package com.smart.iot.start.rest;


import com.smart.iot.parking.biz.*;
import com.smart.iot.parking.entity.ReservatRecord;
import com.smart.iot.parking.vo.ReservatRecordVo;
import com.smart.iot.start.biz.ReservatRecordOSBiz;
import com.yuncitys.smart.merge.core.MergeCore;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.msg.TableResultPageResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import com.yuncitys.smart.parking.common.util.Query;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("reservatRecord")
@CheckClientToken
@CheckUserToken
@Api(tags = "车位预定记录")
public class ReservatRecordController extends BaseController<ReservatRecordBiz,ReservatRecord,String> {
    @Autowired
    public ParkingSpaceBiz parkingSpaceBiz;
    @Autowired
    public ParkingOrdersBiz parkingOrdersBiz;
    @Autowired
    public UserMoncardsBiz userMoncardsBiz;
    @Autowired
    public RabbitTemplate rabbitTemplate;
    @Autowired
    public UserParkingSpaceBiz userParkingSpaceBiz;
    @Autowired
    public ReservatRecordOSBiz reservatRecordBiz;
    @Autowired
    public ParkingBiz parkingBiz;
    @Autowired
    public MergeCore mergeCore;

    @Override
    public ObjectRestResponse<ReservatRecord> add(@RequestBody ReservatRecord reservatRecord){
        return reservatRecordBiz.add(reservatRecord);
    }
    @Override
    public ObjectRestResponse<ReservatRecord> update(@RequestBody ReservatRecord reservatRecord){
        return reservatRecordBiz.update(reservatRecord);
    }
    @ApiOperation("获取预定记录和停车场")
    @RequestMapping(value = "/queryReserverAndParking",method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<Object> queryReserverAndParking(@RequestParam Map<String, Object> params){
        //查询列表数据
        Query query = new Query(params);
        TableResultPageResponse<ReservatRecord> tableResultPageResponse = baseBiz.selectByPageQuery(query,null,null);
        int total = (int)tableResultPageResponse.getData().getTotal();
        ReservatRecordVo reservatRecordVo=null;
        if(total != 0)
        {
            List<ReservatRecord> reservatRecord = tableResultPageResponse.getData().getRows();
            List<ReservatRecordVo> reservatRecordVos= baseBiz.reservatRecordToReservatRecordVo(reservatRecord);
            reservatRecordVo=reservatRecordVos.get(0);
        }
        return new ObjectRestResponse<Object>().data(reservatRecordVo);
    }


}
