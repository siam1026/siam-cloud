package com.siam.package_user.controller.member;

import com.siam.package_user.service.MemberTradeRecordService;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rest/memberTrade")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "用户交易模块相关接口", description = "MemberTradeController")
public class MemberTradeRecordController {
    @Autowired
    private MemberTradeRecordService memberTradeRecordService;

    /*@Operation(summary = "用户交易列表")@PostMapping(value = "/list")
    public BasicResult list(int pageNo, int pageSize, MemberTradeRecord memberTradeRecord){
        BasicData basicResult = new BasicData();

        Page page = memberTradeRecordService.getListByPage(param.getPageNo(), param.getPageSize(), memberTradeRecord);

        return BasicResult.success(page);
    }*/
}