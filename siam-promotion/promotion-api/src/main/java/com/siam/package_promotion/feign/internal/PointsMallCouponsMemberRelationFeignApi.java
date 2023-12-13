package com.siam.package_promotion.feign.internal;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_promotion.entity.internal.PointsMallCouponsMemberRelation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "promotion-siam")
public interface PointsMallCouponsMemberRelationFeignApi {

    @PostMapping(value = "/api/pointsMallCouponsMemberRelation/insertSelective")
    BasicResult insertSelective(@RequestBody PointsMallCouponsMemberRelation record);

    @PostMapping(value = "/api/pointsMallCouponsMemberRelation/updateOverdue")
    BasicResult updateOverdue();

    /**
     * 使用优惠卷
     */
    @PostMapping(value = "/api/pointsMallCouponsMemberRelation/updateCouponsUsed")
    BasicResult updateCouponsUsed(@RequestParam("id") Integer id, @RequestParam("isUsed") Boolean isUsed);

    /**
     * 通过id查询
     * @param id
     * @return
     */
    @PostMapping(value = "/api/pointsMallCouponsMemberRelation/selectPointsMallCouponsMemberRelationByPrimaryKey")
    BasicResult<PointsMallCouponsMemberRelation> selectPointsMallCouponsMemberRelationByPrimaryKey(@RequestParam("id") Integer id);
}