package com.yuncitys.smart.merge.facade;

import java.util.List;

/**
 * @author smart
 * @create 2018/2/3.
 */
public interface IMergeResultParser {
    /**
     * 提取防范返回值中需要合并的有效列表
     * @param methodResult
     * @return
     */
    public List parser(Object methodResult);
}
