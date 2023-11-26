package com.siam.package_feign.mod_feign.user;

import com.siam.package_user.entity.Admin;
import com.siam.package_user.model.example.AdminExample;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "user-siam", path = "/rest/adminFeign")
public interface AdminFeignClient {

    @RequestMapping(value = "/countByExample")
    int countByExample(@RequestBody AdminExample example);

    @RequestMapping(value = "/insertSelective")
    void insertSelective(@RequestBody Admin record);

    @RequestMapping(value = "/selectByExample")
    List<Admin> selectByExample(@RequestBody AdminExample example);

    @RequestMapping(value = "/selectByPrimaryKey")
    Admin selectByPrimaryKey(@RequestParam("id") Integer id);

    @RequestMapping(value = "/updateByPrimaryKeySelective")
    void updateByPrimaryKeySelective(@RequestBody Admin record);
}