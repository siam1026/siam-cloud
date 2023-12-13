package com.siam.package_promotion.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_promotion.entity.CouponsMemberRelation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "promotion-siam")
public interface CouponsMemberRelationFeignApi {

    @PostMapping(value = "/api/couponsMemberRelation/insertSelective")
    BasicResult insertSelective(@RequestBody CouponsMemberRelation record);

    @PostMapping(value = "/api/couponsMemberRelation/updateOverdue")
    BasicResult updateOverdue();

    /**
     * 使用优惠卷
     */
    @PostMapping(value = "/updateCouponsUsed")
    BasicResult updateCouponsUsed(@RequestParam("id") Integer id, @RequestParam("isUsed") Boolean isUsed);

    /**
     * 通过id查询
     * @param id
     * @return
     */
    @PostMapping(value = "/selectCouponsMemberRelationByPrimaryKey")
    BasicResult<CouponsMemberRelation> selectCouponsMemberRelationByPrimaryKey(@RequestParam("id") Integer id);

    @PostMapping(value = "/overdueSMSReminder")
    BasicResult overdueSMSReminder();
}