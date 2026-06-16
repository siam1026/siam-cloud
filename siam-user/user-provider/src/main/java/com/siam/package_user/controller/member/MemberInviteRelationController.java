package com.siam.package_user.controller.member;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_user.entity.Member;
import com.siam.package_user.model.param.MemberInviteRelationParam;
import com.siam.package_user.service.MemberInviteRelationService;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/rest/memberInviteRelation")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "用户邀请关系模块相关接口", description = "MemberInviteRelationController")
public class MemberInviteRelationController {

    @Autowired
    private MemberInviteRelationService memberInviteRelationService;

    @Operation(summary = "通过邀请者id获取邀请的用户")
    @PostMapping(value = "/getMemberListByInviterId")
    public BasicResult getUserListByInviterId(@RequestBody @Validated(value = {}) MemberInviteRelationParam param) {
        Page<Member> result = memberInviteRelationService.getMemberListByInviterId(param);
        return BasicResult.success(result);
    }

    /*@Operation(summary = "新用户被邀请接口")@PostMapping(value = "/newMemberInvite")
    public BasicResult getLoginMemberInfo(@RequestParam String mobile,@RequestParam Integer inviterId) {
        BasicResult basicResult = new BasicResult();

        memberInviteRelationService.insertSelective(mobile, inviterId);

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("注册成功");
        return basicResult;
    }*/
}