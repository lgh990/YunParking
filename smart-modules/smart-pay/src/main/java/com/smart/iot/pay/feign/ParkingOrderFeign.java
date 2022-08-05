/*
 *  Copyright (C) 2018  smart<>




 *

 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.

 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */package com.smart.iot.pay.feign;

import com.yuncitys.smart.parking.auth.client.config.FeignApplyConfiguration;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * ${DESCRIPTION}
 *
 * @author smart
 * @version 2022-07-01 15:16
 */
@FeignClient(value = "smart-parking-server", configuration = FeignApplyConfiguration.class)
public interface ParkingOrderFeign {

    @RequestMapping(value = "/order/payNotify")
    HashMap<String, Object> notify(Map params);

    @RequestMapping(value = "order/queryStatus")
    HashMap<String, Object> queryStatus(Map params);

    @RequestMapping(value = "")
    HashMap<String, Object> pushNewContract(Map params);

}
