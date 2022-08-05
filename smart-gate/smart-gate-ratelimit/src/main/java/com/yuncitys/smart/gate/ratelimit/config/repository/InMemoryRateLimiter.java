package com.yuncitys.smart.gate.ratelimit.config.repository;

import com.yuncitys.smart.gate.ratelimit.config.Rate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In memory rate limiter configuration for dev environment.
 *
 * @author Marcos Barbero
 * @since 2017-06-23
 */
public class InMemoryRateLimiter extends AbstractRateLimiter {

    private Map<String, Rate> repository = new ConcurrentHashMap<>();

    @Override
    protected Rate getRate(String key) {
        return this.repository.get(key);
    }

    @Override
    protected void saveRate(Rate rate) {
        this.repository.put(rate.getKey(), rate);
    }

}
