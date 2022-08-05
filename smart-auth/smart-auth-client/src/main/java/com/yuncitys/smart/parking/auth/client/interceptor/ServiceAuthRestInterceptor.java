package com.yuncitys.smart.parking.auth.client.interceptor;

import com.alibaba.fastjson.JSON;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.IgnoreClientToken;
import com.yuncitys.smart.parking.auth.client.config.ServiceAuthConfig;
import com.yuncitys.smart.parking.auth.client.jwt.ServiceAuthUtil;
import com.yuncitys.smart.parking.auth.common.util.jwt.IJWTInfo;
import com.yuncitys.smart.parking.common.constant.RestCodeConstants;
import com.yuncitys.smart.parking.common.exception.auth.ClientTokenException;
import com.yuncitys.smart.parking.common.msg.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 服务token认证
 *
 * @author smart
 * @version 2022/9/12
 */
@SuppressWarnings("ALL")
public class ServiceAuthRestInterceptor extends HandlerInterceptorAdapter {
    private Logger logger = LoggerFactory.getLogger(ServiceAuthRestInterceptor.class);

    @Autowired
    private ServiceAuthUtil serviceAuthUtil;

    @Autowired
    private ServiceAuthConfig serviceAuthConfig;

    private List<String> allowedClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        if (HttpMethod.OPTIONS.matches(method)){
            return super.preHandle(request, response, handler);
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 配置该注解，说明不进行服务拦截
        CheckClientToken annotation = handlerMethod.getBeanType().getAnnotation(CheckClientToken.class);
        IgnoreClientToken ignoreClientToken = handlerMethod.getMethodAnnotation(IgnoreClientToken.class);
        if (annotation == null) {
            annotation = handlerMethod.getMethodAnnotation(CheckClientToken.class);
        }
        if (annotation == null || ignoreClientToken != null) {
            return super.preHandle(request, response, handler);
        } else {
            String token = request.getHeader(serviceAuthConfig.getTokenHeader());
            try {
                IJWTInfo infoFromToken = serviceAuthUtil.getInfoFromToken(token);
                String uniqueName = infoFromToken.getUniqueName();
                for (String client : serviceAuthUtil.getAllowedClient()) {
                    if (client.equals(uniqueName)) {
                        return super.preHandle(request, response, handler);
                    }
                }
            }catch(ClientTokenException ex){
                response.setStatus(HttpStatus.FORBIDDEN.value());
                logger.error(ex.getMessage(),ex);
                response.setContentType("UTF-8");
                response.getOutputStream().println(JSON.toJSONString(new BaseResponse(ex.getStatus(), ex.getMessage())));
                return false;
            }
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.getOutputStream().println(JSON.toJSONString(new BaseResponse(RestCodeConstants.EX_CLIENT_FORBIDDEN_CODE,"Client is Forbidden!")));
            return false;
        }
    }
}
