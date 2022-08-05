package com.yuncitys.smart.merge.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author smart
 * @create 2018/2/3.
 */
@Data
@ConfigurationProperties("merge")
public class MergeProperties {
    /**
     * guava缓存的键值数
     */
    private Integer guavaCacheNumMaxSize;
    /**
     * guava更新混存的下一次时间,分钟
     */
    private Integer guavaCacheRefreshWriteTime;
    /**
     * guava
     */
    private Integer guavaCacheRefreshThreadPoolSize;

    public Integer getGuavaCacheNumMaxSize() {
        return guavaCacheNumMaxSize;
    }

    public void setGuavaCacheNumMaxSize(Integer guavaCacheNumMaxSize) {
        this.guavaCacheNumMaxSize = guavaCacheNumMaxSize;
    }

    public Integer getGuavaCacheRefreshWriteTime() {
        return guavaCacheRefreshWriteTime;
    }

    public void setGuavaCacheRefreshWriteTime(Integer guavaCacheRefreshWriteTime) {
        this.guavaCacheRefreshWriteTime = guavaCacheRefreshWriteTime;
    }

}
