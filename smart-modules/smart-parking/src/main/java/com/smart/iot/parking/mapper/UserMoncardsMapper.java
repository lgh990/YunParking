package com.smart.iot.parking.mapper;

import com.smart.iot.parking.entity.UserMoncards;
import com.yuncitys.smart.parking.common.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author Mr.AG
 * @email
 *@version 2022-08-15 17:31:58
 */
public interface UserMoncardsMapper extends CommonMapper<UserMoncards> {

    List<UserMoncards> queryMonthCardByUserId(@Param("userId") String userId);

    List<Object> queryMonthCards(Map params);

    Integer queryMonthCardsCount(Map params);

    Long queryMonthCardsCountByUsing(Map params);
}
