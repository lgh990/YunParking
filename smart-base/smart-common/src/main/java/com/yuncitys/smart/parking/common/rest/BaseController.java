package com.yuncitys.smart.parking.common.rest;

import com.yuncitys.ag.core.context.BaseContextHandler;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.msg.TableResultResponse;
import com.yuncitys.smart.parking.common.util.Query;
import com.yuncitys.smart.parking.common.util.StringEscapeEditor;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * ${DESCRIPTION}
 *
 * @author smart
 * @version 2022-06-15 8:48
 */
public class BaseController<Biz extends BusinessBiz,Entity,PK> {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringEscapeEditor());
        binder.registerCustomEditor(String[].class, new StringEscapeEditor());
    }

    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected Biz baseBiz;

    @RequestMapping(value = "",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("新增单个对象")
    public ObjectRestResponse<Entity> add(@RequestBody Entity entity){
        baseBiz.insertSelective(entity);
        return new ObjectRestResponse<Entity>().data(entity);
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("查询单个对象")
    public ObjectRestResponse<Entity> get(@PathVariable PK id){
        ObjectRestResponse<Entity> entityObjectRestResponse = new ObjectRestResponse<>();
        Object o = baseBiz.selectById(id);
        entityObjectRestResponse.data((Entity)o);
        return entityObjectRestResponse;
    }
    @RequestMapping(value = "/one",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("查询单个对象")
    public ObjectRestResponse<Entity> getOne(@RequestBody Entity entity){
        ObjectRestResponse<Entity> entityObjectRestResponse = new ObjectRestResponse<>();
        Object o = baseBiz.selectOne(entity);
        entityObjectRestResponse.data((Entity)o);
        return entityObjectRestResponse;
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    @ResponseBody
    @ApiOperation("更新单个对象")
    public ObjectRestResponse<Entity> update(@RequestBody Entity entity){
        baseBiz.updateSelectiveById(entity);
        return new ObjectRestResponse<Entity>().data(entity);
    }
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    @ApiOperation("移除单个对象")
    public ObjectRestResponse<Entity> remove(@PathVariable PK id){
        baseBiz.deleteById(id);
        return new ObjectRestResponse<Entity>();
    }

    @RequestMapping(value = "/all",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("获取所有数据")
    public List<Entity> all(){
        return baseBiz.selectListAll();
    }
    @ApiOperation("分页获取数据")
    @RequestMapping(value = "/page",method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<Entity> list(@RequestParam Map<String, Object> params) throws InvocationTargetException, ExecutionException, IllegalAccessException, NoSuchMethodException {

        String beginTime="";
        if(params.get("beginTime")!=null) {
            beginTime = String.valueOf(params.get("beginTime"));
        }
        String endTime="";
        if(params.get("beginTime")!=null) {
            endTime=String.valueOf(params.get("endTime"));
        }
        params.remove("beginTime");
        params.remove("endTime");
        Query query = new Query(params);
        query.remove("crtUserId");
        query.remove("isDeleted");
        return baseBiz.selectByQuery(query,beginTime,endTime);
    }
    public String getCurrentUserName(){
        return BaseContextHandler.getUsername();
    }
    @RequestMapping(value = "/queryByIds/{code}",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("根据idList获取map")
    public Map queryByIds(@PathVariable String code){
        return baseBiz.getEntityByIds(code);
    }


}
