package com.siam.package_promotion.controller.merchant;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_common.constant.BasicResultCode;
import com.siam.package_common.util.GsonUtils;
import com.siam.package_promotion.entity.CouponsGoodsRelation;
import com.siam.package_promotion.service.CouponsGoodsRelationService;
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

import java.util.List;

@RestController
@RequestMapping(value = "/rest/merchant/couponsGoodsRelation")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "商家端优惠卷接口", description = "MerchantCouponsGoodsRelationController")
public class MerchantCouponsGoodsRelationController {

    @Autowired
    private CouponsGoodsRelationService couponsGoodsRelationService;

    @Operation(summary = "新增优惠卷商品关系")@PostMapping(value = "/insert")
    public BasicResult insert(@RequestBody @Validated(value = {}) CouponsGoodsRelation param) {
        BasicResult basicResult = new BasicResult();

        List<Integer> goodsIdList = GsonUtils.toList(param.getGoodsIdListStr(), Integer.class);
        couponsGoodsRelationService.insertSelective(param.getCouponsId(), goodsIdList);

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("新增成功");
        return basicResult;
    }

    @Operation(summary = "优惠卷商品关系列表查询")@PostMapping(value = "/list")
    public BasicResult list(@RequestBody @Validated(value = {}) CouponsGoodsRelation couponsGoodsRelation) {
        BasicResult basicResult = new BasicResult();

        couponsGoodsRelationService.getListByPage(couponsGoodsRelation.getPageNo(), couponsGoodsRelation.getPageSize(), couponsGoodsRelation);

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("新增成功");
        return basicResult;
    }
}