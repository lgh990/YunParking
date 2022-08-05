package com.yuncitys.smart.parking.admin.vo;

import com.yuncitys.smart.parking.common.vo.TreeNodeVO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author smart
 * @version 2022-06-19 13:03
 */
public class AuthorityMenuTree extends TreeNodeVO<AuthorityMenuTree> implements Serializable{
    String text;
    List<AuthorityMenuTree> nodes = new ArrayList<AuthorityMenuTree>();
    String icon;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public AuthorityMenuTree(String text, List<AuthorityMenuTree> nodes) {
        this.text = text;
        this.nodes = nodes;
    }

    public AuthorityMenuTree() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<AuthorityMenuTree> getNodes() {
        return nodes;
    }

    public void setNodes(List<AuthorityMenuTree> nodes) {
        this.nodes = nodes;
    }

//    @Override
//    public void setChildren(List<TreeNodeVO> children) {
//        super.setChildren(children);
//        nodes = new ArrayList<AuthorityMenuTree>();
//    }
//
//    @Override
//    public void add(TreeNodeVO node) {
//        super.add(node);
//        AuthorityMenuTree n = new AuthorityMenuTree();
//        BeanUtils.copyProperties(node,n);
//        nodes.add(n);
//    }
}
