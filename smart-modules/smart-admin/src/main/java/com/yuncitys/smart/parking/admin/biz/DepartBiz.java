package com.yuncitys.smart.parking.admin.biz;

import com.alibaba.fastjson.JSONObject;
import com.yuncitys.ag.core.context.BaseContextHandler;
import com.yuncitys.smart.merge.annonation.MergeResult;
import com.yuncitys.smart.parking.admin.entity.Depart;
import com.yuncitys.smart.parking.admin.entity.User;
import com.yuncitys.smart.parking.admin.mapper.DepartMapper;
import com.yuncitys.smart.parking.admin.mapper.UserMapper;
import com.yuncitys.smart.parking.admin.service.TableResultParser;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import com.yuncitys.smart.parking.common.exception.base.BusinessException;
import com.yuncitys.smart.parking.common.msg.TableResultResponse;
import com.yuncitys.smart.parking.common.util.UUIDUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 *
 * @author Mr.AG
 * @email
 *@version 2022-02-04 19:06:43
 */
@Service
public class DepartBiz extends BusinessBiz<DepartMapper,Depart> {
    @Autowired
    private UserMapper userMapper;
    @MergeResult(resultParser = TableResultParser.class)
    public TableResultResponse<User> getDepartUsers(String departId,String userName) {
        List<User> users = this.mapper.selectDepartUsers(departId,userName);
        return new TableResultResponse<User>(users.size(),users);
    }

    public void addDepartUser(String departId, String userIds) {
        if (!StringUtils.isEmpty(userIds)) {
            String[] uIds = userIds.split(",");
            for (String uId : uIds) {
                this.mapper.insertDepartUser(UUIDUtils.generateUuid(),departId,uId, BaseContextHandler.getTenantID());
            }
        }
    }

    /**
     * 根据ID批量获取部门值
     * @param departIDs
     * @return
     */
    public Map<String,String> getDeparts(String departIDs){
        if(StringUtils.isBlank(departIDs)) {
            return new HashMap<>();
        }
        departIDs = "'"+departIDs.replaceAll(",","','")+"'";
        List<Depart> departs = mapper.selectByIds(departIDs);
        return departs.stream().collect(Collectors.toMap(Depart::getId, depart -> JSONObject.toJSONString(depart)));
    }

    public void delDepartUser(String departId, String userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if(user.getDepartId().equals(departId)){
            throw new BusinessException("无法移除用户的默认关联部门,若需移除,请前往用户模块更新用户部门!");
        }
        this.mapper.deleteDepartUser(departId,userId);
    }

    @Override
    public void insertSelective(Depart entity) {
        entity.setId(UUIDUtils.generateUuid());
        super.insertSelective(entity);
    }
}
