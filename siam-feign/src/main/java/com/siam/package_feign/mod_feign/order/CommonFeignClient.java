package com.siam.package_feign.mod_feign.order;

import com.siam.package_order.entity.TravelingDistanceVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(value = "order-siam", path = "/rest/commonFeign")
public interface CommonFeignClient {

    /**
     * 计算订单配送费
     */
    @RequestMapping(value = "/selectDeliveryFee")
    BigDecimal selectDeliveryFee(@RequestParam("lngA") BigDecimal lngA, @RequestParam("latA") BigDecimal latA, @RequestParam("shopId") Integer shopId);

    /**
     * 计算两地的距离 / 用户当前定位与平台各店铺的距离
     */
    @RequestMapping(value = "/selectTravelingDistance")
    TravelingDistanceVo selectTravelingDistance(@RequestParam("lngA") BigDecimal lngA, @RequestParam("latA") BigDecimal latA, @RequestParam("lngB") BigDecimal lngB, @RequestParam("latB") BigDecimal latB);

    /**
     * 查询门店是否营业/店铺是否打烊
     *
     * @param shopId
     */
    @RequestMapping(value = "/selectIsOperatingOfShop")
    Boolean selectIsOperatingOfShop(@RequestParam("shopId") Integer shopId);

}