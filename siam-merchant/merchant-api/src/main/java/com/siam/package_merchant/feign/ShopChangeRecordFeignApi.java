package com.siam.package_merchant.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_merchant.entity.Shop;
import com.siam.package_merchant.model.example.ShopChangeRecordExample;
import com.siam.package_merchant.model.example.ShopExample;
import com.siam.package_merchant.model.param.ShopChangeRecordParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@FeignClient(value = "merchant-siam")
public interface ShopChangeRecordFeignApi {

    @PostMapping(value = "/api/shopChangeRecord/selectByExample")
    BasicResult<Integer> countByExample(@RequestBody ShopChangeRecordParam param);
}