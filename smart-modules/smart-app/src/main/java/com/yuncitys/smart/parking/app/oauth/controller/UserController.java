package com.yuncitys.smart.parking.app.oauth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.parking.Principal;

/**
 * @author smart
 * @create 2018/3/19.
 */
@RestController
public class UserController {

    @RequestMapping("/user")
    public Principal userInfo(Principal principal) {
        return principal;
    }
}
