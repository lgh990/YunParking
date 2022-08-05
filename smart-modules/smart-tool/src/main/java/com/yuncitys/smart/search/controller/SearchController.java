package com.yuncitys.smart.search.controller;

import com.yuncitys.smart.search.service.LuceneService;
import com.yuncitys.smart.parking.api.vo.search.IndexObject;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.msg.TableResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author smart
 * @create 2018/3/5.
 */
@RestController
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private LuceneService luceneService;

    @RequestMapping(value = "", method = {RequestMethod.POST,RequestMethod.GET})
    public TableResultResponse<IndexObject> search(@RequestParam String word, @RequestParam(defaultValue = "1") Integer pageNumber, @RequestParam(defaultValue = "15") Integer pageSize) {
        return luceneService.page(pageNumber, pageSize, word);
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public ObjectRestResponse createIndexObject(@RequestBody IndexObject indexObject) {
        luceneService.save(indexObject);
        return new ObjectRestResponse();
    }

    @RequestMapping(value = "/index", method = RequestMethod.DELETE)
    public ObjectRestResponse removeIndexObject(@RequestBody IndexObject indexObject) {
        luceneService.delete(indexObject);
        return new ObjectRestResponse();
    }

    @RequestMapping(value = "/index", method = RequestMethod.PATCH)
    public ObjectRestResponse batchCreateIndexObject(@RequestBody List<IndexObject> indexObjects) {
        for (IndexObject object : indexObjects) {
            luceneService.save(object);
        }
        return new ObjectRestResponse();
    }

    @RequestMapping(value = "/index", method = RequestMethod.PUT)
    public ObjectRestResponse updateIndexObject(@RequestBody IndexObject indexObject) {
        luceneService.update(indexObject);
        return new ObjectRestResponse();
    }

}
