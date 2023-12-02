package com.siam.package_feign.mod_feign.goods.internal;

import com.siam.package_goods.entity.internal.PointsMallCouponsMemberRelation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "goods-siam", path = "/rest/couponsMemberRelationFeign")
public interface PointsMallCouponsMemberRelationFeignClient {

    @RequestMapping(value = "/insertSelective")
    void insertSelective(@RequestBody PointsMallCouponsMemberRelation record);

    @RequestMapping(value = "/updateOverdue")
    void updateOverdue();

    /**
     * 使用优惠卷
     */
    @RequestMapping(value = "/updateCouponsUsed")
    void updateCouponsUsed(@RequestParam("id") Integer id, @RequestParam("isUsed") Boolean isUsed);

    /**
     * 通过id查询
     * @param id
     * @return
     */
    @RequestMapping(value = "/selectPointsMallCouponsMemberRelationByPrimaryKey")
    PointsMallCouponsMemberRelation selectPointsMallCouponsMemberRelationByPrimaryKey(@RequestParam("id") Integer id);
}