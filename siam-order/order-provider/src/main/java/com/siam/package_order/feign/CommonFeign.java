package com.siam.package_order.feign;

import com.siam.package_order.entity.TravelingDistanceVo;
import com.siam.package_order.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@Slf4j
@RestController
@RequestMapping(value = "/rest/commonFeign")
public class CommonFeign {

    @Autowired
    private CommonService commonService;

    /**
     * 计算订单配送费
     */
    @RequestMapping(value = "/selectDeliveryFee")
    public BigDecimal selectDeliveryFee(@RequestParam("lngA") BigDecimal lngA, @RequestParam("latA") BigDecimal latA, @RequestParam("shopId") Integer shopId){
        return commonService.selectDeliveryFee(lngA, latA, shopId);
    }

    /**
     * 计算两地的距离 / 用户当前定位与平台各店铺的距离
     */
    @RequestMapping(value = "/selectTravelingDistance")
    public TravelingDistanceVo selectTravelingDistance(@RequestParam("lngA") BigDecimal lngA, @RequestParam("latA") BigDecimal latA, @RequestParam("lngB") BigDecimal lngB, @RequestParam("latB") BigDecimal latB){
        return commonService.selectTravelingDistance(lngA, latA, lngB, latB);
    }

    /**
     * 查询门店是否营业/店铺是否打烊
     *
     * @param shopId
     */
    @RequestMapping(value = "/selectIsOperatingOfShop")
    public Boolean selectIsOperatingOfShop(@RequestParam("shopId") Integer shopId){
        return commonService.selectIsOperatingOfShop(shopId);
    }
}