package com.smart.iot.parking.rest;

import com.smart.iot.parking.biz.ParkingOrdersBiz;
import com.smart.iot.parking.biz.PlateBiz;
import com.smart.iot.parking.constant.BaseConstants;
import com.smart.iot.parking.entity.AppUser;
import com.smart.iot.parking.entity.ParkingOrders;
import com.smart.iot.parking.entity.Plate;
import com.smart.iot.parking.utils.VehicleServiceUtil;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.auth.client.annotation.IgnoreUserToken;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.msg.TableResultResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("plate")
@CheckClientToken
@CheckUserToken
@Api(tags = "车牌管理")
public class PlateController extends BaseController<PlateBiz,Plate,String> {
    @Autowired
    ParkingOrdersBiz parkingOrdersBiz;
    @IgnoreUserToken
    @ApiOperation("根据账户编号获取车牌信息")
    @RequestMapping(value = "/getUserPlateList", method = RequestMethod.POST)
    public TableResultResponse<Plate> getUserPlateList(@RequestBody Plate plate) {
        List<Plate> plateList=baseBiz.selectList(plate);
        return new TableResultResponse(plateList.size(),plateList);
    }

    @ApiOperation("添加车牌")
    @RequestMapping(value = "/addPlate", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse getUserPlateList(@RequestParam Map<String,String> params) {
        return baseBiz.addPlate(params);
    }

    @ApiOperation("(限行)获取城市接口")
    @ResponseBody
    @RequestMapping(value = "/queryCityList", method = RequestMethod.POST)
    public ObjectRestResponse queryCityList() {
        return baseBiz.queryCity();
    }

    @ApiOperation("城市限行查询接口")
    @RequestMapping(value = "/qureyCityLimitRow", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse qureyCityLimitRow(@RequestParam Map<String,String> params) {
        return baseBiz.qureyCityLimitRow(params);
    }


    @ApiOperation("驾驶证扣分查询")
    @RequestMapping(value = "/scoreDeductionInquiry", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse scoreDeductionInquiry(@RequestParam Map<String,String> params) {
        return baseBiz.scoreDeductionInquiry(params);
    }

    @ApiOperation("全国车辆违章记录查询 （支持新能源车、大型货车查询）")
    @RequestMapping(value = "/recordsOfViolations", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse recordsOfViolations(@RequestParam Map<String,String> params) {
        return baseBiz.recordsOfViolations(params);
    }

    @Override
    public ObjectRestResponse<Plate> update(@RequestBody Plate plate){
        Plate plate1 = baseBiz.selectById(plate.getPlaId());
        if("".equals(plate.getUserId()))
        {
            //判断用户是否存在进行中订单
            Example example = new Example(ParkingOrders.class);
            List<String> a = new ArrayList<String>();
            a.add("running");
            a.add("unpay");
            example.createCriteria().andIn("orderStatus",a).andEqualTo("plaId",plate1.getPlaId());
            example.createCriteria().andNotEqualTo("orderType","recharge");
            List<ParkingOrders> parkingOrders1 = parkingOrdersBiz.selectByExample(example);
            if(parkingOrders1.size() != 0)
            {
                return new ObjectRestResponse<AppUser>().BaseResponse(BaseConstants.StateConstates.CARNUM_IN_ORDER_CODE,BaseConstants.StateConstates.CARNUM_IN_ORDER_MSG);
            }
        }
        baseBiz.updateSelectiveById(plate);
        return new ObjectRestResponse<Plate>().data(plate);
    }

}
