package com.yuncitys.smart.parking.app.oauth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.parking.config.annotation.web.builders.Httpparking;
import org.springframework.parking.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.parking.oauth2.config.annotation.web.configurers.ResourceServerparkingConfigurer;

import javax.servlet.http.HttpServletResponse;

/**
 * @author smart
 * @create 2018/3/21.
 */
@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(ResourceServerparkingConfigurer resources) throws Exception {
        super.configure(resources);
    }

    @Override
    public void configure(Httpparking http) throws Exception {
        http.formLogin().loginPage("/login").permitAll()
                .and()
                .authorizeRequests().antMatchers("/appUser/register","/appUser/getSmsCode")
                .permitAll().and()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .authorizeRequests()
                .anyRequest().authenticated();
    }
}
