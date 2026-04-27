package com.siam.package_mall.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_mall.service.PointsMallCouponsGoodsRelationService;
import com.siam.package_common.entity.BasicData;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_common.constant.BasicResultCode;
import com.siam.package_common.util.GsonUtils;
import com.siam.package_mall.entity.PointsMallCouponsGoodsRelation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/rest/admin/pointsMall/couponsGoodsRelation")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "后台优惠卷接口", description = "AdminPointsMallCouponsController")
public class AdminPointsMallCouponsGoodsRelationController {

    @Autowired
    private PointsMallCouponsGoodsRelationService pointsMallCouponsGoodsRelationService;

    @Operation(summary = "新增优惠卷商品关系")
    @PostMapping(value = "/insert")
    public BasicResult insert(@RequestBody @Validated(value = {}) PointsMallCouponsGoodsRelation param) {
        BasicResult basicResult = new BasicResult();

        List<Integer> goodsIdList = GsonUtils.toList(param.getGoodsIdListStr(), Integer.class);
        pointsMallCouponsGoodsRelationService.insertSelective(param.getCouponsId(), goodsIdList);

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("新增成功");
        return basicResult;
    }

    @Operation(summary = "优惠卷商品关系列表查询")
    @PostMapping(value = "/list")
    public BasicResult list(@RequestBody @Validated(value = {}) PointsMallCouponsGoodsRelation couponsGoodsRelation) {
        BasicData basicResult = new BasicData();

        Page<PointsMallCouponsGoodsRelation> page = pointsMallCouponsGoodsRelationService.getListByPage(couponsGoodsRelation.getPageNo(), couponsGoodsRelation.getPageSize(), couponsGoodsRelation);

        return BasicResult.success(page);
    }


}
