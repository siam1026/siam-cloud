package com.siam.package_goods.feign.internal;

import com.siam.package_goods.entity.internal.PointsMallFullReductionRule;
import com.siam.package_goods.service.internal.PointsMallFullReductionRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
@RequestMapping(value = "/rest/pointsMallFullReductionRuleFeign")
public class PointsMallFullReductionRuleFeign {

    @Autowired
    private PointsMallFullReductionRuleService pointsMallFullReductionRuleService;

    @RequestMapping(value = "/insertSelective")
    void insertSelective(@RequestBody PointsMallFullReductionRule record){
        pointsMallFullReductionRuleService.insertSelective(record);
    }

    @RequestMapping(value = "/selectByPrimaryKey")
    PointsMallFullReductionRule selectByPrimaryKey(@RequestParam("id") Integer id){
        return pointsMallFullReductionRuleService.selectByPrimaryKey(id);
    }

    @RequestMapping(value = "/updateByPrimaryKeySelective")
    void updateByPrimaryKeySelective(@RequestBody PointsMallFullReductionRule record){
        pointsMallFullReductionRuleService.updateByPrimaryKeySelective(record);
    }
}