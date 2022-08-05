package com.yuncitys.smart.parking.admin.mapper;

import com.yuncitys.smart.parking.admin.entity.Depart;
import com.yuncitys.smart.parking.admin.entity.Group;
import com.yuncitys.smart.parking.admin.entity.Position;
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
public interface PositionMapper extends CommonMapper<Position> {
    /**
     * 批量删除岗位中得用户
     * @param positionId
     */
    void deletePositionUsers(String positionId);

    /**
     * 岗位增加用户
     * @param id
     * @param positionId
     * @param userId
     */
    void insertPositionUser(@Param("id")String id, @Param("positionId")String positionId, @Param("userId") String userId,@Param("tenantId") String tenantId);

    /**
     * 获取岗位关联的用户
     * @param positionId
     * @return
     */
    List<User> selectPositionUsers(String positionId);

    /**
     * 删除岗位关联的角色
     * @param positionId
     */
    void deletePositionGroups(String positionId);

    /**
     * 插入岗位关联的角色
     * @param id
     * @param positionId
     * @param groupId
     */
    void insertPositionGroup(@Param("id")String id, @Param("positionId")String positionId, @Param("groupId") String groupId,@Param("tenantId") String tenantId);

    /**
     * 获取岗位关联的角色
     * @param positionId
     * @return
     */
    List<Group> selectPositionGroups( @Param("positionId")String positionId);

    /**
     * 移除岗位下授权的部门
     * @param positionId
     */
    void deletePositionDeparts(String positionId);

    /**
     * 添加岗位下授权的部门
     * @param id
     * @param positionId
     * @param departId
     */
    void insertPositionDepart(@Param("id")String id, @Param("positionId")String positionId, @Param("departId") String departId,@Param("tenantId") String tenantId);

    /**
     * 获取岗位授权的部门
     * @param positionId
     * @return
     */
    List<Depart> selectPositionDeparts(String positionId);

    /**
     * 获取用户的流程岗位
     * @return
     */
    List<Position> selectUserFlowPosition(String userId);
}
