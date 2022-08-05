package com.smart.iot.parking.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


/**
 * MQTT信息表
 *
 * @author Mr.AG
 * @email
 *@version 2022-08-23 11:36:21
 */
@Getter
@Setter
public class PublishMsg implements Serializable {

    private String msgType;

    private String content;

}
