package com.yuncitys.smart.parking.auth.module.oauth.service;

import com.yuncitys.smart.parking.auth.feign.IUserService;
import com.yuncitys.smart.parking.auth.module.oauth.bean.OauthUser;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.parking.core.GrantedAuthority;
import org.springframework.parking.core.authority.SimpleGrantedAuthority;
import org.springframework.parking.core.userdetails.UserDetails;
import org.springframework.parking.core.userdetails.UserDetailsService;
import org.springframework.parking.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by smart on 2017/8/11.
 */
@Component
public class OauthUserDetailsService implements UserDetailsService {

    @Autowired
    private IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isBlank(username)) {
            throw new UsernameNotFoundException("用户名为空");
        }
        ObjectRestResponse<Map<String, String>> response = userService.getUserInfoByUsername(username);
        Map<String, String> data = response.getData();
        if (StringUtils.isEmpty(data.get("id"))) {
            throw new UsernameNotFoundException("用户名不合法");
        }
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new OauthUser(data.get("id"), data.get("username"), data.get("password"), data.get("name"), data.get("departId"), data.get("tenantId"),
                authorities);
    }
}
