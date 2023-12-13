package com.siam.package_user.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_user.entity.Admin;
import com.siam.package_user.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给其他服务Feign客户端调用的后台管理员模块相关接口
 */
@RestController
public class AdminFeignProvider implements AdminFeignApi {
    @Autowired
    private AdminService adminService;

//    public BasicResult countByExample(AdminParam param) {
//        AdminExample example = new AdminExample();
//        AdminExample.Criteria criteria = example.createCriteria();
//        if(order.getShopId() != null){
//            criteria.andShopIdEqualTo(order.getShopId());
//        }
//        return BasicResult.success(adminService.countByExample(example));
//    }

    public BasicResult insertSelective(Admin record){
        adminService.insertSelective(record);
        return BasicResult.success();
    }

//    public BasicResult selectByExample(AdminParam param) {
//        AdminExample example = new AdminExample();
//        AdminExample.Criteria criteria = example.createCriteria();
//        if(order.getShopId() != null){
//            criteria.andShopIdEqualTo(order.getShopId());
//        }
//        return BasicResult.success(adminService.selectByExample(example));
//    }

    public BasicResult selectByPrimaryKey(@RequestParam("id") Integer id){
        return BasicResult.success(adminService.selectByPrimaryKey(id));
    }

    public BasicResult updateByPrimaryKeySelective(Admin record){
        adminService.updateByPrimaryKeySelective(record);
        return BasicResult.success();
    }
}