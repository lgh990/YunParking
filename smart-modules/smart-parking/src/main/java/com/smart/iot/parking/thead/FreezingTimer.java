package com.smart.iot.parking.thead;

import com.smart.iot.parking.biz.FreezeAuditRecordsBiz;
import com.smart.iot.start.biz.EquipmentDataProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
//@Profile("prod") //开发环境的时候.
public class FreezingTimer {
    @Autowired
    public FreezeAuditRecordsBiz freezeAuditRecordsBiz;
    private static final org.jboss.logging.Logger logger = org.jboss.logging.Logger.getLogger(EquipmentDataProcess.class);
    @Scheduled(cron = "0 0 0 * * ?")
    public void devRunTasks() {
        logger.info("=============进入解除冻结定时器");
        freezeAuditRecordsBiz.freezingTimer();
    }


}
