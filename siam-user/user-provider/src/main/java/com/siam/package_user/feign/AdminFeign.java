package com.siam.package_user.feign;

import com.siam.package_user.entity.Admin;
import com.siam.package_user.model.example.AdminExample;
import com.siam.package_user.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 提供给其他服务Feign客户端调用的后台管理员模块相关接口
 */
@RestController
@RequestMapping(value = "/rest/adminFeign")
public class AdminFeign {
    @Autowired
    private AdminService adminService;

    @RequestMapping(value = "/countByExample")
    public int countByExample(AdminExample example){
        return adminService.countByExample(example);
    }

    @RequestMapping(value = "/insertSelective")
    public void insertSelective(Admin record){
        adminService.insertSelective(record);
    }

    @RequestMapping(value = "/selectByExample")
    public List<Admin> selectByExample(AdminExample example){
        return adminService.selectByExample(example);
    }

    @RequestMapping(value = "/selectByPrimaryKey")
    public Admin selectByPrimaryKey(@RequestParam("id") Integer id){
        return adminService.selectByPrimaryKey(id);
    }

    @RequestMapping(value = "/updateByPrimaryKeySelective")
    public void updateByPrimaryKeySelective(Admin record){
        adminService.updateByPrimaryKeySelective(record);
    }
}