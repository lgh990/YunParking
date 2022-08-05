package com.smart.iot.onsite.rest;

import com.smart.iot.onsite.biz.ParkingOrdersOSBiz;
import com.smart.iot.onsite.feign.ToolFeign;
import com.smart.iot.parking.biz.ParkingOrdersBiz;
import com.smart.iot.parking.entity.AppUser;
import com.smart.iot.parking.entity.ParkingOrders;
import com.smart.iot.parking.entity.UserParkingSpace;
import com.smart.iot.parking.utils.BASE64DecodedMultipartFile;
import com.smart.iot.parking.utils.JsonUtil;
import com.smart.iot.parking.utils.ioUtil;
import com.smart.iot.parking.vo.ParkingOrdersVo;
import com.smart.iot.roadside.biz.ParkingOrdersRSBiz;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.auth.client.annotation.IgnoreUserToken;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.msg.TableResultPageResponse;
import com.yuncitys.smart.parking.common.msg.TableResultResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import com.yuncitys.smart.parking.common.util.DateUtil;
import com.yuncitys.smart.parking.common.util.Query;
import com.yuncitys.smart.parking.common.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("parkingOrdersOS")
@CheckClientToken
@CheckUserToken
@Slf4j
@Api(tags = "室内停车订单")
public class ParkingOrdersOSController extends BaseController<ParkingOrdersOSBiz,ParkingOrders,String> {
    @Autowired
    public ParkingOrdersOSBiz parkingOrdersOsBiz;
    @Autowired
    public ToolFeign toolFeign;



    @ApiOperation("购买月卡")
    @RequestMapping(value = "/buyMonthCard",method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<AppUser> buyMonthCard(@RequestBody ParkingOrders parkingOrders) throws Exception {
        return parkingOrdersOsBiz.buyMonthCard(parkingOrders);
    }


    @ApiOperation("出入口业务逻辑")
    @RequestMapping(value = "/ioManager", method = RequestMethod.POST)
    @IgnoreUserToken
    public void ioManager(String camera_id, String park_id, String color, String car_plate, String picture, HttpServletRequest request, HttpServletResponse response,String type) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("camera_id",camera_id);
        map.put("park_id",park_id);
        map.put("color",color);
        map.put("car_plate",car_plate);
        map.put("picture",picture);
        map.put("type",type);
        parkingOrdersOsBiz.ioBusiness(map);
    }

    @ApiOperation("出入口业务逻辑")
    @RequestMapping(value = "/ioManagerDh", method = RequestMethod.POST)
    @IgnoreUserToken
    public void ioManagerDh(@RequestBody String params) throws Exception {
        Map maps= JsonUtil.JsonStringToMap(params);
        Map picture= (Map) maps.get("Picture");
        if(picture!=null) {
            /*Map normalPic=(Map)picture.get("NormalPic");//大图片
            Map cutoutPic=(Map)picture.get("CutoutPic");//小图片
            Map snapInfo = (Map) picture.get("SnapInfo");//摄像头信息
            Map plate = (Map) picture.get("Plate");//车牌信息
            String plateColor = String.valueOf(plate.get("PlateColor"));//车牌颜色
            String plateNumber = String.valueOf(plate.get("PlateNumber"));//车牌号
            String plateType = String.valueOf(plate.get("PlateType"));//车牌类型
            String deviceId = String.valueOf(snapInfo.get("DeviceID"));//设备sn
            String content=String.valueOf(normalPic.get("Content"));//大图片base64
            String cutoutContent=String.valueOf(cutoutPic.get("Content"));//小图片base64
            Calendar now = Calendar.getInstance();
            int year= now.get(Calendar.YEAR);
            int month= (now.get(Calendar.MONTH) + 1);
            ObjectRestResponse pictures=new ObjectRestResponse();
            if(!StringUtil.isEmpty(content)) {
                pictures = toolFeign.imageUpload(content, year + "_" + month + "/" + plateNumber);
            }
           ObjectRestResponse fragmentPicure=new ObjectRestResponse();
            if(!StringUtil.isEmpty(content)) {
                fragmentPicure = toolFeign.imageUpload(cutoutContent, year + "_" + month + "/" + plateNumber+"/fragmentStr");
            }
            int type=ioUtil.getCarIsTtFreerDh(plateType);
            String color=ioUtil.getCarTypeByPlateColorDh(plateColor);
            Map<String, Object> map = new HashMap<>();
            map.put("camera_id", deviceId);
            map.put("color", color);
            map.put("car_plate", plateNumber);
            map.put("picture",pictures.getData());
            map.put("fragmentPicure",fragmentPicure.getData());
            map.put("type", type);
            parkingOrdersOsBiz.ioBusiness(map);*/
        }else{
            //心跳
        }
    }



