package com.siam.package_promotion.controller.member;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.entity.BasicData;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_common.constant.BasicResultCode;
import com.siam.package_promotion.entity.CouponsMemberRelation;
import com.siam.package_promotion.service.CouponsMemberRelationService;
import com.siam.package_user.auth.cache.MemberSessionManager;
import com.siam.package_user.entity.Member;
import com.siam.package_user.util.TokenUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping(value = "/rest/member/couponsMemberRelation")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "优惠卷用关系接口", description = "CouponsMemberRelationController")
public class CouponsMemberRelationController {

    @Autowired
    private CouponsMemberRelationService couponsMemberRelationService;

//    @Autowired
//    private MemberService memberService;

    @Autowired
    private MemberSessionManager memberSessionManager;

    @Operation(summary = "新增优惠卷用户关系")
    @PostMapping(value = "/insert")
    public BasicResult insert(@RequestBody @Validated(value = {}) CouponsMemberRelation couponsMemberRelation) {
        BasicResult basicResult = new BasicResult();

        couponsMemberRelationService.insertSelective(couponsMemberRelation);

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("新增成功");
        return basicResult;
    }

    @Operation(summary = "查看优惠卷关系详情（包含关联优惠卷）")@PostMapping(value = "/selectById")
    public BasicResult selectById(@RequestBody @Validated(value = {}) CouponsMemberRelation param){
        BasicData basicResult = new BasicData();

        CouponsMemberRelation couponsMemberRelation = couponsMemberRelationService.selectCouponsMemberRelationByPrimaryKey(param.getId());

        basicResult.setData(couponsMemberRelation);
        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("获取成功");
        return basicResult;
    }


    @Operation(summary = "优惠卷用户关系列表")@PostMapping(value = "/list")
    public BasicResult list(@RequestBody @Validated(value = {}) CouponsMemberRelation couponsMemberRelation, HttpServletRequest request) {
        BasicData basicResult = new BasicData();
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());

        //查询当前登录用户持有的优惠券
        couponsMemberRelation.setMemberId(loginMember.getId());
        Page<Map<String, Object>> page = couponsMemberRelationService.getListByPage(couponsMemberRelation.getPageNo(), couponsMemberRelation.getPageSize(), couponsMemberRelation);

        return BasicResult.success(page);
    }

    @Operation(summary = "查询优惠卷数量")
    @PostMapping(value = "/selectCounts")
    public BasicResult selectCounts( HttpServletRequest request){
        BasicData basicResult = new BasicData();
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        Integer memberId = loginMember.getId();

        Integer counts = couponsMemberRelationService.getCountsByMemberId(memberId);

        basicResult.setData(counts);
        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("获取成功");
        return basicResult;
    }

}