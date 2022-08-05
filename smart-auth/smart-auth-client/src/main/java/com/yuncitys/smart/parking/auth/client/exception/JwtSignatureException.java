package com.yuncitys.smart.parking.auth.client.exception;

/**
 *
 * @author smart
 * @version 2022/9/15
 */
public class JwtSignatureException extends Exception {
    public JwtSignatureException(String s) {
        super(s);
    }
}
