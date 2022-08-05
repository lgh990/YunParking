package com.yuncitys.smart.parking.auth.client.interceptor;

import com.yuncitys.ag.core.context.BaseContextHandler;
import com.yuncitys.smart.parking.auth.client.config.ServiceAuthConfig;
import com.yuncitys.smart.parking.auth.client.config.UserAuthConfig;
import com.yuncitys.smart.parking.auth.client.jwt.ServiceAuthUtil;
import com.yuncitys.smart.parking.common.constant.RestCodeConstants;
import lombok.extern.java.Log;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * @author smart
 */
@Component
@Log
public class OkHttpTokenInterceptor implements Interceptor {
	@Autowired
	@Lazy
	private ServiceAuthUtil serviceAuthUtil;
	@Autowired
	@Lazy
	private ServiceAuthConfig serviceAuthConfig;
	@Autowired
	@Lazy
	private UserAuthConfig userAuthConfig;


	@Override
	public Response intercept(Chain chain) throws IOException {
		Request newRequest = chain.request()
				.newBuilder()
				.header(userAuthConfig.getTokenHeader(), BaseContextHandler.getToken())
				.build();
		Response response = chain.proceed(newRequest);
		if(HttpStatus.FORBIDDEN.value()==response.code()){
			if(response.body().string().contains(String.valueOf(RestCodeConstants.EX_CLIENT_INVALID_CODE))){
				log.info("Client Token Expire,Retry to request...");
				serviceAuthUtil.refreshClientToken();
				newRequest = chain.request()
						.newBuilder()
						.header(userAuthConfig.getTokenHeader(), BaseContextHandler.getToken())
						.header(serviceAuthConfig.getTokenHeader(),serviceAuthUtil.getClientToken())
						.build();
				response = chain.proceed(newRequest);
			}
		}
	    return response;
	}

}
