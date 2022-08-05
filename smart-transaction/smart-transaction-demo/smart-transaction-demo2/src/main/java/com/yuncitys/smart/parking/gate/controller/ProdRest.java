package com.yuncitys.smart.parking.gate.controller;

import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.ag.core.context.BaseContextHandler;
import com.yuncitys.smart.parking.gate.biz.ProdBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author smart
 *@version 2022/1/18.
 */
@RestController
@RequestMapping("/prod")
@Slf4j
@CheckUserToken
public class ProdRest {
    @Autowired
    private ProdBiz prodBiz;
    @RequestMapping("/test")
    public void test() throws InterruptedException {
        log.info(BaseContextHandler.getUserID());
        prodBiz.test();
    }
}
