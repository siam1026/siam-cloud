package com.siam.package_promotion.controller.member;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.entity.BasicData;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_common.exception.StoneCustomerException;
import com.siam.package_promotion.entity.FullReductionRule;
import com.siam.package_promotion.service.FullReductionRuleService;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/rest/fullReductionRule")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "满减规则模块相关接口", description = "FullReductionRuleController")
public class FullReductionRuleController {

    @Autowired
    private FullReductionRuleService fullReductionRuleService;

    @Operation(summary = "满减规则列表")@PostMapping(value = "/list")
    public BasicResult list(@RequestBody @Validated(value = {}) FullReductionRule fullReductionRule){
        BasicData basicResult = new BasicData();

        if(fullReductionRule.getShopId() == null){
            throw new StoneCustomerException("店铺id不能为空");
        }

        Page<FullReductionRule> page = fullReductionRuleService.getListByPage(fullReductionRule.getPageNo(), fullReductionRule.getPageSize(), fullReductionRule);

        return BasicResult.success(page);
    }

    /*@Operation(summary = "查看满减规则详情")@PostMapping(value = "/selectById")
    public BasicResult selectById(@RequestParam(value = "id", required = true) int id){
        BasicData basicResult = new BasicData();

        FullReductionRule fullReductionRule=fullReductionRuleService.selectByPrimaryKey(id);

        basicResult.setData(fullReductionRule);
        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("获取成功");
        return basicResult;
    }*/


}
