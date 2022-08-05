package com.yuncitys.smart.parking.admin.mapper;

import com.yuncitys.smart.parking.admin.entity.Depart;
import com.yuncitys.smart.parking.admin.entity.User;
import com.yuncitys.smart.parking.common.data.Tenant;
import com.yuncitys.smart.parking.common.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 *
 * @author Mr.AG
 * @email
 *@version 2022-02-04 19:06:43
 */
@Tenant
public interface DepartMapper extends CommonMapper<Depart> {

    List<User> selectDepartUsers(@Param("departId") String departId,@Param("userName") String userName);

    void deleteDepartUser(@Param("departId")String departId, @Param("userId") String userId);

    void insertDepartUser(@Param("id") String id, @Param("departId") String departId, @Param("userId") String userId,@Param("tenantId") String tenantId);

}
