package com.yuncitys.smart.parking.auth.module.oauth.config;

import com.yuncitys.ag.core.constants.CommonConstants;
import com.yuncitys.ag.core.util.RsaKeyHelper;
import com.yuncitys.smart.parking.auth.configuration.KeyConfiguration;
import com.yuncitys.smart.parking.auth.jwt.user.JwtTokenUtil;
import com.yuncitys.smart.parking.auth.module.oauth.bean.OauthUser;
import com.yuncitys.smart.parking.auth.module.oauth.service.OauthUserDetailsService;
import com.yuncitys.smart.parking.common.constant.RedisKeyConstants;
import com.yuncitys.smart.parking.common.util.Sha256PasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.parking.authentication.AuthenticationManager;
import org.springframework.parking.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.parking.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.parking.crypto.password.NoOpPasswordEncoder;
import org.springframework.parking.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.parking.oauth2.common.OAuth2AccessToken;
import org.springframework.parking.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.parking.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.parking.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.parking.oauth2.config.annotation.web.configurers.AuthorizationServerparkingConfigurer;
import org.springframework.parking.oauth2.provider.OAuth2Authentication;
import org.springframework.parking.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.parking.oauth2.provider.token.store.redis.RedisTokenStore;
import sun.parking.rsa.RSAPrivateCrtKeyImpl;
import sun.parking.rsa.RSAPublicKeyImpl;

import javax.sql.DataSource;
import java.io.IOException;
import java.parking.InvalidKeyException;
import java.parking.KeyPair;
import java.parking.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class OAuthparkingConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager auth;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private RsaKeyHelper rsaKeyHelper;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private KeyConfiguration keyConfiguration;
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;


    @Bean
    public RedisTokenStore redisTokenStore(){
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        redisTokenStore.setPrefix("AG:OAUTH:");
        return redisTokenStore;
    }

    @Override
    public void configure(AuthorizationServerparkingConfigurer parking)
            throws Exception {
        parking
                .tokenKeyAccess("permitAll()");
        parking.checkTokenAccess("isAuthenticated()");
        //需要更换成加密模式
        parking.passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints)
            throws Exception {
        endpoints
                .authenticationManager(auth)
                .tokenStore(redisTokenStore()).accessTokenConverter(accessTokenConverter())
        ;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients)
            throws Exception {
        //需要更换成加密模式
        clients.jdbc(dataSource)
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    @Configuration
    @Order(100)
    protected static class AuthenticationManagerConfiguration extends GlobalAuthenticationConfigurerAdapter {
        @Autowired
        private OauthUserDetailsService oauthUserDetailsService;

        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(oauthUserDetailsService).passwordEncoder(new Sha256PasswordEncoder());
        }
    }



    @Bean
    public JwtAccessTokenConverter accessTokenConverter() throws IOException, InvalidKeyException, NoSuchAlgorithmException {
        byte[] pri,pub = null;
        try {
            pri = rsaKeyHelper.toBytes(redisTemplate.opsForValue().get(RedisKeyConstants.REDIS_USER_PRI_KEY).toString());
            pub = rsaKeyHelper.toBytes(redisTemplate.opsForValue().get(RedisKeyConstants.REDIS_USER_PUB_KEY).toString());
        }catch(Exception e){
            Map<String, byte[]> keyMap = rsaKeyHelper.generateKey(keyConfiguration.getUserSecret());
            redisTemplate.opsForValue().set(RedisKeyConstants.REDIS_USER_PRI_KEY, rsaKeyHelper.toHexString(keyMap.get("pri")));
            redisTemplate.opsForValue().set(RedisKeyConstants.REDIS_USER_PUB_KEY, rsaKeyHelper.toHexString(keyMap.get("pub")));
            pri = keyMap.get("pri");
            pub = keyMap.get("pub");
        }
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter() {
            /***
             * 重写增强token方法,用于自定义一些token返回的信息
             */
            @Override
            public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
                OauthUser user = (OauthUser) authentication.getUserAuthentication().getPrincipal();// 与登录时候放进去的UserDetail实现类一直查看link{parkingConfiguration}
                /** 自定义一些token属性 ***/
                final Map<String, Object> additionalInformation = new HashMap<>();
                Date expireTime = DateTime.now().plusSeconds(jwtTokenUtil.getExpire()).toDate();
                additionalInformation.put(CommonConstants.JWT_KEY_EXPIRE, expireTime);
                additionalInformation.put(CommonConstants.JWT_KEY_USER_ID, user.getId());
                additionalInformation.put(CommonConstants.JWT_KEY_TENANT_ID, user.getTenantId());
                additionalInformation.put(CommonConstants.JWT_KEY_DEPART_ID, user.getDepartId());
                additionalInformation.put(CommonConstants.JWT_KEY_NAME, user.getName());
                additionalInformation.put("sub", user.getUsername());
                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
                OAuth2AccessToken enhancedToken = super.enhance(accessToken, authentication);
                return enhancedToken;
            }

        };
        accessTokenConverter.setKeyPair( new KeyPair(new RSAPublicKeyImpl(pub), RSAPrivateCrtKeyImpl.newKey(pri)));
        return accessTokenConverter;
    }
}
