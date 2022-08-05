package com.smart.iot.parking.biz;

import com.smart.iot.parking.entity.Parking;
import com.smart.iot.parking.entity.ParkingArea;
import com.smart.iot.parking.entity.ParkingSpace;
import com.smart.iot.parking.mapper.ParkingAreaMapper;
import com.smart.iot.parking.srever.TablePageResultParser;
import com.smart.iot.parking.utils.PageUtil;
import com.smart.iot.parking.vo.ParkingAreaVo;
import com.yuncitys.smart.merge.annonation.MergeResult;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import com.yuncitys.smart.parking.common.msg.TableResultPageResponse;
import com.yuncitys.smart.parking.common.util.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 区层
 *
 * @author Mr.AG
 * @email
 *@version 2022-07-30 14:30:12
 */
@Service
public class ParkingAreaBiz extends BusinessBiz<ParkingAreaMapper,ParkingArea> {

    @Autowired
    public ParkingBiz parkingBiz;
    @Autowired
    private ParkingAreaMapper parkingAreaMapper;

    @MergeResult(resultParser = TablePageResultParser.class)
    @Override
    public TableResultPageResponse<ParkingArea> selectByPageQuery(Query query, String beginDate, String endDate)
    {
        return super.selectByPageQuery(query,  beginDate, endDate);
    }

    public List<ParkingAreaVo> parkingAreaToParkingAreaVo(List<ParkingArea> parkingAreaList){
        List<String> parkingIdList=new ArrayList<>();
        for(ParkingArea parkingArea:parkingAreaList){
            parkingIdList.add(parkingArea.getParkingId());
        }
        List<ParkingAreaVo> list=new ArrayList();
        List<Parking> parkingList=parkingBiz.parkingIdInList(parkingIdList);
        for(ParkingArea parkingArea:parkingAreaList){
            ParkingAreaVo parkingAreaVo=new ParkingAreaVo(parkingArea);
            for(Parking parking:parkingList){
                if(parking.getParkingId().equals(parkingArea.getParkingId())){
                    parkingAreaVo.setParking(parking);
                }
            }
            list.add(parkingAreaVo);
        }
        return list;
    }


    public List<ParkingArea> areaIdInList(List<String> areaIdList){
        if(areaIdList!=null && areaIdList.size()>0) {
            Example example = new Example(ParkingArea.class);
            example.createCriteria().andIn("areaId", areaIdList);
            return this.selectByExample(example);
        }
        return new ArrayList<>();
    }

    /**
     * 区层管理查询
     * @param params
     * @return
     */
    public TableResultPageResponse<Object> queryParkingArea(Map params) {
        PageUtil.makeStartPoint(params);
        List<Object> parkingAreas = parkingAreaMapper.queryParkingArea(params);
        return new TableResultPageResponse(parkingAreaMapper.queryParkingAreaCount(params),
                parkingAreas,Long.parseLong(params.get("startPoint").toString()),Long.parseLong(params.get("limit").toString()) );
    }

}
