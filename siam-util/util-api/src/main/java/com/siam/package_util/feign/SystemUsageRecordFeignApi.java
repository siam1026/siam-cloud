package com.siam.package_util.feign;

import com.siam.package_common.entity.BasicResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@FeignClient(value = "util-siam")
public interface SystemUsageRecordFeignApi {

    /**
     * 查询进店人数
     */
    @PostMapping(value = "/api/systemUsageRecord/selectCountIntoShop")
    BasicResult<Integer> selectCountIntoShop(@RequestParam(value = "shopId", required = false) Integer shopId, @RequestParam("startTime") Date startTime, @RequestParam("endTime") Date endTime);

    /**
     * 查询进入积分商城人数
     */
    @PostMapping(value = "/api/systemUsageRecord/selectCountIntoPointsMall")
    BasicResult<Integer> selectCountIntoPointsMall(@RequestParam("startTime") Date startTime, @RequestParam("endTime") Date endTime);
}