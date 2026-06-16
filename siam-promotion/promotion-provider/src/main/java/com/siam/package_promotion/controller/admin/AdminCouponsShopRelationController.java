package com.siam.package_promotion.controller.admin;

import com.siam.package_common.constant.BasicResultCode;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_common.util.GsonUtils;
import com.siam.package_promotion.entity.CouponsShopRelation;
import com.siam.package_promotion.service.CouponsShopRelationService;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/rest/admin/couponsShopRelation")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "后台优惠卷接口", description = "AdminCouponsShopRelationController")
public class AdminCouponsShopRelationController {

    @Autowired
    private CouponsShopRelationService couponsShopRelationService;

    @Operation(summary = "新增优惠卷店铺关系")
    @PostMapping(value = "/insert")
    public BasicResult insert(@RequestBody @Validated(value = {}) CouponsShopRelation param) {
        BasicResult basicResult = new BasicResult();

        List<Integer> shopIdList = GsonUtils.toList(param.getShopIdListStr(), Integer.class);
        couponsShopRelationService.insertSelective(param.getCouponsId(), shopIdList);

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("新增成功");
        return basicResult;
    }

    @Operation(summary = "优惠卷店铺关系列表查询")
    @PostMapping(value = "/list")
    public BasicResult list(@RequestBody @Validated(value = {}) CouponsShopRelation param) {
        BasicResult basicResult = new BasicResult();

        couponsShopRelationService.getListByPage(param.getPageNo(), param.getPageSize(), param);

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("新增成功");
        return basicResult;
    }
}