package com.siam.package_goods.feign;

import com.siam.package_goods.entity.FullReductionRule;
import com.siam.package_goods.service.FullReductionRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
@RequestMapping(value = "/rest/fullReductionRuleFeign")
public class FullReductionRuleFeign {

    @Autowired
    private FullReductionRuleService fullReductionRuleService;

    @RequestMapping(value = "/insertSelective")
    void insertSelective(@RequestBody FullReductionRule record){
        fullReductionRuleService.insertSelective(record);
    }

    @RequestMapping(value = "/selectByPrimaryKey")
    FullReductionRule selectByPrimaryKey(@RequestParam("id") Integer id){
        return fullReductionRuleService.selectByPrimaryKey(id);
    }

    @RequestMapping(value = "/updateByPrimaryKeySelective")
    void updateByPrimaryKeySelective(@RequestBody FullReductionRule record){
        fullReductionRuleService.updateByPrimaryKeySelective(record);
    }
}