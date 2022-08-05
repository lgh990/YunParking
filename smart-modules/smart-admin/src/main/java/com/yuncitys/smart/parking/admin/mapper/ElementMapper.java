package com.yuncitys.smart.parking.admin.mapper;

import com.yuncitys.smart.parking.admin.entity.Element;
import com.yuncitys.smart.parking.common.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ElementMapper extends CommonMapper<Element> {
    public List<Element> selectAuthorityElementByUserId(@Param("userId")String userId, @Param("type") String type);
    public List<Element> selectAuthorityMenuElementByUserId(@Param("userId")String userId,@Param("menuId")String menuId, @Param("type") String type);
    public List<Element> selectAuthorityElementByClientId(@Param("clientId")String clientId, @Param("type") String type);
    public List<Element> selectAllElementPermissions();
}
