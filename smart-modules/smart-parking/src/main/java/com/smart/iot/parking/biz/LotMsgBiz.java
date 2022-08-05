package com.smart.iot.parking.biz;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smart.iot.parking.constant.BaseConstants;
import com.smart.iot.parking.entity.LotMsg;
import com.smart.iot.parking.entity.Parking;
import com.smart.iot.parking.entity.ParkingSpace;
import com.smart.iot.parking.entity.Plate;
import com.smart.iot.parking.mapper.LotMsgMapper;
import com.smart.iot.parking.srever.TablePageResultParser;
import com.smart.iot.parking.utils.PageUtil;
import com.smart.iot.parking.vo.LotMsgVo;
import com.yuncitys.smart.merge.annonation.MergeResult;
import com.yuncitys.smart.merge.core.MergeCore;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.msg.TableResultPageResponse;
import com.yuncitys.smart.parking.common.util.DateUtil;
import com.yuncitys.smart.parking.common.util.Query;
import com.yuncitys.smart.parking.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 停车记录表
 *
 * @author Mr.AG
 * @email
 *@version 2022-08-08 11:32:56
 */
@Service
public class LotMsgBiz extends BusinessBiz<LotMsgMapper,LotMsg> {
    @Autowired
    public PublishMsgBiz publishMsgBiz;

    @MergeResult(resultParser = TablePageResultParser.class)
    @Override
    public TableResultPageResponse<LotMsg> selectByPageQuery(Query query, String beginDate, String endDate)
    {
        return super.selectByPageQuery(query,  beginDate, endDate);
    }
    @Autowired
    public LotMsgMapper lotMsgMapper;
    @Autowired
    public PlateBiz plateBiz;
    @Autowired
    private MergeCore mergeCore;
    @Autowired
    public ParkingBiz parkingBiz;
    @Autowired
    public ParkingSpaceBiz parkingSpaceBiz;
    public void updateLotMsg(String spaceId,String lotType,String date) {
        if (lotType.equals(BaseConstants.enabledFlag.y)) {
            LotMsg lotMsg = lotMsgMapper.queryRunLotMsgBySpaceId(spaceId);
            if (lotMsg == null) {
                if(date == null) {
                    date = DateUtil.getDateTime();
                }
                LotMsg lm = new LotMsg();
                lm.setLmId(StringUtil.uuid());
                lm.setBeginDate(date);
                lm.setSpaceId(spaceId);
                this.saveOrUpdate(lm);
            }
        }else if (lotType.equals(BaseConstants.enabledFlag.n)) {
            LotMsg lotMsg = lotMsgMapper.queryRunLotMsgBySpaceId(spaceId);
            if (lotMsg != null)
            {
                if(date == null) {
                    date = DateUtil.getDateTime();
                }
                String longTime = DateUtil.getDateDiff(lotMsg.getBeginDate(),date);
                lotMsg.setEndDate(date);
                lotMsg.setLotTime(longTime);
                this.saveOrUpdate(lotMsg);
            }
        }
    }
    public ObjectRestResponse<LotMsg> updateLotMsgPlateInfo(LotMsg lotMsg) {
        Plate plate = JSON.parseObject(lotMsg.getPlateId(), Plate.class);
        Plate plate1 = plateBiz.selectOne(plate);
        if (plate1 == null) {
            plate1 = new Plate();
            plate1.setPlaId(StringUtil.uuid());
            plate1.setCarNumber(plate.getCarNumber());
            //通过车牌颜色判断车的大小
            plate1.setCarType(plate.getCarType());
            plate.setPlaId(StringUtil.uuid());
            plateBiz.saveOrUpdate(plate);
        }
        lotMsg.setPlateId(plate1.getPlaId());
        this.updateSelectiveById(lotMsg);
        lotMsg.setPlateId(JSONObject.toJSONString(plate1));
        return new ObjectRestResponse<LotMsg>().data(lotMsg);
    }

    public List<LotMsgVo> queryRunLotMsgByParkingIdAndSpaceType(String parkingId) {
        List<LotMsg> lotMsgs = lotMsgMapper.queryRunLotMsgByParkingIdAndSpaceType(parkingId,BaseConstants.SpaceType.temporary);
        return ioRecordListToIoRecordVo(lotMsgs);
    }

    public List<LotMsgVo> ioRecordListToIoRecordVo(List<LotMsg> lotMsgList){
        List<String> parkingIdList=new ArrayList<>();
        List<String> spaceIdList=new ArrayList<>();
        List<String> plaIdList=new ArrayList<>();
        for(LotMsg lotMsg:lotMsgList){
            parkingIdList.add(lotMsg.getParkingId());
            plaIdList.add(lotMsg.getPlateId());
            spaceIdList.add(lotMsg.getSpaceId());
        }
        List<LotMsgVo> list1=new ArrayList();
        List<Parking> parkingList=parkingBiz.parkingIdInList(parkingIdList);
        List<Plate> plateList=plateBiz.PlaIdInList(plaIdList);
        List<ParkingSpace> spaceList=parkingSpaceBiz.spaceIdInList(spaceIdList);
        for(LotMsg lotMsg:lotMsgList) {
            LotMsgVo lotMsgVo = new LotMsgVo(lotMsg);
            for (Parking parking : parkingList) {
                if (parking.getParkingId().equals(lotMsg.getParkingId())) {
                    lotMsgVo.setParking(parking);
                }
            }
            for (Plate plate : plateList) {
                if (plate.getPlaId().equals(lotMsg.getPlateId())) {
                    lotMsgVo.setPlate(plate);
                }
            }
            for (ParkingSpace space : spaceList) {
                if (space.getSpaceId().equals(lotMsg.getSpaceId())) {
                    lotMsgVo.setParkingSpace(space);
                }
            }
            list1.add(lotMsgVo);
        }
        return list1;
    }

    /**
     * 路测出入口管理查询
     *
     * @param params
     * @return
     */
    public TableResultPageResponse<Object> queryLotMsg(Map params) {
        PageUtil.makeStartPoint(params);
        List<Object> lotMsgs = lotMsgMapper.queryLotMsgs(params);
        return new TableResultPageResponse(lotMsgMapper.queryLotMsgsCount(params),
                lotMsgs,Long.parseLong(params.get("startPoint").toString()),Long.parseLong(params.get("limit").toString()));
    }
}
