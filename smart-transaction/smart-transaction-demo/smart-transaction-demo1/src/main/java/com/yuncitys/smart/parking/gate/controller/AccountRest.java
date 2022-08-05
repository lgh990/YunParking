package com.yuncitys.smart.parking.gate.controller;

import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.gate.biz.AccountBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author smart
 *@version 2022/1/18.
 */
@RestController
@RequestMapping("/account")
@CheckUserToken
public class AccountRest {

    @Autowired
    private AccountBiz accountBiz;


    @RequestMapping("/test")
    public ObjectRestResponse test(){
        accountBiz.test();
        return new ObjectRestResponse();
    }
}
