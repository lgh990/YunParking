package com.codingapi.ribbon.loadbalancer;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * created by foxdd 2017-12-05
 */
public class LcnZoneAwareLoadBalancerProxy extends ZoneAwareLoadBalancer<Server> {

	private Logger logger = LoggerFactory.getLogger(LcnZoneAwareLoadBalancerProxy.class);

	LcnLoadBalancerRule lcnLoadBalancerRule = new LcnLoadBalancerRule();

	public LcnZoneAwareLoadBalancerProxy(IClientConfig clientConfig, IRule rule,
            IPing ping, ServerList<Server> serverList, ServerListFilter<Server> filter,
            ServerListUpdater serverListUpdater) {
		super(clientConfig, rule, ping, serverList, filter, serverListUpdater);
	}

	@Override
	public Server chooseServer(Object key){
		logger.info("enter chooseServer method, key:" + key);
        List<Server> serverList = super.getReachableServers();
        if(null == serverList || serverList.isEmpty()){
		    return super.chooseServer(key);
        }
		return lcnLoadBalancerRule.proxy(serverList, super.chooseServer(key));

	}

}
