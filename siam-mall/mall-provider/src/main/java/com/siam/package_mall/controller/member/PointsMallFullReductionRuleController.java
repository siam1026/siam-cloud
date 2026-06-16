package com.siam.package_mall.controller.member;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_mall.service.PointsMallFullReductionRuleService;
import com.siam.package_common.entity.BasicData;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_mall.entity.PointsMallFullReductionRule;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rest/pointsMall/fullReductionRule")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "满减规则模块相关接口", description = "PointsMallFullReductionRuleController")
public class PointsMallFullReductionRuleController {

    @Autowired
    private PointsMallFullReductionRuleService fullReductionRuleService;

    @Operation(summary = "满减规则列表")@PostMapping(value = "/list")
    public BasicResult list(@RequestBody @Validated(value = {}) PointsMallFullReductionRule fullReductionRule){
        BasicData basicResult = new BasicData();

        Page<PointsMallFullReductionRule> page = fullReductionRuleService.getListByPage(fullReductionRule.getPageNo(), fullReductionRule.getPageSize(), fullReductionRule);

        return BasicResult.success(page);
    }

    /*@Operation(summary = "查看满减规则详情")@PostMapping(value = "/selectById")
    public BasicResult selectById(@RequestParam(value = "id", required = true) int id){
        BasicData basicResult = new BasicData();

        PointsMallFullReductionRule fullReductionRule=fullReductionRuleService.selectByPrimaryKey(id);

        basicResult.setData(fullReductionRule);
        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("获取成功");
        return basicResult;
    }*/


}
