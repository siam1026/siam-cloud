package com.siam.package_promotion.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_promotion.entity.FullReductionRule;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "promotion-siam")
public interface FullReductionRuleFeignApi {

    @PostMapping(value = "/api/fullReductionRule/insertSelective")
    BasicResult insertSelective(@RequestBody FullReductionRule record);

    @PostMapping(value = "/api/fullReductionRule/selectByPrimaryKey")
    BasicResult<FullReductionRule> selectByPrimaryKey(@RequestParam("id") Integer id);

    @PostMapping(value = "/api/fullReductionRule/updateByPrimaryKeySelective")
    BasicResult updateByPrimaryKeySelective(@RequestBody FullReductionRule record);

    @PostMapping(value = "/api/fullReductionRule/selectByShopId")
    BasicResult<List<FullReductionRule>> selectByShopId(@RequestBody FullReductionRule fullReductionRule);
}