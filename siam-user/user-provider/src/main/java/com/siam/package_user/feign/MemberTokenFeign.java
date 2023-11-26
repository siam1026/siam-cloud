package com.siam.package_user.feign;

import com.siam.package_user.entity.MemberToken;
import com.siam.package_user.service.MemberTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给其他服务Feign客户端调用的用户Token模块相关接口
 */
@RestController
@RequestMapping(value = "/rest/memberTokenFeign")
public class MemberTokenFeign {
    @Autowired
    private MemberTokenService memberTokenService;

    @RequestMapping(value = "/getLoginMemberInfo")
    public MemberToken getLoginMemberInfo(@RequestParam("token") String token){
        return memberTokenService.getLoginMemberInfo(token);
    }
}