    @ApiOperation("出入口业务逻辑")
    @RequestMapping(value = "/ioManagerZs", produces = "application/json", method = RequestMethod.POST)
    @IgnoreUserToken
    synchronized public String ioManagerZs(@RequestBody String params) throws Exception {
        log.info("=================================摄像头拍到====================");
            Map maps = JsonUtil.JsonStringToMap(params);
            Map alarmGioInMap = (Map) maps.get("AlarmInfoPlate");
            String serialno = String.valueOf(alarmGioInMap.get("serialno"));
            Map plateResultMap = (Map) ((Map) alarmGioInMap.get("result")).get("PlateResult");
            Map timeval= (Map) ((Map)plateResultMap.get("timeStamp")).get("Timeval");
            long time=Long.valueOf(String.valueOf(timeval.get("sec")));
            String date=DateUtil.format(new Date(time));
            log.info("=================================摄像头发送的时间===================="+date);
            int colorType = Integer.valueOf(String.valueOf(plateResultMap.get("colorType")));
            //车牌类型 0：未知车牌:、1：蓝牌小汽车、2：:黑牌小汽车、3：单排黄牌、4：双排黄牌、 5：警车车牌、6：武警车牌、
            // 7：个性化车牌、8：单排军车牌、9：双排军车牌、10：使馆车牌、11：香港进出中国大陆车牌、12：农用车牌、
            // 13：教练车牌、14：澳门进出中国大陆车牌、15：双层武警车牌、16：武警总队车牌、17：双层武警总队车牌、18：民航车牌、19：新能源车牌
            int type = Integer.valueOf(String.valueOf(plateResultMap.get("type")));
            type = ioUtil.getCarIsTtFreer(type);
            String color = ioUtil.getCarTypeByPlateColor(colorType);
            String license = String.valueOf(plateResultMap.get("license"));
            String imageStr = String.valueOf(plateResultMap.get("imageFile"));
            String fragmentStr = String.valueOf(plateResultMap.get("imageFragmentFile"));
            log.info("=================================车牌号:" + license);
            Map map = new HashMap();
            map.put("camera_id", serialno);
            map.put("color", color);
            map.put("car_plate", license);
            map.put("picture", imageStr);
            map.put("fragmentPicure", fragmentStr);
            map.put("type", type);
            parkingOrdersOsBiz.ioBusiness(map);
            return "{\"Response_AlarmInfoPlate\":{\"info\":\"ok\",\"plateid\":" + license + ",\"channelNum\":0,\"manualTrigger\":\"ok\",\"is_pay\":\"true\",\"serialData\":[{\"serialChannel\":0,\"data\":\"...\",\"dataLen\":123}]}}";
    }
    /*@ApiOperation("出入口业务逻辑")
    @RequestMapping(value = "/ioManagerZsss", produces = "application/json", method = RequestMethod.POST)
    @IgnoreUserToken
    @ResponseBody
    public String ioManagerZsss( HttpServletRequest params) throws Exception {
        Map maps= new HashMap();
        maps.put("picture","");
        return "{\"Response_AlarmInfoPlate\":{\"manualTrigger\":\"ok\"}}" ;

    }*/
   /* @ApiOperation("出入口业务逻辑")
    @RequestMapping(value = "/ioManagerZss", produces = "application/json", method = RequestMethod.POST)
    @IgnoreUserToken
    @ResponseBody
    public String ioManagerZss( HttpServletRequest params) throws Exception {
        Map maps= new HashMap();
        maps.put("picture","");
        return "{\n" +
                "    \"Response_AlarmInfoPlate\": \n" +
                "    {\n" +
                "        \"manualTrigger\" : \"ok\",//回复ok进行手动触发\n" +
                "        // ....其他数据\n" +
                "    }\n" +
                "}\n";
    }*/

    @RequestMapping(value = "placeVipReservatOrder",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("室内vip预付")
    public ObjectRestResponse<Object> placeVipReservatOrder(@RequestBody ParkingOrders parkingOrders){
        return parkingOrdersOsBiz.placeVipReservatOrder(parkingOrders);
    }
    @RequestMapping(value = "queryPrivateUserAndParking",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("查询业主用户和停车场")
    public TableResultResponse queryPrivateUserAndParking(@RequestBody ParkingOrders parkingOrders) throws ExecutionException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {
        return parkingOrdersOsBiz.queryPrivateUserAndParking(parkingOrders);
    }

    @RequestMapping(value = "queryPrivateUserSpaceOrder",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("查询业主用户车位收入")
    public TableResultPageResponse queryPrivateUserSpaceOrder(@RequestParam Map<String, Object> params){
        return parkingOrdersOsBiz.queryPrivateUserSpaceOrder(params);
    }

}
