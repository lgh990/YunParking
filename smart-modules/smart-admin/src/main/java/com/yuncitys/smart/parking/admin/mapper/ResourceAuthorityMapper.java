package com.yuncitys.smart.parking.admin.mapper;

import com.yuncitys.smart.parking.admin.entity.ResourceAuthority;
import com.yuncitys.smart.parking.common.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;
public interface ResourceAuthorityMapper extends CommonMapper<ResourceAuthority> {
    public void deleteByAuthorityIdAndResourceType(@Param("authorityId")String authorityId,@Param("resourceType") String resourceType,@Param("type") String type);
}
