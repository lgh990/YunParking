package com.yuncitys.smart.parking.admin.vo;

import com.yuncitys.smart.parking.common.vo.TreeNodeVO;

/**
 * ${DESCRIPTION}
 *
 * @author smart
 * @version 2022-06-17 15:21
 */
public class GroupTree extends TreeNodeVO<GroupTree> {
    String label;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
