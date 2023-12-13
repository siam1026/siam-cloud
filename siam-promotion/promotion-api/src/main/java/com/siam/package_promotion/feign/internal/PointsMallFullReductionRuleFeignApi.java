package com.siam.package_promotion.feign.internal;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_promotion.entity.internal.PointsMallFullReductionRule;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "promotion-siam")
public interface PointsMallFullReductionRuleFeignApi {

    @PostMapping(value = "/api/pointsMallFullReductionRule/insertSelective")
    BasicResult insertSelective(@RequestBody PointsMallFullReductionRule record);

    @PostMapping(value = "/api/pointsMallFullReductionRule/selectByPrimaryKey")
    BasicResult<PointsMallFullReductionRule> selectByPrimaryKey(@RequestParam("id") Integer id);

    @PostMapping(value = "/api/pointsMallFullReductionRule/updateByPrimaryKeySelective")
    BasicResult updateByPrimaryKeySelective(@RequestBody PointsMallFullReductionRule record);
}