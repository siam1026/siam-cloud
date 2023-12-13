package com.siam.package_user.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_user.entity.Admin;
import com.siam.package_user.model.example.AdminExample;
import com.siam.package_user.model.param.AdminParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "user-siam")
public interface AdminFeignApi {

//    @PostMapping(value = "/api/admin/countByExample")
//    BasicResult countByExample(@RequestBody AdminParam param);

    @PostMapping(value = "/api/admin/insertSelective")
    BasicResult insertSelective(@RequestBody Admin record);

//    @PostMapping(value = "/api/admin/selectByExample")
//    BasicResult selectByExample(@RequestBody AdminParam param);

    @PostMapping(value = "/api/admin/selectByPrimaryKey")
    BasicResult selectByPrimaryKey(@RequestParam("id") Integer id);

    @PostMapping(value = "/api/admin/updateByPrimaryKeySelective")
    BasicResult updateByPrimaryKeySelective(@RequestBody Admin record);
}