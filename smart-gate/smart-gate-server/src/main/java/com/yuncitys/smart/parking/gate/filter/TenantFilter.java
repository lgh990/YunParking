package com.yuncitys.smart.parking.gate.filter;

import com.yuncitys.ag.core.context.BaseContextHandler;
import com.yuncitys.smart.parking.common.constant.RequestHeaderConstants;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

/**
 * 租户路径拦截
 * @author smart
 * @create 2018/2/9.
 */
@Component("atenantFilter")
@WebFilter(filterName = "atenantFilter", urlPatterns = {"/api"})
@Order(-2147483648)
@Slf4j
public class TenantFilter implements Filter {
    @Autowired
    private ZuulProperties zuulProperties;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String tenantFlag = ((HttpServletRequest) request).getHeader(RequestHeaderConstants.TENANT_FLAG);
        if(StringUtils.isNotBlank(tenantFlag)) {
            String urlPath = httpServletRequest.getRequestURI();
            urlPath = urlPath.substring(zuulProperties.getPrefix().length() + 1, urlPath.length());
            String realPath = urlPath.substring(urlPath.indexOf("/"), urlPath.length());
            log.info("请求路径:"+realPath);
            String tenant = urlPath.substring(0, urlPath.indexOf("/"));
            RequestContext ctx = RequestContext.getCurrentContext();
            // 将租户id放置请求头部传递后端
            ctx.addZuulRequestHeader(RequestHeaderConstants.TENANT,tenant);
            BaseContextHandler.set("tenant", tenant);
            chain.doFilter(new UriWrapperRequest((HttpServletRequest) request, zuulProperties.getPrefix() + realPath), response);
        }else{
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }

    class UriWrapperRequest extends HttpServletRequestWrapper {
        private String uri;

        public UriWrapperRequest(HttpServletRequest request, String uri) {
            super(request);
            this.uri = uri;
        }

        @Override
        public String getRequestURI() {
            return this.uri;
        }

        @Override
        public String getServletPath() {
            return this.uri;
        }
    }
}
