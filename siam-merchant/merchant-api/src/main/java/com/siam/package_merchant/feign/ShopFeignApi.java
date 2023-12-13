package com.siam.package_merchant.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_merchant.entity.Shop;
import com.siam.package_merchant.model.example.ShopExample;
import com.siam.package_merchant.model.param.ShopParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@FeignClient(value = "merchant-siam")
public interface ShopFeignApi {

    @PostMapping(value = "/api/shop/insertSelective")
    BasicResult<Integer> insertSelective(@RequestBody Shop record);

    @PostMapping(value = "/api/shop/selectByPrimaryKey")
    BasicResult<Shop> selectByPrimaryKey(@RequestParam("id") Integer id);

    @PostMapping(value = "/api/shop/selectByExample")
    BasicResult<List<Shop>> selectByExample(@RequestBody ShopParam param);

    @PostMapping(value = "/api/shop/getListByCouponsId")
    BasicResult<List<Shop>> getListByCouponsId(@RequestParam("couponsId") Integer couponsId);

    @PostMapping(value = "/api/shop/selectByDistance")
    BasicResult<Page<Shop>> selectByDistance(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize, @RequestParam("positionLongitude") BigDecimal positionLongitude, @RequestParam("positionLatitude") BigDecimal positionLatitude, @RequestParam("deliveryDistanceLimit") BigDecimal deliveryDistanceLimit);

    @PostMapping(value = "/api/shop/countByExample")
    BasicResult<Integer> countByExample(ShopParam param);
}