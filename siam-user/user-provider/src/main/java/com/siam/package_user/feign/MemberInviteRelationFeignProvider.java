package com.siam.package_user.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_user.service.MemberInviteRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
public class MemberInviteRelationFeignProvider implements MemberInviteRelationFeignApi {

    @Autowired
    private MemberInviteRelationService memberInviteRelationService;

    /**
     * 查询用户的邀请人(三层邀请关系)
     *
     * @return
     */
    public BasicResult selectInviter(@RequestParam("memberId") Integer memberId){
        return BasicResult.success(memberInviteRelationService.selectInviter(memberId));
    }
}