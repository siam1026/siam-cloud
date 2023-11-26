package com.siam.package_feign.mod_feign.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value = "user-siam", path = "/rest/memberInviteRelationFeign")
public interface MemberInviteRelationFeignClient {

    /**
     * 查询用户的邀请人(三层邀请关系)
     *
     * @return
     */
    @RequestMapping(value = "/selectInviter")
    Map<String, Integer> selectInviter(@RequestParam("memberId") Integer memberId);
}