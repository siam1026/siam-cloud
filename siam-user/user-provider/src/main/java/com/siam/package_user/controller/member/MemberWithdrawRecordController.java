package com.siam.package_user.controller.member;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_user.entity.MemberWithdrawRecord;
import com.siam.package_user.model.param.MemberWithdrawRecordParam;
import com.siam.package_user.service.MemberWithdrawRecordService;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/rest/member/memberWithdrawRecord")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "用户提现记录模块相关接口", description = "MemberWithdrawRecordController")
public class MemberWithdrawRecordController {

    @Autowired
    private MemberWithdrawRecordService memberWithdrawRecordService;

    @Operation(summary = "用户提现记录列表")
    @PostMapping(value = "/list")
    public BasicResult list(@RequestBody @Validated(value = {}) MemberWithdrawRecordParam param) {
        Page<MemberWithdrawRecord> page = memberWithdrawRecordService.getListByPage(param);
        return BasicResult.success(page);
    }

    @Operation(summary = "邀请新用户注册奖励金额-用户提现接口")
    @PostMapping(value = "/inviteRewardAmount/insert")
    public BasicResult insert(@RequestBody @Validated(value = {}) MemberWithdrawRecordParam param) {
        memberWithdrawRecordService.insert(param);
        return BasicResult.success();
    }
}