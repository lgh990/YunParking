package com.yuncitys.smart.parking.admin.biz;

import com.yuncitys.smart.parking.admin.entity.ResourceAuthority;
import com.yuncitys.smart.parking.admin.mapper.ResourceAuthorityMapper;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Smart on 2017/6/19.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ResourceAuthorityBiz extends BusinessBiz<ResourceAuthorityMapper,ResourceAuthority> {
    public void deleteByAuthorityIdAndResourceType(String s, String resourceTypeMenu, String type) {
        this.mapper.deleteByAuthorityIdAndResourceType(s,resourceTypeMenu,type);
    }
}
