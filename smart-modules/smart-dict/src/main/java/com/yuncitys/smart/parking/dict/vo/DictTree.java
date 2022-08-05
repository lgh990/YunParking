package com.yuncitys.smart.parking.dict.vo;

import com.yuncitys.smart.parking.common.vo.TreeNodeVO;

/**
 * @author smart
 * @create 2018/1/30.
 */
public class DictTree extends TreeNodeVO<DictTree> {
    String label;
    String code;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public DictTree(){

    }
    public DictTree(Object id,Object parentId,String label,String code) {
        this.label = label;
        this.code = code;
        this.setId(id);
        this.setParentId(parentId);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
