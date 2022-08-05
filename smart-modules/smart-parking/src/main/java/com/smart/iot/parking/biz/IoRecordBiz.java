package com.smart.iot.parking.biz;

import com.smart.iot.parking.entity.IoRecord;
import com.smart.iot.parking.entity.Parking;
import com.smart.iot.parking.entity.ParkingIo;
import com.smart.iot.parking.entity.Plate;
import com.smart.iot.parking.mapper.IoRecordMapper;
import com.smart.iot.parking.vo.IoRecordVo;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 进出记录表
 *
 * @author Mr.AG
 * @email
 *@version 2022-08-16 17:32:12
 */
@Service
public class IoRecordBiz extends BusinessBiz<IoRecordMapper,IoRecord> {

    @Autowired
    public PlateBiz plateBiz;
    @Autowired
    public  ParkingBiz parkingBiz;
    @Autowired
    public ParkingIoBiz parkingIoBiz;

    public List<IoRecord> lpIdInList(List<String> lpIdList){
        if(lpIdList!=null && lpIdList.size()>0) {
            Example example = new Example(IoRecord.class);
            example.createCriteria().andIn("lrId", lpIdList);
            return this.selectByExample(example);
        }
        return new ArrayList<>();
    }

    public List<IoRecordVo> ioRecordListToIoRecordVo(List<IoRecord> list){
        List<String> parkingIdList=new ArrayList<>();
        List<String> plaIdList=new ArrayList<>();
        List<String> accinIdList=new ArrayList<>();
        List<String> exitIdList=new ArrayList<>();
        for(IoRecord ioRecord:list){
            parkingIdList.add(ioRecord.getParkingId());
            plaIdList.add(ioRecord.getPlateId());
            accinIdList.add(ioRecord.getAccinId());
            exitIdList.add(ioRecord.getExitId());
        }
        List<IoRecordVo> list1=new ArrayList();
        List<Parking> parkingList=parkingBiz.parkingIdInList(parkingIdList);
        List<Plate> plateList=plateBiz.PlaIdInList(plaIdList);
        List<ParkingIo> accinList=parkingIoBiz.ioIdInList(accinIdList);
        List<ParkingIo> exitList=parkingIoBiz.ioIdInList(exitIdList);
        for(IoRecord ioRecord:list) {
            IoRecordVo ioRecordVo = new IoRecordVo(ioRecord);
            for (Parking parking : parkingList) {
                if (parking.getParkingId().equals(ioRecord.getParkingId())) {
                    ioRecordVo.setParking(parking);
                }
            }
            for (Plate plate : plateList) {
                if (plate.getPlaId().equals(ioRecord.getPlateId())) {
                    ioRecordVo.setPlate(plate);
                }
            }
            for (ParkingIo accin : accinList) {
                if (accin.getParkingIoId().equals(ioRecord.getAccinId())) {
                    ioRecordVo.setAccinParkingIo(accin);
                }
            }
            for (ParkingIo exit : exitList) {
                if (exit.getParkingIoId().equals(ioRecord.getExitId())) {
                    ioRecordVo.setExitParkingIo(exit);
                }
            }
            list1.add(ioRecordVo);
        }
        return list1;
    }
}
