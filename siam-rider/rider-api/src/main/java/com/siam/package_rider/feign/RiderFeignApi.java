package com.siam.package_rider.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_rider.entity.Rider;
import com.siam.package_rider.model.example.RiderExample;
import com.siam.package_rider.model.param.RiderParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "rider-siam")
public interface RiderFeignApi {

    @PostMapping(value = "/api/rider/countByExample")
    BasicResult countByExample(@RequestBody RiderParam param);

    @PostMapping(value = "/api/rider/insertSelective")
    BasicResult insertSelective(@RequestBody Rider record);

    @PostMapping(value = "/api/rider/selectByExample")
    BasicResult<List<Rider>> selectByExample(@RequestBody RiderParam param);

    @PostMapping(value = "/api/rider/selectByPrimaryKey")
    BasicResult<Rider> selectByPrimaryKey(@RequestParam("id") Integer id);

    @PostMapping(value = "/api/rider/updateByPrimaryKeySelective")
    BasicResult updateByPrimaryKeySelective(@RequestBody Rider record);

    @PostMapping(value = "/api/rider/selectByMobile")
    BasicResult selectByMobile(@RequestParam("mobile") String mobile);
}