package com.smart.iot.parking.feign;

import com.yuncitys.smart.parking.auth.client.config.FeignApplyConfiguration;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ${DESCRIPTION}
 *
 * @author smart
 * @version 2022-07-01 15:16
 */
@FeignClient(value = "smart-charge-server", configuration = FeignApplyConfiguration.class)
public interface ChargeRulesTypeFeign {
    @RequestMapping(value = "/chargeRulesType/queryByIds/{code}", method = RequestMethod.GET)
    public HashMap<String, Object> queryChargeRulesTypeByIds(@PathVariable("code") String code);

    @RequestMapping(value = "/chargeRulesType/queryCostByIdAndParkingId", method = RequestMethod.GET)
    public ObjectRestResponse<BigDecimal> queryCostByIdAndParkingId(@RequestParam Map<String, Object> params);

    @RequestMapping(value = "/chargeRulesType/getWebCode", method = RequestMethod.POST)
    public String getWebCode(@RequestParam("parkingId") String parkingId, @RequestParam("tableName") String tableName, @RequestParam("webCode")
            String webCode);

    @RequestMapping(value = "/chargeRulesType/idInList", method = RequestMethod.POST)
    public List<Map> idInList(@RequestParam("idList") List<String> idList);


    @RequestMapping(value = "/chargeRulesType/addOrUpdateChargeRule", method = RequestMethod.POST)
    public ObjectRestResponse<Object> addOrUpdateChargeRule(@RequestBody Map<String, Object> params);

    @RequestMapping(value = "/chargeRulesType/queryByIdAndParkingId", method = RequestMethod.GET)
    public HashMap<String, Object> queryByIdAndParkingId(@RequestParam("parkingId") String parkingId, @RequestParam("chargeRuleId") String chargeRuleId);

}
