package com.yuncitys.smart.search.service;


import com.yuncitys.smart.parking.api.vo.search.IndexObject;
import com.yuncitys.smart.parking.common.msg.TableResultResponse;

/**
 * lucense 接口
 * @author smart
 */
public interface LuceneService {

    void save(IndexObject indexObject);

    void update(IndexObject indexObject);

    void delete(IndexObject indexObject);

    void deleteAll();

    TableResultResponse page(Integer pageNumber, Integer pageSize, String keyword);
}
