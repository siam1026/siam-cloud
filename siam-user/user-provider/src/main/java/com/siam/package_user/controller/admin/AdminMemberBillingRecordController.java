//package com.siam.package_user.controller.admin;
//
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.siam.package_common.entity.BasicData;
//import com.siam.package_common.entity.BasicResult;
//import com.siam.package_common.constant.BasicResultCode;
//import com.siam.package_user.entity.MemberBillingRecord;
//import com.siam.package_user.service.MemberBillingRecordService;
//import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
//import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
//import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
//import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping(value = "/rest/admin/memberBillingRecord")
//@Transactional(rollbackFor = Exception.class)
//@Tag(name = "后台用户账单记录模块相关接口", description = "AdminMemberBillingRecordController")
//public class AdminMemberBillingRecordController {
//
//    @Autowired
//    private MemberBillingRecordService memberBillingRecordService;
//
//    @Operation(summary = "用户账单记录列表")
////    @PostMapping(value = "/list")
//    public BasicResult list(int pageNo, int pageSize, MemberBillingRecord memberBillingRecord){
//        BasicData basicResult = new BasicData();
//        Page<MemberBillingRecord> page = memberBillingRecordService.getListByPage(param.getPageNo(), param.getPageSize(), memberBillingRecord);
//
//        basicResult.setData(page);
//        basicResult.setSuccess(true);
//        basicResult.setCode(BasicResultCode.SUCCESS);
//        basicResult.setMessage("查询成功");
//        return basicResult;
//    }
//
//    @Operation(summary = "用户账单记录创建")
////    @PostMapping(value = "/insert")
//    public BasicResult insert(MemberBillingRecord memberBillingRecord){
//        BasicResult basicResult = new BasicResult();
//
//        memberBillingRecord.setCreateTime(new Date());
//        memberBillingRecordService.insertSelective(memberBillingRecord);
//
//        basicResult.setSuccess(true);
//        basicResult.setCode(BasicResultCode.SUCCESS);
//        basicResult.setMessage("创建成功");
//        return basicResult;
//    }
//}