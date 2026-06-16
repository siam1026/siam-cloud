//package com.siam.package_user.controller.admin;
//
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.siam.package_common.entity.BasicData;
//import com.siam.package_common.entity.BasicResult;
//import com.siam.package_common.constant.BasicResultCode;
//import com.siam.package_user.entity.MemberTradeRecord;
//import com.siam.package_user.service.MemberTradeRecordService;
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
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequestMapping(value = "/rest/admin/memberTrade")
//@Transactional(rollbackFor = Exception.class)
//@Tag(name = "后台用户交易模块相关接口", description = "AdminMemberTradeController")
//public class AdminMemberTradeRecordController {
//    @Autowired
//    private MemberTradeRecordService memberTradeRecordService;
//
//    @Operation(summary = "用户交易列表")
////    @PostMapping(value = "/list")
//    public BasicResult list(int pageNo, int pageSize, MemberTradeRecord memberTradeRecord){
//        BasicData basicResult = new BasicData();
//
//        Page page = memberTradeRecordService.getListByPage(param.getPageNo(), param.getPageSize(), memberTradeRecord);
//
//        basicResult.setData(page);
//        basicResult.setSuccess(true);
//        basicResult.setCode(BasicResultCode.SUCCESS);
//        basicResult.setMessage("查询成功");
//        return basicResult;
//    }
//
//
//    @Operation(summary = "新增用户交易")
////    @PostMapping(value = "/insert")
//    public BasicResult insert(MemberTradeRecord memberTradeRecord){
//        BasicResult basicResult = new BasicResult();
//
//        memberTradeRecordService.insertSelective(memberTradeRecord);
//
//        basicResult.setSuccess(true);
//        basicResult.setCode(BasicResultCode.SUCCESS);
//        basicResult.setMessage("新增成功");
//        return basicResult;
//    }
//
//    @Operation(summary = "修改用户交易")
////    @PostMapping(value = "/update")
//    public BasicResult update(MemberTradeRecord memberTradeRecord){
//        BasicResult basicResult = new BasicResult();
//
//        memberTradeRecordService.updateByPrimaryKeySelective(memberTradeRecord);
//
//        basicResult.setSuccess(true);
//        basicResult.setCode(BasicResultCode.SUCCESS);
//        basicResult.setMessage("修改成功");
//        return basicResult;
//    }
//
//    @Operation(summary = "删除用户交易(含批量操作)")
////    @PostMapping(value = "/delete")
//    public BasicResult delete(@RequestParam(value = "ids", required = true) List<String> ids){
//        BasicResult basicResult = new BasicResult();
//
//        for(String id : ids){
//            memberTradeRecordService.deleteByPrimaryKey(Integer.valueOf(id));
//        }
//
//        basicResult.setSuccess(true);
//        basicResult.setCode(BasicResultCode.SUCCESS);
//        basicResult.setMessage("删除成功");
//        return basicResult;
//    }
//}