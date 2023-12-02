package com.siam.package_feign.mod_feign.goods.internal;

import com.siam.package_goods.entity.internal.PointsMallFullReductionRule;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "goods-siam", path = "/rest/pointsMallFullReductionRuleFeign")
public interface PointsMallFullReductionRuleFeignClient {

    @RequestMapping(value = "/insertSelective")
    void insertSelective(@RequestBody PointsMallFullReductionRule record);

    @RequestMapping(value = "/selectByPrimaryKey")
    PointsMallFullReductionRule selectByPrimaryKey(@RequestParam("id") Integer id);

    @RequestMapping(value = "/updateByPrimaryKeySelective")
    void updateByPrimaryKeySelective(@RequestBody PointsMallFullReductionRule record);
}