package com.siam.package_promotion.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.entity.BasicData;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_common.constant.BasicResultCode;
import com.siam.package_common.util.GsonUtils;
import com.siam.package_promotion.entity.CouponsGoodsRelation;
import com.siam.package_promotion.service.CouponsGoodsRelationService;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/rest/admin/couponsGoodsRelation")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "后台优惠卷接口", description = "AdminCouponsController")
public class AdminCouponsGoodsRelationController {

    @Autowired
    private CouponsGoodsRelationService couponsGoodsRelationService;

    @Operation(summary = "新增优惠卷商品关系")
    @PostMapping(value = "/insert")
    public BasicResult insert(@RequestBody @Validated(value = {}) CouponsGoodsRelation param) {
        BasicResult basicResult = new BasicResult();

        List<Integer> goodsIdList = GsonUtils.toList(param.getGoodsIdListStr(), Integer.class);
        couponsGoodsRelationService.insertSelective(param.getCouponsId(), goodsIdList);

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("新增成功");
        return basicResult;
    }

    @Operation(summary = "优惠卷商品关系列表查询")
    @PostMapping(value = "/list")
    public BasicResult list(@RequestBody @Validated(value = {}) CouponsGoodsRelation param) {
        BasicData basicResult = new BasicData();

        Page<CouponsGoodsRelation> page = couponsGoodsRelationService.getListByPage(param.getPageNo(), param.getPageSize(), param);

        return BasicResult.success(page);
    }
}