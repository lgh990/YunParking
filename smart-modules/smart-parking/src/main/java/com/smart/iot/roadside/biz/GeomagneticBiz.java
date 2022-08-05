package com.smart.iot.roadside.biz;

import com.smart.iot.parking.biz.AppUserBiz;
import com.smart.iot.parking.biz.ParkingBiz;
import com.smart.iot.parking.biz.ParkingSpaceBiz;
import com.smart.iot.parking.constant.BaseConstants;
import com.smart.iot.parking.entity.AppUser;
import com.smart.iot.parking.entity.Parking;
import com.smart.iot.parking.entity.ParkingOrders;
import com.smart.iot.parking.entity.ParkingSpace;
import com.smart.iot.parking.mapper.ParkingOrdersMapper;
import com.smart.iot.parking.mapper.ParkingSpaceMapper;
import com.yuncitys.smart.parking.common.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GeomagneticBiz {
    @Autowired
    public ParkingSpaceMapper parkingSpaceMapper;
    @Autowired
    public ParkingSpaceBiz parkingSpaceBiz;
    @Autowired
    public AppUserBiz appUserBiz;
    @Autowired
    public ParkingOrdersRSBiz parkingOrdersRSBiz;
    @Autowired
    public ParkingOrdersMapper parkingOrdersMapper;
    @Autowired
    public ParkingBiz parkingBiz;
    //地磁失联过长异常处理
    public void monitorDevGeomagnetic() {
        //查询失联车位
        List<ParkingSpace> parkingSpaces = parkingSpaceMapper.queryOutOfContactSpace();
        List<String> idList = new ArrayList<String>();
        for (int i = 0; i < parkingSpaces.size(); i++) {
            ParkingSpace space = parkingSpaces.get(i);
//            idList.set(i, space.getSpaceId());
            idList.add(space.getSpaceId());
            space.setSpaceStatus(BaseConstants.errorType.communication);
            parkingSpaceBiz.saveOrUpdate(space);
        }
//        Example example = new Example(ParkingOrders.class);
//        example.createCriteria().andEqualTo("orderStatus", BaseConstants.OrderStatus.running);
//        example.createCriteria().andIn("spaceId", idList);//该条件无效？没有按照该条件查询
//        List<ParkingOrders> parkingOrders = parkingOrdersRSBiz.selectByExample(example);
        List<ParkingOrders> parkingOrders = parkingOrdersMapper.selectBySpaceIds(idList,BaseConstants.OrderStatus.running);
        //查询车位下进行中订单
        ParkingSpace parkingSpace = null;
        for (ParkingOrders parkingOrders1 : parkingOrders) {
            parkingSpace=getOrderSpace(parkingOrders1,parkingSpaces);
//            if(parkingSpace==null){
//                log.
//                return;
//            }
            if (parkingSpace!=null && parkingSpace.getAttr2() == null) {
                parkingSpace.setAttr2("30");
            }
            long difTimeStap = DateUtil.getDiffTimeStamp(parkingSpace.getAttr1(), null);
            if (difTimeStap > (Integer.valueOf(parkingSpace.getAttr2())*3*60)) {//暂定超过3倍心跳周期截单,90分钟
                parkingSpace.setAttr2(null);
                parkingSpace.setAttr1(null);
                parkingOrders1.setOrderStatus(BaseConstants.OrderStatus.exception);
                parkingOrders1.setEndDate(DateUtil.getDateTime());
                parkingOrders1.setErrorType(BaseConstants.errorType.communication);
                parkingOrdersRSBiz.saveOrUpdate(parkingOrders1);
                //退款操作
                if (parkingOrders1.getPayStatus().equals(BaseConstants.status.success)) {
                    AppUser appUser = appUserBiz.selectById(parkingOrders1.getUserId());
                    appUser.setMoney(appUser.getMoney().add(parkingOrders1.getRealMoney()));
                    appUserBiz.saveOrUpdate(appUser);
                    Parking parking = parkingBiz.selectById(parkingOrders1.getParkingId());
                    parking.setParkingRevenue(parking.getParkingRevenue().subtract(parkingOrders1.getRealMoney()));
                    parkingBiz.saveOrUpdate(parking);
                }

            }
        }

    }

    private ParkingSpace getOrderSpace(ParkingOrders parkingOrders,List<ParkingSpace> parkingSpaces){
        String spaceId=parkingOrders.getSpaceId();
        for(ParkingSpace parkingSpace:parkingSpaces){
            if(parkingSpace.getSpaceId().equals(spaceId)){
                return parkingSpace;
            }
        }
        return null;
    }
}
