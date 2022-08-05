package com.yuncitys.smart.parking.app.config;

import com.yuncitys.smart.parking.common.data.IUserDepartDataService;
import com.yuncitys.smart.parking.common.data.MybatisDataInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * 租户\部门数据隔离
 * @author smart
 * @create 2018/2/11.
 */
@Configuration
public class MybatisDataConfig {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @PostConstruct
    public void init(){
        /**
         * 有些mapper的某些方法不需要进行隔离，则可以在配置忽略，按逗号隔开.
         * 如:"com.yuncitys.smart.parking.admin.mapper.UserMapper.selectOne",表示该mapper下不进行租户隔离
         */
        sqlSessionFactory.getConfiguration().addInterceptor(new MybatisDataInterceptor(null));
    }
}
