package com.siam.package_promotion.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_promotion.entity.CouponsMemberRelation;
import com.siam.package_promotion.service.CouponsMemberRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
public class CouponsMemberRelationFeignProvider implements CouponsMemberRelationFeignApi {

    @Autowired
    private CouponsMemberRelationService couponsMemberRelationService;

    public BasicResult insertSelective(CouponsMemberRelation record){
        couponsMemberRelationService.insertSelective(record);
        return BasicResult.success();
    }

    public BasicResult updateOverdue(){
        couponsMemberRelationService.updateOverdue();
        return BasicResult.success();
    }

    /**
     * 使用优惠卷
     */
    public BasicResult updateCouponsUsed(@RequestParam("id") Integer id, @RequestParam("isUsed") Boolean isUsed){
        couponsMemberRelationService.updateCouponsUsed(id, isUsed);
        return BasicResult.success();
    }

    /**
     * 通过id查询
     * @param id
     * @return
     */
    public BasicResult selectCouponsMemberRelationByPrimaryKey(@RequestParam("id") Integer id){
        return BasicResult.success(couponsMemberRelationService.selectCouponsMemberRelationByPrimaryKey(id));
    }

    @Override
    public BasicResult overdueSMSReminder() {
        couponsMemberRelationService.overdueSMSReminder();
        return BasicResult.success();
    }

    public BasicResult sverdueSMSReminder(){
        couponsMemberRelationService.overdueSMSReminder();
        return BasicResult.success();
    }
}