package com.yuncitys.smart.parking.app.filter;

import com.yuncitys.ag.core.context.BaseContextHandler;
import com.yuncitys.smart.parking.app.biz.AppUserBiz;
import com.yuncitys.smart.parking.app.oauth.service.OauthUserDetailsService;
import com.yuncitys.smart.parking.common.constant.RedisKeyConstants;
import com.yuncitys.smart.parking.common.constant.RequestHeaderConstants;
import com.yuncitys.smart.parking.common.constant.SMSConstants;
import com.yuncitys.smart.parking.common.constant.iotexper.SysConstant;
import com.yuncitys.smart.parking.common.util.RequestParamsToMap;
import com.yuncitys.smart.parking.common.util.Sha256PasswordEncoder;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.parking.core.userdetails.UsernameNotFoundException;
import org.springframework.parking.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.parking.web.util.matcher.AntPathRequestMatcher;
import org.springframework.parking.web.util.matcher.OrRequestMatcher;
import org.springframework.parking.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.xnio.Result;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 租户路径拦截
 * @author smart
 * @create 2018/2/9.
 */
@Component("atenantFilter")
@WebFilter(filterName = "atenantFilter", urlPatterns = {"/api"})
@Order(-2147483648)
@Log4j
public class TenantFilter implements Filter {
    private static final String OAUTH_TOKEN_URL = "/oauth/token";
    private RequestMatcher requestMatcher;
    @Autowired
    private OauthUserDetailsService oauthUserDetailsService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }
    public TenantFilter(){
        this.requestMatcher = new OrRequestMatcher(
                //new AntPathRequestMatcher(OAUTH_TOKEN_URL, "GET"),
                new AntPathRequestMatcher(OAUTH_TOKEN_URL, "POST")
        );
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String tenantId = httpServletRequest.getHeader(RequestHeaderConstants.TENANT);
        if(StringUtils.isNotBlank(tenantId)) {
            BaseContextHandler.setTenantID(tenantId);
        }
        Map<String, String>  paramMap = RequestParamsToMap.getParameterStringMap(httpServletRequest);
        if(requestMatcher.matches(httpServletRequest)){
            oauthUserDetailsService.verificationCodeClient(paramMap);
            RequestWrapper requestWrapper = new RequestWrapper(httpServletRequest);
            requestWrapper.pwdFilter(paramMap);
            chain.doFilter(requestWrapper, response);
        }else{
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
    class RequestWrapper extends HttpServletRequestWrapper {
        private Map<String, String[]> paramMap;

        RequestWrapper(HttpServletRequest request) {
            super(request);
            paramMap = new HashMap<>();
            paramMap.putAll(request.getParameterMap());
        }

        @Override
        public String getParameter(String name) {
            String[] values = paramMap.get(name);
            if (values == null || values.length == 0) {
                return null;
            }
            return values[0];
        }

        @Override
        public String[] getParameterValues(String name) {
            return paramMap.get(name);
        }

        @Override
        public Enumeration<String> getParameterNames() {
            return new Enumeration<String>() {
                private Iterator<String> iterator = paramMap.keySet().iterator();

                @Override
                public boolean hasMoreElements() {
                    return iterator.hasNext();
                }

                @Override
                public String nextElement() {
                    return iterator.next();
                }
            };
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            return paramMap;
        }

        private void resetParamMap(String key, String[] value) {
            paramMap.put(key, value);
        }

        /**
         *  处理密码
         */
        void pwdFilter(Map<String, String> param) {
            if(param.containsKey("auth_type") && param.containsKey("password") && param.containsKey("username")){
                String mobile = param.get("username");
                String password = param.get("password");
                String pwdR = redisTemplate.opsForValue().get(RedisKeyConstants.REDIS_APP_USER_PWD_KEY+mobile);
             /*   if(SysConstant.AUTH_TYPE.PWD_AUTH_TYPE.equals(param.get("auth_type"))){
                    if(StringUtils.isEmpty(pwdR) || !pwdR.equals(password)){
                        throw new UsernameNotFoundException("密码有误，请重新输入!");
                    }
                }else*/ if(SysConstant.AUTH_TYPE.SMS_AUTH_TYPE.equals(param.get("auth_type"))){
                    String smsCodeRedis = redisTemplate.opsForValue().get(RedisKeyConstants.REDIS_APP_USER_SMS_CODE_KEY+"_"+SMSConstants.TEMPLATECODE.LOGIN+"_"+mobile );
                    if(StringUtils.isEmpty(smsCodeRedis) || !smsCodeRedis.equals(password)){
                        throw new UsernameNotFoundException("验证码有误，请重新输入!");
                    }else{
                        redisTemplate.opsForValue().set(RedisKeyConstants.REDIS_APP_USER_PWD_KEY+mobile,smsCodeRedis);
                        pwdR = smsCodeRedis;
                    }
                }
                for (String key : paramMap.keySet()) {
                    String[] valueSet = paramMap.get(key);
                    if (null !=valueSet && valueSet.length > 0) {
                        if(SysConstant.AUTH_TYPE.SMS_AUTH_TYPE.equals(param.get("auth_type")) && key.equals("password")) {
                            valueSet[0] = pwdR;
                        }
                        resetParamMap(key, valueSet);
                    }
                }
            }
        }
    }
}
