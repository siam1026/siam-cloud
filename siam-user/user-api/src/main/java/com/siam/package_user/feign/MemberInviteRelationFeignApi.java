package com.siam.package_user.feign;

import com.siam.package_common.entity.BasicResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value = "user-siam")
public interface MemberInviteRelationFeignApi {

    /**
     * 查询用户的邀请人(三层邀请关系)
     *
     * @return
     */
    @PostMapping(value = "/api/memberInviteRelation/selectInviter")
    BasicResult<Map<String, Integer>> selectInviter(@RequestParam("memberId") Integer memberId);
}