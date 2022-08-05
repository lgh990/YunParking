package com.yuncitys.smart.parking.auth.client.exception;

/**
 * Created by smart on 2017/9/15.
 */
public class JwtTokenExpiredException extends Exception {
    public JwtTokenExpiredException(String s) {
        super(s);
    }
}
