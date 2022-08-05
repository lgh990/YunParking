package com.yuncitys.smart.gate.ratelimit.config.repository.springdata;

import com.yuncitys.smart.gate.ratelimit.config.Rate;
import com.yuncitys.smart.gate.ratelimit.config.repository.AbstractRateLimiter;
import lombok.RequiredArgsConstructor;

/**
 * In memory rate limiter configuration for dev environment.
 *
 * @author Marcos Barbero
 * @since 2017-06-23
 */
@RequiredArgsConstructor
public class SpringDataRateLimiter extends AbstractRateLimiter {

    private final IRateLimiterRepository repository;

    @Override
    protected Rate getRate(String key) {
        return this.repository.findOne(key);
    }

    @Override
    protected void saveRate(Rate rate) {
        this.repository.save(rate);
    }

}
