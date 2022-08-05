package com.yuncitys.smart.merge.facade;

import java.util.List;

/**
 * @author smart
 * @create 2018/2/3.
 */
public class DefaultMergeResultParser implements IMergeResultParser {
    @Override
    public List parser(Object methodResult) {
        return (List<?>) methodResult;
    }
}
