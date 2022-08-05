package com.yuncitys.smart.parking.common.srever;

import com.yuncitys.smart.merge.facade.IMergeResultParser;
import com.yuncitys.smart.parking.common.msg.TableResultResponse;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author smart
 * @create 2018/2/4.
 */
@Component
public class TableResultParser implements IMergeResultParser {
    @Override
    public List parser(Object o) {
        TableResultResponse response = (TableResultResponse) o;
        TableResultResponse.TableData data = (TableResultResponse.TableData) response.getData();
        List result = data.getRows();
        return result;
    }
}
