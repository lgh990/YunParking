package com.yuncitys.smart.parking.common.biz;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yuncitys.smart.merge.annonation.MergeResult;
import com.yuncitys.smart.parking.common.mapper.CommonMapper;
import com.yuncitys.smart.parking.common.msg.TableResultPageResponse;
import com.yuncitys.smart.parking.common.msg.TableResultResponse;
import com.yuncitys.smart.parking.common.srever.TableResultParser;
import com.yuncitys.smart.parking.common.util.BeanUtils2;
import com.yuncitys.smart.parking.common.util.EntityUtils;
import com.yuncitys.smart.parking.common.util.Query;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Mr.AG
 * Date: 17/1/13
 * Time: 15:13
 * Version 1.0.0
 */

public abstract class BaseBiz<M extends CommonMapper<T>, T> {
    @Autowired
    protected M mapper;

    public void setMapper(M mapper) {
        this.mapper = mapper;
    }

    public T selectOne(T entity) {
        return mapper.selectOne(entity);
    }


    public T selectById(Object id) {
        return mapper.selectByPrimaryKey(id);
    }


    public List<T> selectList(T entity) {
        return mapper.select(entity);
    }


    public List<T> selectListAll() {
        return mapper.selectAll();
    }


    public Long selectCount(T entity) {
        return new Long(mapper.selectCount(entity));
    }

    public void insertSelective(T entity) {
        mapper.insertSelective(entity);
    }

    public void saveOrUpdate(List<T> entitys) {
         for(T entity : entitys)
         {
             saveOrUpdate(entity);
         }
    }

    public void saveOrUpdate(T entity) {
        BeanUtils2<T> beanUtils = new BeanUtils2<T>();
        Object primaryKeyValue = null;
        Class <T> entityClass = (Class <T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        try {
            primaryKeyValue = beanUtils.getEntityPrimaryKeyValue(entity,entityClass);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        T object = mapper.selectByPrimaryKey(primaryKeyValue);
        if(object == null)
        {
            EntityUtils.setCreatAndUpdatInfo(entity);
            mapper.insertSelective(entity);
        }else
        {
            EntityUtils.setUpdatedInfo(entity);
            mapper.updateByPrimaryKeySelective(entity);
        }
    }

    public void saveOrUpdate1(T entity) {
        BeanUtils2<T> beanUtils = new BeanUtils2<T>();
        Object primaryKeyValue = null;
        Class <T> entityClass = (Class <T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        try {
            primaryKeyValue = beanUtils.getEntityPrimaryKeyValue(entity,entityClass);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        T object = mapper.selectByPrimaryKey(primaryKeyValue);
        if(object == null)
        {
            EntityUtils.setCreatAndUpdatInfo1(entity);
            mapper.insertSelective(entity);
        }else
        {
            EntityUtils.setUpdatedInfo(entity);
            mapper.updateByPrimaryKeySelective(entity);
        }
    }

    public void delete(T entity) {
        mapper.delete(entity);
    }


    public void deleteById(Object id) {
        mapper.deleteByPrimaryKey(id);
    }


    public void updateById(T entity) {
        mapper.updateByPrimaryKey(entity);
    }


    public void updateSelectiveById(T entity) {
        mapper.updateByPrimaryKeySelective(entity);
    }

    public List<T> selectByExample(Object example) {
        return mapper.selectByExample(example);
    }

    public int selectCountByExample(Object example) {
        return mapper.selectCountByExample(example);
    }
    public TableResultResponse<T> selectByQuery(Query query) {
        Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        Example example = new Example(clazz);
        query2criteria(query, example);
        Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
        List<T> list = this.selectByExample(example);
        return new TableResultResponse<T>(result.getTotal(), list);
    }

    public TableResultResponse<T> selectByQuery(Query query,String beginDate,String endDate) {
        Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        Example example = new Example(clazz);
        Example.Criteria criteria=query2criteria1(query, example);
        if(StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate)) {
            criteria.andBetween("crtTime", beginDate, endDate);
        }
        example.orderBy("crtTime").desc();
        Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
        List<T> list = this.selectByExample(example);
        return new TableResultResponse<T>(result.getTotal(), list);
    }

    public TableResultPageResponse<T> selectByPageQuery(Query query, String beginDate, String endDate) {
        Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        Example example = new Example(clazz);
        Example.Criteria criteria=query2criteria1(query, example);
        if(StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate)) {
            criteria.andBetween("crtTime", beginDate, endDate);
        }
        example.orderBy("crtTime").desc();
        Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
        List<T> list = this.selectByExample(example);
        return new TableResultPageResponse<T>(result.getTotal(), list ,result.getPageNum(),result.getPageSize());
    }


    public void query2criteria(Query query, Example example) {
        if (query.entrySet().size() > 0) {
            Example.Criteria criteria = example.createCriteria();
            for (Map.Entry<String, Object> entry : query.entrySet()) {
                criteria.andLike(entry.getKey(), "%" + entry.getValue().toString() + "%");
            }
        }
    }

    public Example.Criteria query2criteria1(Query query, Example example) {
        Example.Criteria criteria=null;
        if (query.entrySet().size() > 0) {
            criteria = example.createCriteria();
            for (Map.Entry<String, Object> entry : query.entrySet()) {
                criteria.andLike(entry.getKey(), "%" + entry.getValue().toString() + "%");
            }
        }
        return criteria;
    }

}
