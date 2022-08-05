package com.yuncitys.smart.parking.common.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * 基础entity
 * @author smart
 *@version 2022/1/13.
 */
@Data
public class BaseEntity implements Serializable{
    @Id
    private Object id;
    private Boolean deleteFlag;
}
