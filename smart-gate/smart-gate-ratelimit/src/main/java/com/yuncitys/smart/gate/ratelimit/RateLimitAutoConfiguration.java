package com.yuncitys.smart.gate.ratelimit;

import com.yuncitys.smart.gate.ratelimit.config.IUserPrincipal;
import com.yuncitys.smart.gate.ratelimit.config.RateLimiter;
import com.yuncitys.smart.gate.ratelimit.config.properties.RateLimitProperties;
import com.yuncitys.smart.gate.ratelimit.config.repository.InMemoryRateLimiter;
import com.yuncitys.smart.gate.ratelimit.config.repository.RedisRateLimiter;
import com.yuncitys.smart.gate.ratelimit.config.repository.springdata.IRateLimiterRepository;
import com.yuncitys.smart.gate.ratelimit.config.repository.springdata.SpringDataRateLimiter;
import com.yuncitys.smart.gate.ratelimit.filters.RateLimitFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import static com.yuncitys.smart.gate.ratelimit.config.properties.RateLimitProperties.PREFIX;

/**
 * @author Marcos Barbero
 */
@Configuration
@EnableConfigurationProperties(RateLimitProperties.class)
@ConditionalOnProperty(prefix = PREFIX, name = "enabled", havingValue = "true")
public class RateLimitAutoConfiguration {

    @Bean
    public RateLimitFilter rateLimiterFilter(final RateLimiter rateLimiter,
                                             final RateLimitProperties rateLimitProperties,
                                             final RouteLocator routeLocator, final IUserPrincipal userPrincipal) {
        return new RateLimitFilter(rateLimiter, rateLimitProperties, routeLocator,userPrincipal);
    }

    @ConditionalOnClass(RedisTemplate.class)
    @ConditionalOnMissingBean(RateLimiter.class)
    @ConditionalOnProperty(prefix = PREFIX, name = "repository", havingValue = "REDIS")
    public static class RedisConfiguration {

        @Bean("rateLimiterRedisTemplate")
        public StringRedisTemplate redisTemplate(final RedisConnectionFactory connectionFactory) {
            return new StringRedisTemplate(connectionFactory);
        }

        @Bean
        public RateLimiter redisRateLimiter(@Qualifier("rateLimiterRedisTemplate") final RedisTemplate redisTemplate) {
            return new RedisRateLimiter(redisTemplate);
        }
    }

    @EntityScan
    @EnableJpaRepositories
    @ConditionalOnMissingBean(RateLimiter.class)
    @ConditionalOnProperty(prefix = PREFIX, name = "repository", havingValue = "JPA")
    public static class SpringDataConfiguration {

        @Bean
        public RateLimiter springDataRateLimiter(IRateLimiterRepository rateLimiterRepository) {
            return new SpringDataRateLimiter(rateLimiterRepository);
        }

    }

    @ConditionalOnMissingBean(RateLimiter.class)
    @ConditionalOnProperty(prefix = PREFIX, name = "repository", havingValue = "IN_MEMORY", matchIfMissing = true)
    public static class InMemoryConfiguration {

        @Bean
        public RateLimiter inMemoryRateLimiter() {
            return new InMemoryRateLimiter();
        }
    }

}
