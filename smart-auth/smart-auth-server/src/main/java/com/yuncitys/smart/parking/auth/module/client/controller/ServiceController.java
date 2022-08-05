package com.yuncitys.smart.parking.auth.module.client.controller;

import com.yuncitys.smart.parking.auth.module.client.biz.ClientBiz;
import com.yuncitys.smart.parking.auth.module.client.entity.Client;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author smart
 * @version 2022/12/26.
 */
@RestController
@RequestMapping("service")
public class ServiceController extends BaseController<ClientBiz,Client,String>{

    @RequestMapping(value = "/{id}/client", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse modifyUsers(@PathVariable String id, String clients){
        baseBiz.modifyClientServices(id, clients);
        return new ObjectRestResponse();
    }

    @RequestMapping(value = "/{id}/client", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<List<Client>> getUsers(@PathVariable String id){
        ObjectRestResponse<List<Client> > entityObjectRestResponse = new ObjectRestResponse<>();
        Object o = baseBiz.getClientServices(id);
        entityObjectRestResponse.data((List<Client>)o);
        return entityObjectRestResponse;
    }
}
