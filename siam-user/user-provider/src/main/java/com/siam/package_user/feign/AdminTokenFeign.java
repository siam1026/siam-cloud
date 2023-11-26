package com.siam.package_user.feign;

import com.siam.package_user.entity.AdminToken;
import com.siam.package_user.service.AdminTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给其他服务Feign客户端调用的后台管理员Token模块相关接口
 */
@RestController
@RequestMapping(value = "/rest/adminTokenFeign")
public class AdminTokenFeign {
    @Autowired
    private AdminTokenService adminTokenService;

    /**
     * 获取当前登录管理员信息
     * @return
     **/
    @RequestMapping(value = "/getLoginAdminInfo")
    public AdminToken getLoginAdminInfo(@RequestParam("token") String token){
        return adminTokenService.getLoginAdminInfo(token);
    }
}