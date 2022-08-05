package com.yuncitys.smart.gate.ratelimit.config.repository.springdata;

import com.yuncitys.smart.gate.ratelimit.config.Rate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRateLimiterRepository extends CrudRepository<Rate, String> {

}
