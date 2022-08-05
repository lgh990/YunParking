package com.smart.iot.start.rest;

import com.smart.iot.start.util.RestResult;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("rest")
@CheckClientToken
@CheckUserToken
@Api(tags = "路侧巡检机")
public class RestDevController {


    @ApiOperation("路侧巡检机上传")
    @RequestMapping(value = "/identification",method = RequestMethod.GET)
    @ResponseBody
    public RestResult queryReserverAndParking(@RequestBody Map<String, Object> params){
        String deviceId=String.valueOf(params.get("device_id"));//识别设备 ID
        int collectionType=Integer.valueOf(String.valueOf(params.get("collection_type")));//设备类型，请根据设备具体分类填写。 1：车位管理设备； 2：违停违法抓拍设备。
        String spaceNumber=String.valueOf(params.get("space_number"));//泊位号。车位管理设备中此项是必须的。
        float recognitionRate=Float.valueOf(String.valueOf(params.get("recognition_rate")));//泊位置信度：此次识别的可信度
        String IdentifiedPlateNumber=String.valueOf(params.get("Identified_plate_number"));//车牌号码，若无车，传空，若无车牌，传临时牌照
        double IdentifiedTime=Double.valueOf(String.valueOf(params.get("Identified_time")));//照片抓拍时间戳，单位 秒
        int IdentifiedPlateColor=Integer.valueOf(String.valueOf(params.get("Identified_plate_color")));//识别的车牌颜色
        int IdentifiedPlateType=Integer.valueOf(String.valueOf(params.get("Identified_plate_type")));//识别的车牌种类
        String IdentifiedTaskId=String.valueOf(params.get("Identified_task_id"));//识别任务 ID,主要用于数据和图片分开上传时使用，客户端自定义不重复。
        String EventImgFlag=String.valueOf(params.get("EventImgFlag"));//是否包含事件图片。1 表示包含， 0 表示不包含。
        String EventImageFile=String.valueOf(params.get("EventImageFile"));//  事件图片数据以 Base64编码的值。
        String PlateImageFlag=String.valueOf(params.get("PlateImageFlag"));//是否包含号牌抠图图片。 1 表示包含， 0 表示不包含。
        String PlateImageFile=String.valueOf(params.get("PlateImageFile"));//号牌抠图图片数据以 Base64编码的值。
        float recognitionRatePlatetext=Float.valueOf(String.valueOf(params.get("recognition_rate_platetext")));//车牌号码置信度：此次识别的可信度
        int IdentifiedCapType=Integer.valueOf(String.valueOf(params.get("Identified_cap_type")));//抓拍类型。该字段在设备是违停违法抓拍设备时有用
        double Identified_red_time=Double.valueOf(String.valueOf(params.get("Identified_red_time")));//红灯后时间戳，单位秒。该字段在设备是违停违法抓拍设备时有用.
        int Identified_lane_id=Integer.valueOf(String.valueOf(params.get("Identified_lane_id")));//车道号，从左向右，从 1开始。该字段在设备是违停违法抓拍设备时有用.
        String Identified_point_name=String.valueOf(params.get("Identified_point_name"));//点位名称。该字段在设备是违停违法抓拍设备时有用
        int Identified_point_direction=Integer.valueOf(String.valueOf(params.get("Identified_point_direction")));//点位方向。该字段在设备是违停违法抓拍设备时有用.
        String Identified_illegal_locationid=String.valueOf(params.get("Identified_illegal_locationid"));//违法地点编号。该字段在设备是违停违法抓拍设备时有用.
        String Identified_illegal_roadid=String.valueOf(params.get("Identified_illegal_roadid"));//违法路段编号。该字段在设备是违停违法抓拍设备时有用.
        String Identified_illegal_areaid=String.valueOf(params.get("Identified_illegal_roadid"));//违法地区编号。该字段在设备是违停违法抓拍设备时有用.
        String Identified_illegal_lanetype=String.valueOf(params.get("Identified_illegal_lanetype"));//违法车道类型。该字段在设备是违停违法抓拍设备时有用.
        String Identified_devgroup_id=String.valueOf(params.get("Identified_devgroup_id"));//上传设备组编号。该字段在设备是违停违法抓拍设备时有用
        String Identified_user_id=String.valueOf(params.get("Identified_user_id"));//用户编码。该字段在设备是违停违法抓拍设备时有用.
        String Identified_gps_value=String.valueOf(params.get("Identified_gps_value"));//经纬度信息, 纬度在前，经度在后，若是南纬西经值，则 在 前 面 加 ”-“, 例 如“0.00000,0.00000”。该字段在设备是违停违法抓拍设备时有用.
        String Identified_illegal_code=String.valueOf(params.get("Identified_illegal_code"));//违法代码。该字段在设备是违停违法抓拍设备时有用
        RestResult restResult=new RestResult();
        return restResult.BaseResponse(0,"",0);
    }


}
