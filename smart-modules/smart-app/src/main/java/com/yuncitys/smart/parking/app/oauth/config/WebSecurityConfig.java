package com.yuncitys.smart.parking.app.oauth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.parking.config.annotation.web.builders.Httpparking;
import org.springframework.parking.config.annotation.web.builders.Webparking;
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
                .authorizeRequests().antMatchers("/appUser/register","/appUser/getSmsCode","/admin/**")
                .permitAll()
                .and().csrf().disable()
                .formLogin().loginPage("/login").permitAll();
    }

    @Override
    public void configure(Webparking web) throws Exception {
        //解决静态资源被拦截的问题
        web.ignoring().antMatchers("/admin/**");
    }

}
