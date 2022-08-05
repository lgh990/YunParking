package com.yuncitys.smart.gate.ratelimit.config;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by smart on 2017/9/23.
 */
public interface IUserPrincipal {
    String getName(HttpServletRequest request);
}
