package com.yuncitys.smart.parking.common.biz;

import com.alibaba.fastjson.JSONObject;
import com.yuncitys.smart.merge.annonation.MergeResult;
import com.yuncitys.smart.parking.common.mapper.CommonMapper;
import com.yuncitys.smart.parking.common.msg.TableResultResponse;
import com.yuncitys.smart.parking.common.srever.TableResultParser;
import com.yuncitys.smart.parking.common.util.BeanUtils2;
import com.yuncitys.smart.parking.common.util.EntityUtils;
import com.yuncitys.smart.parking.common.util.Query;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 基础业务类
 *
 * @author smart
 *@version 2022/1/13.
 */
public abstract class BusinessBiz<M extends CommonMapper<T>, T> extends BaseBiz<M, T> {
    @Override
    public void updateById(T entity) {
        EntityUtils.setUpdatedInfo(entity);
        super.updateById(entity);
    }

    @Override
    public void insertSelective(T entity) {
        EntityUtils.setCreatAndUpdatInfo(entity);
        super.insertSelective(entity);
    }

    @Override
    public void updateSelectiveById(T entity) {
        EntityUtils.setUpdatedInfo(entity);
        super.updateSelectiveById(entity);
    }

    /**
     * 根据ID批量获取实体
     *
     * @param iDs
     * @return
     */
    public Map<String, String> getEntityByIds(String iDs) {
        if (org.apache.commons.lang3.StringUtils.isBlank(iDs)) {
            return new HashMap<>();
        }
        iDs = "'" + iDs.replaceAll(",", "','") + "'";
        List<T> entitys = this.mapper.selectByIds(iDs);
        BeanUtils2<T> beanUtils = new BeanUtils2<T>();
        Object primaryKeyValue = null;
        Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        Map<String, String> map = new HashMap<String, String>();
        for (T entity : entitys) {
            try {
                primaryKeyValue = beanUtils.getEntityPrimaryKeyValue(entity, entityClass);
                map.put(String.valueOf(primaryKeyValue), JSONObject.toJSONString(entity));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

}
