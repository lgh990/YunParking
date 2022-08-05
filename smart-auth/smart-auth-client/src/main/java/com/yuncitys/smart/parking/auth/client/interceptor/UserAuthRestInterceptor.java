package com.yuncitys.smart.parking.auth.client.interceptor;

import com.alibaba.fastjson.JSON;
import com.yuncitys.ag.core.constants.CommonConstants;
import com.yuncitys.ag.core.context.BaseContextHandler;
import com.yuncitys.ag.core.util.jwt.IJWTInfo;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.auth.client.annotation.IgnoreUserToken;
import com.yuncitys.smart.parking.auth.client.config.UserAuthConfig;
import com.yuncitys.smart.parking.auth.client.jwt.UserAuthUtil;
import com.yuncitys.smart.parking.common.constant.RequestHeaderConstants;
import com.yuncitys.smart.parking.common.exception.auth.NonLoginException;
import com.yuncitys.smart.parking.common.msg.BaseResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户token拦截认证
 *
 * @author smart
 * @version 2022/9/10
 */
public class UserAuthRestInterceptor extends HandlerInterceptorAdapter {
    private Logger logger = LoggerFactory.getLogger(UserAuthRestInterceptor.class);

    @Autowired
    private UserAuthUtil userAuthUtil;

    @Autowired
    private UserAuthConfig userAuthConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        if (HttpMethod.OPTIONS.matches(method)){
            return super.preHandle(request, response, handler);
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 配置该注解，说明不进行用户拦截
        CheckUserToken annotation = handlerMethod.getBeanType().getAnnotation(CheckUserToken.class);
        IgnoreUserToken ignoreUserToken = handlerMethod.getMethodAnnotation(IgnoreUserToken.class);
        if (annotation == null) {
            annotation = handlerMethod.getMethodAnnotation(CheckUserToken.class);
        }
        if (annotation == null || ignoreUserToken != null) {
            return super.preHandle(request, response, handler);
        } else {
            String token = request.getHeader(userAuthConfig.getTokenHeader());

            if (StringUtils.isEmpty(token)) {
                if (request.getCookies() != null) {
                    for (Cookie cookie : request.getCookies()) {
                        if (cookie.getName().equals(userAuthConfig.getTokenHeader())) {
                            token = cookie.getValue();
                        }
                    }
                }
            }
            token = token.replaceAll("%20"," ");
            if (token != null && token.startsWith(RequestHeaderConstants.JWT_TOKEN_TYPE)) {
                token = token.substring(RequestHeaderConstants.JWT_TOKEN_TYPE.length(),token.length());
            }
            try {
                String tenantID = BaseContextHandler.getTenantID();
                IJWTInfo infoFromToken = userAuthUtil.getInfoFromToken(token);
                if(infoFromToken==null){
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setContentType("UTF-8");
                    response.getOutputStream().println(JSON.toJSONString(new BaseResponse(401, "User token signature error!")));
                    return false;
                }
                BaseContextHandler.setToken(token);
                BaseContextHandler.setUsername(infoFromToken.getUniqueName());
                BaseContextHandler.setName(infoFromToken.getName());
                BaseContextHandler.setUserID(infoFromToken.getId());
                BaseContextHandler.setDepartID(infoFromToken.getOtherInfo().get(CommonConstants.JWT_KEY_DEPART_ID));
                String userTenantId = infoFromToken.getOtherInfo().get(CommonConstants.JWT_KEY_TENANT_ID);
                if(StringUtils.isNoneBlank(tenantID)){
                    if(!tenantID.equals(userTenantId)){
                        throw new NonLoginException("用户不合法!");
                    }
                }
                BaseContextHandler.setTenantID(userTenantId);
            }catch(NonLoginException ex){
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                logger.error(ex.getMessage(),ex);
                response.setContentType("UTF-8");
                response.getOutputStream().println(JSON.toJSONString(new BaseResponse(ex.getStatus(), ex.getMessage())));
                return false;
            }

            return super.preHandle(request, response, handler);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        BaseContextHandler.remove();
        super.afterCompletion(request, response, handler, ex);
    }
}
