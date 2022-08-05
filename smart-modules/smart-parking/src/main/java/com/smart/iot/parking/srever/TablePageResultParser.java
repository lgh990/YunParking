package com.smart.iot.parking.srever;

import com.yuncitys.smart.merge.facade.IMergeResultParser;
import com.yuncitys.smart.parking.common.msg.TableResultPageResponse;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author smart
 * @create 2018/2/4.
 */
@Component
public class TablePageResultParser implements IMergeResultParser {
    @Override
    public List parser(Object o) {
        TableResultPageResponse response = (TableResultPageResponse) o;
        TableResultPageResponse.TableData data = (TableResultPageResponse.TableData) response.getData();
        List result = data.getRows();
        return result;
    }
}
