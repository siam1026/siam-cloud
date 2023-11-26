package com.siam.package_goods.feign;

import com.siam.package_goods.entity.CouponsMemberRelation;
import com.siam.package_goods.service.CouponsMemberRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
@RequestMapping(value = "/rest/couponsMemberRelationFeign")
public class CouponsMemberRelationFeign {

    @Autowired
    private CouponsMemberRelationService couponsMemberRelationService;

    @RequestMapping(value = "/insertSelective")
    public void insertSelective(@RequestBody CouponsMemberRelation record){
        couponsMemberRelationService.insertSelective(record);
    }

    @RequestMapping(value = "/updateOverdue")
    public void updateOverdue(){
        couponsMemberRelationService.updateOverdue();
    }

    /**
     * 使用优惠卷
     */
    @RequestMapping(value = "/updateCouponsUsed")
    public void updateCouponsUsed(@RequestParam("id") Integer id, @RequestParam("isUsed") Boolean isUsed){
        couponsMemberRelationService.updateCouponsUsed(id, isUsed);
    }

    /**
     * 通过id查询
     * @param id
     * @return
     */
    @RequestMapping(value = "/selectCouponsMemberRelationByPrimaryKey")
    public CouponsMemberRelation selectCouponsMemberRelationByPrimaryKey(@RequestParam("id") Integer id){
        return couponsMemberRelationService.selectCouponsMemberRelationByPrimaryKey(id);
    }

    @RequestMapping(value = "/overdueSMSReminder")
    public void overdueSMSReminder(){
        couponsMemberRelationService.overdueSMSReminder();
    }
}