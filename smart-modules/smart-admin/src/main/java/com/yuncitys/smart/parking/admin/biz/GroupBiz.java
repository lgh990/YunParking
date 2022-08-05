package com.yuncitys.smart.parking.admin.biz;

import com.smart.cache.annotation.CacheClear;
import com.yuncitys.ag.core.context.BaseContextHandler;
import com.yuncitys.smart.merge.annonation.MergeResult;
import com.yuncitys.smart.parking.admin.constant.AdminCommonConstant;
import com.yuncitys.smart.parking.admin.entity.Element;
import com.yuncitys.smart.parking.admin.entity.Group;
import com.yuncitys.smart.parking.admin.entity.Menu;
import com.yuncitys.smart.parking.admin.entity.ResourceAuthority;
import com.yuncitys.smart.parking.admin.mapper.*;
import com.yuncitys.smart.parking.admin.vo.AuthorityMenuTree;
import com.yuncitys.smart.parking.admin.vo.GroupUsers;
import com.yuncitys.smart.parking.admin.vo.MenuTree;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import com.yuncitys.smart.parking.common.util.BooleanUtil;
import com.yuncitys.smart.parking.common.util.UUIDUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * ${DESCRIPTION}
 *
 * @author smart
 * @version 2022-06-12 8:48
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class GroupBiz extends BusinessBiz<GroupMapper, Group> {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ResourceAuthorityBiz resourceAuthorityBiz;
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private ElementMapper elementMapper;

    @Override
    public void insertSelective(Group entity) {
        if (AdminCommonConstant.ROOT.equals(entity.getParentId())) {
            entity.setPath("/" + entity.getCode());
        } else {
            Group parent = this.selectById(entity.getParentId());
            entity.setPath(parent.getPath() + "/" + entity.getCode());
        }
        super.insertSelective(entity);
    }

    @Override
    public void updateById(Group entity) {
        if (AdminCommonConstant.ROOT == entity.getParentId()) {
            entity.setPath("/" + entity.getCode());
        } else {
            Group parent = this.selectById(entity.getParentId());
            entity.setPath(parent.getPath() + "/" + entity.getCode());
        }
        super.updateById(entity);
    }

    /**
     * 获取群组关联用户
     *
     * @param groupId
     * @return
     */
    public GroupUsers getGroupUsers(String groupId) {
        return new GroupUsers(userMapper.selectMemberByGroupId(groupId), userMapper.selectLeaderByGroupId(groupId));
    }

    /**
     * 变更群主所分配用户
     *
     * @param groupId
     * @param members
     * @param leaders
     */
    @CacheClear(pre = "permission")
    public void modifyGroupUsers(String groupId, String members, String leaders) {
        mapper.deleteGroupLeadersById(groupId);
        mapper.deleteGroupMembersById(groupId);
        if (!StringUtils.isEmpty(members)) {
            String[] mem = members.split(",");
            for (String m : mem) {
                mapper.insertGroupMembersById(UUIDUtils.generateUuid(), groupId, m, BaseContextHandler.getTenantID());
            }
        }
        if (!StringUtils.isEmpty(leaders)) {
            String[] mem = leaders.split(",");
            for (String m : mem) {
                mapper.insertGroupLeadersById(UUIDUtils.generateUuid(), groupId, m, BaseContextHandler.getTenantID());
            }
        }
    }

    /**
     * 变更群组关联的菜单
     *
     * @param groupId
     * @param menus
     */
    @CacheClear(keys = {"permission:menu", "permission:u"})
    public void modifyAuthorityMenu(String groupId, String[] menus, String type) {
        resourceAuthorityBiz.deleteByAuthorityIdAndResourceType(groupId + "", AdminCommonConstant.RESOURCE_TYPE_MENU, type);
        List<Menu> menuList = menuMapper.selectAll();
        Map<String, String> map = new HashMap<String, String>();
        for (Menu menu : menuList) {
            map.put(menu.getId().toString(), menu.getParentId().toString());
        }
        Set<String> relationMenus = new HashSet<String>();
        relationMenus.addAll(Arrays.asList(menus));
        ResourceAuthority authority = null;
        for (String menuId : menus) {
            findParentID(map, relationMenus, menuId);
        }
        for (String menuId : relationMenus) {
            authority = new ResourceAuthority(AdminCommonConstant.AUTHORITY_TYPE_GROUP, AdminCommonConstant.RESOURCE_TYPE_MENU);
            authority.setAuthorityId(groupId + "");
            authority.setResourceId(menuId);
            authority.setParentId("-1");
            authority.setType(type);
            resourceAuthorityBiz.insertSelective(authority);
        }
    }

    private void findParentID(Map<String, String> map, Set<String> relationMenus, String id) {
        String parentId = map.get(id);
        if (String.valueOf(AdminCommonConstant.ROOT).equals(id) || parentId == null) {
            return;
        }
        relationMenus.add(parentId);
        findParentID(map, relationMenus, parentId);
    }

    /**
     * SimpleRouteLocator
     * 分配资源权限
     *
     * @param groupId
     * @param menuId
     * @param elementId
     */
    @CacheClear(keys = {"permission:ele", "permission:u"})
    public void modifyAuthorityElement(String groupId, String menuId, String elementId, String type) {
        ResourceAuthority authority = new ResourceAuthority(AdminCommonConstant.AUTHORITY_TYPE_GROUP, AdminCommonConstant.RESOURCE_TYPE_BTN);
        authority.setAuthorityId(groupId + "");
        authority.setResourceId(elementId + "");
        authority.setParentId("-1");
        authority.setType(type);
        resourceAuthorityBiz.insertSelective(authority);
    }

    /**
     * 移除资源权限
     *
     * @param groupId
     * @param elementId
     */
    @CacheClear(keys = {"permission:ele", "permission:u"})
    public void removeAuthorityElement(String groupId, String elementId, String type) {
        ResourceAuthority authority = new ResourceAuthority();
        authority.setAuthorityId(groupId + "");
        authority.setResourceId(elementId + "");
        authority.setParentId("-1");
        authority.setType(type);
        resourceAuthorityBiz.delete(authority);
    }


    /**
     * 获取群主关联的菜单
     *
     * @param groupId
     * @return
     */
    public List<AuthorityMenuTree> getAuthorityMenu(String groupId, String type) {
        List<Menu> menus = menuMapper.selectMenuByAuthorityId(String.valueOf(groupId), AdminCommonConstant.AUTHORITY_TYPE_GROUP, type);
        List<AuthorityMenuTree> trees = new ArrayList<AuthorityMenuTree>();
        AuthorityMenuTree node = null;
        for (Menu menu : menus) {
            node = new AuthorityMenuTree();
            node.setText(menu.getTitle());
            BeanUtils.copyProperties(menu, node);
            trees.add(node);
        }
        return trees;
    }

    /**
     * 获取群组关联的资源
     *
     * @param groupId
     * @return
     */
    public List<String> getAuthorityElement(String groupId, String type) {
        ResourceAuthority authority = new ResourceAuthority(AdminCommonConstant.AUTHORITY_TYPE_GROUP, AdminCommonConstant.RESOURCE_TYPE_BTN);
        authority.setAuthorityId(groupId);
        authority.setType(type);
        List<ResourceAuthority> authorities = resourceAuthorityBiz.selectList(authority);
        List<String> ids = new ArrayList<String>();
        for (ResourceAuthority auth : authorities) {
            ids.add(auth.getResourceId());
        }
        return ids;
    }

    /**
     * 获取当前管理员可以分配的菜单
     * @return
     */
    public List<MenuTree> getAuthorizeMenus() {
        if (BooleanUtil.BOOLEAN_TRUE.equals(userMapper.selectByPrimaryKey(BaseContextHandler.getUserID()).getIsSuperAdmin())) {
            return MenuTree.buildTree(menuMapper.selectAll(), AdminCommonConstant.ROOT);
        }
        return MenuTree.buildTree(menuMapper.selectAuthorityMenuByUserId(BaseContextHandler.getUserID(), AdminCommonConstant.RESOURCE_TYPE_AUTHORISE), AdminCommonConstant.ROOT);
    }

    /**
     * 获取当前管理员可以分配的资源
     * @param menuId
     * @return
     */
    @MergeResult
    public List<Element> getAuthorizeElements(String menuId) {
        if (BooleanUtil.BOOLEAN_TRUE.equals(userMapper.selectByPrimaryKey(BaseContextHandler.getUserID()).getIsSuperAdmin())) {
            Example example = new Example(Element.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("menuId", menuId);
            return elementMapper.selectByExample(example);
        }
        return elementMapper.selectAuthorityMenuElementByUserId(BaseContextHandler.getUserID(),menuId,AdminCommonConstant.RESOURCE_TYPE_AUTHORISE);
    }
}
