package com.smart.iot.parking.feign;

        import com.yuncitys.smart.parking.auth.client.config.FeignApplyConfiguration;
        import com.yuncitys.smart.parking.common.msg.TableResultResponse;
        import org.springframework.cloud.netflix.feign.FeignClient;
        import org.springframework.web.bind.annotation.PathVariable;
        import org.springframework.web.bind.annotation.RequestMapping;
        import org.springframework.web.bind.annotation.RequestMethod;

        import java.util.HashMap;

/**
 * ${DESCRIPTION}
 *
 * @author smart
 * @version 2022-07-01 15:16
 */
@FeignClient(value = "smart-admin",configuration = FeignApplyConfiguration.class)
public interface DepartFeign {
  @RequestMapping(value="/depart/tree",method = RequestMethod.GET)
  public Object getTree();
  @RequestMapping(value="depart/getByPK/{id}",method = RequestMethod.GET)
  public HashMap<String,Object> getByPK(@PathVariable("id") String id);
}
