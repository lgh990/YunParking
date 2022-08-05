package com.yuncitys.smart.parking.auth.interceptor;

import com.alibaba.fastjson.JSON;
import com.yuncitys.ag.core.constants.CommonConstants;
import com.yuncitys.ag.core.util.jwt.IJWTInfo;
import com.yuncitys.smart.parking.auth.configuration.UserConfiguration;
import com.yuncitys.smart.parking.auth.jwt.user.JwtTokenUtil;
import com.yuncitys.ag.core.context.BaseContextHandler;
import com.yuncitys.smart.parking.common.constant.RequestHeaderConstants;
import com.yuncitys.smart.parking.common.exception.auth.NonLoginException;
import com.yuncitys.smart.parking.common.msg.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author smart
 * @version 2022/9/10
 */
@Slf4j
public class UserAuthRestInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserConfiguration userConfiguration;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(userConfiguration.getUserTokenHeader());
        if (StringUtils.isEmpty(token)) {
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if (cookie.getName().equals(userConfiguration.getUserTokenHeader())) {
                        token = cookie.getValue();
                    }
                }
            }
        }
        token = token.replaceAll("%20"," ");
        if (token != null && token.startsWith(RequestHeaderConstants.JWT_TOKEN_TYPE)) {
            token = token.substring(RequestHeaderConstants.JWT_TOKEN_TYPE.length(), token.length());
        }
        try {
            IJWTInfo infoFromToken = jwtTokenUtil.getInfoFromToken(token);
            BaseContextHandler.setToken(token);
            BaseContextHandler.setUsername(infoFromToken.getUniqueName());
            BaseContextHandler.setName(infoFromToken.getName());
            BaseContextHandler.setUserID(infoFromToken.getId());
            BaseContextHandler.setDepartID(infoFromToken.getOtherInfo().get(CommonConstants.JWT_KEY_DEPART_ID));
            BaseContextHandler.setTenantID(infoFromToken.getOtherInfo().get(CommonConstants.JWT_KEY_TENANT_ID));
        } catch (NonLoginException ex) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            log.error(ex.getMessage(), ex);
            response.setContentType("UTF-8");
            response.getOutputStream().println(JSON.toJSONString(new BaseResponse(ex.getStatus(), ex.getMessage())));
            return false;
        }
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        BaseContextHandler.remove();
        super.afterCompletion(request, response, handler, ex);
    }
}
