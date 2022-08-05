package com.smart.iot.roadside.thead;

import com.smart.iot.roadside.biz.GeomagneticBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
//@Profile("prod") //开发环境的时候.
public class Scheduler {
    @Autowired
    public GeomagneticBiz geomagneticBiz;

    @Scheduled(fixedRate = 2000000)
    public void devRunTasks() {
        geomagneticBiz.monitorDevGeomagnetic();
    }


}
