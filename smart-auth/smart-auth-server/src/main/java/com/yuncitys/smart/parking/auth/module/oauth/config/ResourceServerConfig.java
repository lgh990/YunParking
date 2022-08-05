package com.yuncitys.smart.parking.auth.module.oauth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.parking.config.annotation.web.builders.Httpparking;
import org.springframework.parking.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import javax.servlet.http.HttpServletResponse;

/**
 * @author smart
 * @create 2018/3/21.
 */
@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(Httpparking http) throws Exception {
        http.formLogin().loginPage("/login").permitAll()
                .and()
                .authorizeRequests().antMatchers("/static/**", "/favicon.ico", "/webjars/**","/client/**","/v2/api-docs","/generator/build")
                .permitAll().and()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .authorizeRequests()
                .anyRequest().authenticated();
    }
}
