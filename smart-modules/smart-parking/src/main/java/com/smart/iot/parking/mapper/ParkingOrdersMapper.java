package com.smart.iot.parking.mapper;

import com.smart.iot.parking.entity.ParkingOrders;
import com.yuncitys.smart.parking.common.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 订单表
 *
 * @author Mr.AG
 * @email
 *@version 2022-08-07 14:53:11
 */
public interface ParkingOrdersMapper extends CommonMapper<ParkingOrders> {    String findMaxEndDateByLmId(@Param("lmId") String lmId);

    String findMaxStartDateByLpId(@Param("lpId") String lpId);

    BigDecimal queryRealMoneyByUserId(@Param("chargeId") String chargeId, @Param("chargeDate") String chargeDate);

    int queryOrderCountByUserId(@Param("chargeId") String chargeId, @Param("chargeDate") String chargeDate);

    List<ParkingOrders> queryPrivateUserAndParking(@Param("privateUserId") String privateUserId, @Param("parkingId") String parkingId);

    List<ParkingOrders> selectByParenIds(@Param("idList") String[] idList);

    int queryPrivateUserSpaceOrderCount(@Param("idList") String[] idList);

    List<ParkingOrders> queryPrivateUserSpaceOrder(@Param("idList") String[] idList, @Param("page") int page, @Param("limit") int limit);

    List<ParkingOrders> selectBySpaceIds(@Param("idList") List<String> idList, @Param("orderStatus") String orderStatus);

    List<Map> queryOrdersByPage(Map params);

    Long queryOrdersByCount(Map params);

    Long queryOrdersAllCount(Map params);

    List queryEveryDayCountByMouth(Map params);

    Map queryEveryMonthCountByYear (Map params);

    List queryOrdersTimeByMonth (Map params);

    List queryOrdersTimeByYear (Map params);

    List payTypeChartByMonth (Map params);

    List payTypeChartByYear (Map params);

    List totalRevenueDayCount (Map params);

    List totalRevenueMonthCount (Map params);

    Map userActiveTimes ();

    Long queryMonthCount(Map params);

    Long parkingTimesCount(Map params);

    Double totalRevenueByAllCount(Map params);

    Map queryOrdersTimeByAllCount(Map params);

    List countOrdersByBegin(Map params);

    List countOrdersByEnd(Map params);

    List sumMoneyByEnd(Map params);

    List<Map> queryDataByDate(Map params);
}
