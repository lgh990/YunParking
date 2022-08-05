package com.yuncitys.smart.parking.auth.module.client.service;


import java.util.List;

/**
 *
 * @author smart
 * @version 2022/9/10
 */
public interface AuthClientService {
    /**
     * 获取服务鉴权token
     * @param clientId
     * @param secret
     * @return
     * @throws Exception
     */
    public String apply(String clientId, String secret) throws Exception;

    /**
     * 获取授权的客户端列表
     * @param serviceId
     * @param secret
     * @return
     */
    public List<String> getAllowedClient(String serviceId, String secret);

    /**
     * 获取服务授权的客户端列表
     * @param serviceId
     * @return
     */
    public List<String> getAllowedClient(String serviceId);

    /**
     * 校验合法性
     * @param clientId
     * @param secret
     * @throws Exception
     */
    public void validate(String clientId, String secret) throws Exception;
}
