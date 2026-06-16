package com.siam.package_mall.controller.admin;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.annoation.AdminPermission;
import com.siam.package_mall.service.PointsMallFullReductionRuleService;
import com.siam.package_common.entity.BasicData;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_common.constant.BasicResultCode;
import com.siam.package_mall.entity.PointsMallFullReductionRule;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/rest/admin/pointsMall/fullReductionRule")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "后台满减规则模块相关接口", description = "AdminPointsMallFullReductionRuleController")
public class AdminPointsMallFullReductionRuleController {

    @Autowired
    private PointsMallFullReductionRuleService pointsMallFullReductionRuleService;


    @Operation(summary = "新增满减规则")
    @PostMapping(value = "/insert")
    public BasicResult insert(@RequestBody @Validated(value = {}) PointsMallFullReductionRule fullReductionRule){
        BasicResult basicResult = new BasicResult();

        pointsMallFullReductionRuleService.insertSelective(fullReductionRule);

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("新增成功");
        return basicResult;
    }

    @AdminPermission
    @Operation(summary = "修改满减规则")
    @PutMapping(value = "/update")
    public BasicResult update(@RequestBody @Validated(value = {}) PointsMallFullReductionRule fullReductionRule){
        BasicResult basicResult = new BasicResult();

        pointsMallFullReductionRuleService.updateByPrimaryKeySelective(fullReductionRule);

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("修改成功");
        return basicResult;
    }

    @AdminPermission
    @Operation(summary = "删除满减规则")
    @DeleteMapping(value = "/delete")
    public BasicResult delete(@RequestBody @Validated(value = {}) PointsMallFullReductionRule param){
        BasicResult basicResult = new BasicResult();

        pointsMallFullReductionRuleService.deleteByPrimaryKey(param.getId());

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("删除成功");
        return basicResult;
    }

    @Operation(summary = "查看满减规则详情")
    @PostMapping(value = "/selectById")
    public BasicResult selectById(@RequestBody @Validated(value = {}) PointsMallFullReductionRule param){
        BasicData basicResult = new BasicData();

        PointsMallFullReductionRule fullReductionRule= pointsMallFullReductionRuleService.selectByPrimaryKey(param.getId());

        basicResult.setData(fullReductionRule);
        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("获取成功");
        return basicResult;
    }


    @Operation(summary = "满减规则列表")
    @PostMapping(value = "/list")
    public BasicResult list(@RequestBody @Validated(value = {}) PointsMallFullReductionRule fullReductionRule) {
        BasicData basicResult = new BasicData();

        Page<PointsMallFullReductionRule> page = pointsMallFullReductionRuleService.getListByPage(fullReductionRule.getPageNo(), fullReductionRule.getPageSize(), fullReductionRule);

        return BasicResult.success(page);
    }

}
