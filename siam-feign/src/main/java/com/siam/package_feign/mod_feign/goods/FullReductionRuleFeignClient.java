package com.siam.package_feign.mod_feign.goods;

import com.siam.package_goods.entity.FullReductionRule;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "goods-siam", path = "/rest/fullReductionRuleFeign")
public interface FullReductionRuleFeignClient {

    @RequestMapping(value = "/insertSelective")
    void insertSelective(@RequestBody FullReductionRule record);

    @RequestMapping(value = "/selectByPrimaryKey")
    FullReductionRule selectByPrimaryKey(@RequestParam("id") Integer id);

    @RequestMapping(value = "/updateByPrimaryKeySelective")
    void updateByPrimaryKeySelective(@RequestBody FullReductionRule record);
}