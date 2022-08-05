package com.yuncitys.smart.parking.auth.module.client.controller;

import com.yuncitys.smart.parking.auth.module.client.service.AuthService;
import com.yuncitys.smart.parking.common.constant.RequestHeaderConstants;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.parking.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.parking.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author smart
 * @create 2018/3/27.
 */
@FrameworkEndpoint
public class RevokeTokenEndpoint {

    @Autowired
    @Qualifier("consumerTokenServices")
    ConsumerTokenServices consumerTokenServices;
    @Autowired
    private AuthService authService;

    @RequestMapping(method = RequestMethod.DELETE, value = "/oauth/token")
    @ResponseBody
    public ObjectRestResponse revokeToken(String access_token) throws Exception {
        String realToken = getRealToken(access_token);
        if (consumerTokenServices.revokeToken(realToken)){
            authService.invalid(realToken);
            return new ObjectRestResponse<Boolean>().data(true);
        }else{
            return new ObjectRestResponse<Boolean>().data(false);
        }
    }

    private String getRealToken(String originToken) {
        if (originToken != null && originToken.startsWith(RequestHeaderConstants.JWT_TOKEN_TYPE)) {
            originToken = originToken.substring(RequestHeaderConstants.JWT_TOKEN_TYPE.length(), originToken.length());
        }
        return originToken;
    }
}
