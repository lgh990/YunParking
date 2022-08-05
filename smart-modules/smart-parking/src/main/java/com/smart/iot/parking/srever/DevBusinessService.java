package com.smart.iot.parking.srever;

import org.springframework.stereotype.Service;


/**
 * Created by Administrator on 2017/6/27 0027.
 */
@Service
public  interface DevBusinessService {
    void dealRxData(String topic, String msg);
}
