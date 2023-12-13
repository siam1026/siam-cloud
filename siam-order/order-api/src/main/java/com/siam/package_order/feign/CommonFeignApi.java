package com.siam.package_order.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_order.entity.TravelingDistanceVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(value = "order-siam")
public interface CommonFeignApi {

    /**
     * 计算订单配送费
     */
    @PostMapping(value = "/api/common/selectDeliveryFee")
    BasicResult selectDeliveryFee(@RequestParam("lngA") BigDecimal lngA, @RequestParam("latA") BigDecimal latA, @RequestParam("shopId") Integer shopId);

    /**
     * 计算两地的距离 / 用户当前定位与平台各店铺的距离
     */
    @PostMapping(value = "/api/common/selectTravelingDistance")
    BasicResult<TravelingDistanceVo> selectTravelingDistance(@RequestParam("lngA") BigDecimal lngA, @RequestParam("latA") BigDecimal latA, @RequestParam("lngB") BigDecimal lngB, @RequestParam("latB") BigDecimal latB);

    /**
     * 查询门店是否营业/店铺是否打烊
     *
     * @param shopId
     */
    @PostMapping(value = "/api/common/selectIsOperatingOfShop")
    BasicResult<Boolean> selectIsOperatingOfShop(@RequestParam("shopId") Integer shopId);

}