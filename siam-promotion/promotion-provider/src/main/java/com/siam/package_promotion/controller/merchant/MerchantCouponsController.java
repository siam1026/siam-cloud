package com.siam.package_promotion.controller.merchant;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.annoation.MerchantPermission;
import com.siam.package_common.constant.BasicResultCode;
import com.siam.package_common.constant.Quantity;
import com.siam.package_common.entity.BasicData;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_goods.entity.Goods;
import com.siam.package_goods.feign.GoodsFeignApi;
import com.siam.package_goods.model.example.GoodsExample;
import com.siam.package_goods.model.param.GoodsParam;
import com.siam.package_merchant.auth.cache.MerchantSessionManager;
import com.siam.package_merchant.entity.Merchant;
import com.siam.package_promotion.entity.Coupons;
import com.siam.package_promotion.entity.CouponsGoodsRelation;
import com.siam.package_promotion.entity.CouponsShopRelation;
import com.siam.package_promotion.model.dto.CouponsDto;
import com.siam.package_promotion.service.CouponsGoodsRelationService;
import com.siam.package_promotion.service.CouponsService;
import com.siam.package_promotion.service.CouponsShopRelationService;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/rest/merchant/coupons")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "商家端优惠卷接口", description = "MerchantCouponsController")
public class MerchantCouponsController {

    @Autowired
    private CouponsService couponsService;

    @Autowired
    private CouponsShopRelationService couponsShopRelationService;

//    @Autowired
//    private MerchantService merchantService;

    @Autowired
    private CouponsGoodsRelationService couponsGoodsRelationService;

    @Autowired
    private GoodsFeignApi goodsFeignApi;

    @Autowired
    private MerchantSessionManager merchantSessionManager;

    @Operation(summary = "新增优惠卷")@PostMapping(value = "/insert")
    public BasicResult insert(@RequestBody @Validated(value = {}) Coupons coupons, HttpServletRequest request) {
        BasicResult basicResult = new BasicResult();

        //获取当前登录用户绑定的门店编号
        Merchant loginMerchant = merchantSessionManager.getSession(TokenUtil.getToken());

        //添加优惠券
        couponsService.insertSelective(coupons);

        //添加优惠券与店铺的关系
        CouponsShopRelation couponsShopRelation = new CouponsShopRelation();
        couponsShopRelation.setCouponsId(coupons.getId());
        couponsShopRelation.setShopId(loginMerchant.getShopId());
        couponsShopRelation.setCreateTime(new Date());
        couponsShopRelationService.insertSelective(couponsShopRelation);

        //所有状态的商品都查出来
        GoodsParam goodsParam = new GoodsParam();
        goodsParam.setShopId(loginMerchant.getShopId());
        List<Goods> goodsList = goodsFeignApi.selectByExample(goodsParam).getData();
        //添加优惠券与商品的关系，默认关联所有商品
        CouponsGoodsRelation couponsGoodsRelation = new CouponsGoodsRelation();
        for (Goods goods : goodsList) {
            couponsGoodsRelation.setId(null);
            couponsGoodsRelation.setCouponsId(coupons.getId());
            couponsGoodsRelation.setGoodsId(goods.getId());
            couponsGoodsRelation.setCreateTime(new Date());
            couponsGoodsRelationService.insertSelective(couponsGoodsRelation);
        }

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("新增成功");
        return basicResult;
    }

    @MerchantPermission
    @Operation(summary = "修改优惠卷-TODO")@PutMapping(value = "/update")
    public BasicResult update(@RequestBody @Validated(value = {}) Coupons coupons, HttpServletRequest request){
        BasicResult basicResult = new BasicResult();

        //todo 暹罗
        couponsService.updateByPrimaryKeySelective(coupons);

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("修改成功");
        return basicResult;
    }

    @MerchantPermission
    @Operation(summary = "删除优惠卷")@DeleteMapping(value = "/delete")
    public BasicResult delete(@RequestBody @Validated(value = {}) Coupons param){
        BasicResult basicResult = new BasicResult();

        couponsService.deleteByPrimaryKey(param.getId());

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("删除成功");
        return basicResult;
    }

    @Operation(summary = "查看优惠卷详情（包含关联商品）")@PostMapping(value = "/selectById")
    public BasicResult selectById(@RequestBody @Validated(value = {}) Coupons param){
        BasicData basicResult = new BasicData();

        Map map = couponsService.selectCouponsAndGoodsByPrimaryKey(param.getId());

        basicResult.setData(map);
        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("获取成功");
        return basicResult;
    }

    @Operation(summary = "优惠卷列表")@PostMapping(value = "/list")
    public BasicResult list(@RequestBody @Validated(value = {}) CouponsDto couponsDto, HttpServletRequest request) {
        BasicData basicResult = new BasicData();

        //获取当前登录用户绑定的门店编号
        Merchant loginMerchant = merchantSessionManager.getSession(TokenUtil.getToken());

        couponsDto.setShopId(loginMerchant.getShopId());
        couponsDto.setIsDelete(false);
        couponsDto.setSource(Quantity.INT_1);
        Page<Map<String, Object>> page = couponsService.getListJoinCouponsShopRelationByPage(couponsDto.getPageNo(), couponsDto.getPageSize(), couponsDto);

        return BasicResult.success(page);
    }

    @Operation(summary = "优惠卷时间修改（延长）")@PostMapping(value = "/updateEndTime")
    public BasicResult updateEndTime(@RequestBody @Validated(value = {}) Coupons coupons){
        BasicResult basicResult = new BasicResult();

        //修改时间service
        couponsService.updateCouponsEndTime(coupons);

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("x修改成功");
        return basicResult;
    }
}
