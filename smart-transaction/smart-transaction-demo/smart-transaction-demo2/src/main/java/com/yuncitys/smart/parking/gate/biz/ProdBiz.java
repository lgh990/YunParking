package com.yuncitys.smart.parking.gate.biz;

import com.yuncitys.smart.parking.common.biz.BaseBiz;
import com.yuncitys.smart.parking.gate.entity.Prod;
import com.yuncitys.smart.parking.gate.mapper.ProdMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 *
 * @author Mr.AG
 * @email
 *@version 2022-01-18 11:11:06
 */
@Service
public class ProdBiz extends BaseBiz<ProdMapper,Prod> {
    @Transactional
    public void test(){
        Prod prod = mapper.selectByPrimaryKey(1);
        prod.setNum(prod.getNum()-2);
        mapper.updateByPrimaryKey(prod);
    }
}
