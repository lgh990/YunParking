package com.yuncitys.smart.parking.auth.module.client.biz;

import com.yuncitys.smart.parking.auth.module.client.entity.Client;
import com.yuncitys.smart.parking.auth.module.client.entity.ClientService;
import com.yuncitys.smart.parking.auth.module.client.mapper.ClientMapper;
import com.yuncitys.smart.parking.auth.module.client.mapper.ClientServiceMapper;
import com.yuncitys.smart.parking.common.biz.BaseBiz;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 *
 *
 * @author Mr.AG
 * @email
 * @version 2022-1-26 19:43:46
 */
@Service
public class ClientBiz extends BusinessBiz<ClientMapper,Client> {
    @Autowired
    private ClientServiceMapper clientServiceMapper;
    @Autowired
    private ClientServiceBiz clientServiceBiz;

    public List<Client> getClientServices(String id) {
        return mapper.selectAuthorityServiceInfo(id);
    }

    public void modifyClientServices(String id, String clients) {
        clientServiceMapper.deleteByServiceId(id);
        if (!StringUtils.isEmpty(clients)) {
            String[] mem = clients.split(",");
            for (String m : mem) {
                ClientService clientService = new ClientService();
                clientService.setServiceId(m);
                clientService.setClientId(id+"");
                clientServiceBiz.insertSelective(clientService);
            }
        }
    }
}
