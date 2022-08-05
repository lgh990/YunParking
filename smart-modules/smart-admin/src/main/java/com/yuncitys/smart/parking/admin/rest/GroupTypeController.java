package com.yuncitys.smart.parking.admin.rest;

import com.yuncitys.smart.parking.admin.biz.GroupTypeBiz;
import com.yuncitys.smart.parking.admin.entity.GroupType;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.common.rest.BaseController;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ${DESCRIPTION}
 *
 * @author smart
 * @version 2022-06-08 11:51
 */
@Controller
@RequestMapping("groupType")
@CheckUserToken
@CheckClientToken
@Api(tags = "角色组")
public class GroupTypeController extends BaseController<GroupTypeBiz, GroupType, String> {
//
//    @RequestMapping(value = "/page",method = RequestMethod.GET)
//    @ResponseBody
//    public TableResultResponse<Object> page(@RequestParam(defaultValue = "10") int limit, @RequestParam(defaultValue = "1")int page, String name){
//        Example example = new Example(GroupType.class);
//        if(StringUtils.isNotBlank(name))
//            example.createCriteria().andLike("name", "%" + name + "%");
//        Page<Object> result = PageHelper.startPage(page, limit);
//        baseBiz.selectByExample(example);
//        return new TableResultResponse<Object>(result.getTotal(),result.getResult());
//    }

}
