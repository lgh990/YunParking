package com.yuncitys.smart.parking.gate.biz;

import com.codingapi.tx.annotation.TxTransaction;
import com.yuncitys.smart.parking.common.biz.BaseBiz;
import com.yuncitys.smart.parking.gate.entity.Account;
import com.yuncitys.smart.parking.gate.feign.IProdFeign;
import com.yuncitys.smart.parking.gate.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 *
 * @author Mr.AG
 * @email
 *@version 2022-01-18 11:02:17
 */
@Service
public class AccountBiz extends BaseBiz<AccountMapper,Account> {
    @Autowired
    private IProdFeign prodFeign;
    @TxTransaction
    @Transactional
    public void test(){
        prodFeign.test();
        Account account = mapper.selectByPrimaryKey(1);
        Integer balance = account.getBalance();
        account.setBalance(balance-100);
        mapper.updateByPrimaryKey(account);
        int i =  1/0;
    }
}
