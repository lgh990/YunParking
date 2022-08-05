package com.smart.iot.parking.biz;

import com.smart.iot.parking.constant.BaseConstants;
import com.smart.iot.parking.entity.AppUser;
import com.smart.iot.parking.entity.Plate;
import com.smart.iot.parking.entity.PlateAuditRecord;
import com.smart.iot.parking.mapper.PlateAuditRecordMapper;
import com.smart.iot.parking.srever.TablePageResultParser;
import com.smart.iot.parking.utils.SendMsg;
import com.smart.iot.parking.vo.PlateAuditRecordVo;
import com.yuncitys.smart.merge.annonation.MergeResult;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import com.yuncitys.smart.parking.common.msg.BaseResponse;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.msg.TableResultPageResponse;
import com.yuncitys.smart.parking.common.util.Query;
import com.yuncitys.smart.parking.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 车牌审核记录表
 *
 * @author Mr.AG
 * @email
 *@version 2022-08-08 14:20:33
 */
@Service
@Slf4j
@Transactional
public class PlateAuditRecordBiz extends BusinessBiz<PlateAuditRecordMapper,PlateAuditRecord> {

    @Autowired
    public PlateBiz plateBiz;
    @Autowired
    public PlateAuditRecordBiz plateAuditRecordBiz;
    @Autowired
    public AppUserBiz appUserBiz;
    public BaseResponse auditDrivLicense(PlateAuditRecord plateAuditRecord) throws Exception {
        String carType = plateAuditRecord.getCarType();
        if(carType.equals("2")){
            carType="auto";
        }else if(carType.equals("1")){
            carType="truck";
        }
        String status = plateAuditRecord.getResult();
        plateAuditRecord = plateAuditRecordBiz.selectById(plateAuditRecord.getRecordId());
        plateAuditRecord.setCarType(carType);
        String userId = plateAuditRecord.getUserId();
        AppUser user=appUserBiz.selectById(userId);
        if(status.equals(BaseConstants.AuditConstates.SUCCESS)){
            Plate aPlate = new Plate();
            aPlate.setCarNumber(plateAuditRecord.getCarNumber());
            Plate plate = plateBiz.selectOne(aPlate);
            if(plate == null){
                plate = new Plate();

            }else{
                String plateUserId =plate.getUserId();
                if(!StringUtil.isBlank(plateUserId)&& !"0".equals(plateUserId)) {
                    return new BaseResponse(BaseConstants.StateConstates.OCCUPY_PLATE_CODE, BaseConstants.StateConstates.OCCUPY_PLATE_MSG);
                }
            }
            plateAuditRecord.setResult(status);
            this.updateSelectiveById(plateAuditRecord);
            plate.setUserId(plateAuditRecord.getUserId());
            plate.setDefaultLicensePlate(BaseConstants.DefaultPlate.Y);
            plate.setCarNumber(plateAuditRecord.getCarNumber());
            log.info("行驶证审核成功");
            Plate defaultPlate = new Plate();
            defaultPlate.setUserId(userId);
            defaultPlate.setDefaultLicensePlate(BaseConstants.DefaultPlate.Y);
            Plate defaultPlate2 = plateBiz.selectOne(defaultPlate);
            if(defaultPlate2 != null)
            {
                plate.setDefaultLicensePlate(BaseConstants.DefaultPlate.N);
            }
            plate.setCarType(carType);
            if(StringUtil.isEmpty(plate.getPlaId())) {
                plate.setPlaId(StringUtil.uuid());
                plateBiz.insertSelective(plate);
            }else{
                plateBiz.updateById(plate);
            }
         /*   List<ParkingOrder> orders =orderRepository.findByPlaIdAndEnabledFlag(plate.getPlaId(),"Y");
            //当验证车牌成功时，判断之前是否存在手持机下单，如果存在则把订单重新绑定到用户名下
            for (ParkingOrder order :orders)
            {
                order.setUserId(userId);
                orderRepository.saveAndFlush(order);
            }*/
            SendMsg.carNumberSusses(user.getUserType(),plateAuditRecord.getCarNumber());
            return new ObjectRestResponse<Plate>().data(plate);
        }else{
/*
            UserInfo user = userInfoRepository.findByUserId(userId);
            SendMsg_NEW.free_single(user.getUserName(), df.format(order.getRealMoney()));
*/
            plateAuditRecord.setResult(status);
            plateAuditRecord.setEnabledFlag("y");
            plateAuditRecordBiz.updateById(plateAuditRecord);
            log.info("行驶证审核失败");
          SendMsg.carNumberFail(user.getUserType(),plateAuditRecord.getCarNumber());
            return new ObjectRestResponse<PlateAuditRecord>().data(plateAuditRecord);
        }
    }
    @MergeResult(resultParser = TablePageResultParser.class)
    @Override
    public TableResultPageResponse<PlateAuditRecord> selectByPageQuery(Query query, String beginDate, String endDate)
    {
        return super.selectByPageQuery(query,  beginDate, endDate);
    }

    /**
     * plateAuditRecordToMap
     * @param pateAuditRecordList
     * @return
     */
    public List<PlateAuditRecordVo> plateAuditRecordToPlateAuditRecordVo(List<PlateAuditRecord> pateAuditRecordList){
        List<String> userIdList=new ArrayList<>();
        for(PlateAuditRecord plateAuditRecord:pateAuditRecordList){
            userIdList.add(plateAuditRecord.getUserId());
        }
        List<AppUser> appUserList=appUserBiz.userIdInList(userIdList);
        List<PlateAuditRecordVo> list=new ArrayList();
        for(PlateAuditRecord plateAuditRecord:pateAuditRecordList){
            PlateAuditRecordVo plateAuditRecordVo=new PlateAuditRecordVo(plateAuditRecord);
            for(AppUser appUser:appUserList){
                if(appUser.getId().equals(plateAuditRecord.getUserId())){
                    plateAuditRecordVo.setAppUser(appUser);
                }
            }
            list.add(plateAuditRecordVo);
        }
        return list;
    }
}
