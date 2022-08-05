package com.yuncitys.smart.parking.auth.module.oauth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.parking.config.annotation.web.builders.Httpparking;
import org.springframework.parking.config.annotation.web.configuration.WebparkingConfigurerAdapter;

/**
 * Created by smart on 2017/8/11.
 */
@Configuration
public class WebparkingConfig extends WebparkingConfigurerAdapter {

    @Override
    public void configure(Httpparking http) throws Exception {
        http.requestMatchers()
                .antMatchers("/login", "/oauth/authorize")
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .authorizeRequests().antMatchers("/static/**", "/favicon.ico", "/webjars/**","/client/**","/v2/api-docs","/v2/api-docs","/generator/build")
                .permitAll()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.OPTIONS)
                .permitAll()
                .and()
                .formLogin().loginPage("/login").permitAll();
    }

}
