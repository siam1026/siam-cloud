package com.siam.package_merchant.controller.merchant;

import com.siam.package_common.annoation.MerchantPermission;
import com.siam.package_util.entity.Setting;
import com.siam.package_util.feign.SettingFeignApi;
import com.siam.package_common.entity.BasicData;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_common.constant.BasicResultCode;
import com.siam.package_common.constant.Quantity;
import com.siam.package_common.exception.StoneCustomerException;
import com.siam.package_merchant.auth.cache.MerchantSessionManager;
import com.siam.package_merchant.entity.Merchant;
import com.siam.package_user.util.TokenUtil;
import com.siam.package_merchant.entity.Shop;
import com.siam.package_merchant.entity.ShopChangeRecord;
import com.siam.package_merchant.service.ShopChangeRecordService;
import com.siam.package_merchant.service.ShopService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping(value = "/rest/merchant/shop")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "商家端门店模块相关接口", description = "MerchantShopController")
public class MerchantShopController {

    @Autowired
    private MerchantSessionManager merchantSessionManager;

    @Autowired
    private ShopService shopService;

    @Autowired
    private ShopChangeRecordService shopChangeRecordService;

    @Autowired
    private SettingFeignApi settingFeignApi;

    @Operation(summary = "申请开店-提交门店信息")
    @PostMapping(value = "/apply")
    public BasicResult apply(@RequestBody @Validated(value = {}) Shop shop, HttpServletRequest request){
        BasicResult basicResult = new BasicResult();

        Merchant loginMerchant = merchantSessionManager.getSession(TokenUtil.getToken());

        if(loginMerchant.getAuditStatus() == Quantity.INT_2){
            throw new StoneCustomerException("您的门店信息已审核通过，不能重复申请");
        }

        if(loginMerchant.getShopId() != shop.getId()){
            throw new StoneCustomerException("您没有权限提交此门店信息");
        }

        Setting setting = settingFeignApi.selectCurrent().getData();
        shop.setStartDeliveryPrice(setting.getStartDeliveryPrice());

        shop.setUpdateTime(new Date());
        shopService.updateByPrimaryKeySelective(shop);

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("提交成功");
        return basicResult;
    }

    @Operation(summary = "获取登录商家账号的开店信息")
    @PostMapping(value = "/getLoginMerchantShopInfo")
    public BasicResult getLoginMerchantShopInfo(@RequestBody @Validated(value = {}) Shop param){
        BasicData basicResult = new BasicData();
        Merchant loginMerchant = merchantSessionManager.getSession(TokenUtil.getToken());

        Shop shop = shopService.selectByPrimaryKey(loginMerchant.getShopId());
        if(shop == null){
            throw new StoneCustomerException("无法获取开店信息，请稍后重试");
        }

        basicResult.setData(shop);
        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("查询成功");
        return basicResult;
    }

    @MerchantPermission
    @PostMapping(value = "/update")
    public BasicResult update(@RequestBody @Validated(value = {}) Shop shop, HttpServletRequest request){
        BasicResult basicResult = new BasicResult();

        Merchant loginMerchant = merchantSessionManager.getSession(TokenUtil.getToken());

        if (loginMerchant.getShopId() != shop.getId()){
            throw new StoneCustomerException("您没有权限修改此门店信息");
        }

        Shop dbShop = shopService.selectByPrimaryKey(shop.getId());
        if(dbShop == null){
            throw new StoneCustomerException("该店铺信息不存在");
        }

        shop.setUpdateTime(new Date());
        shopService.updateByPrimaryKeySelective(shop);

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("修改成功");
        return basicResult;
    }

    @PostMapping(value = "/applyChangeImportantData")
    public BasicResult applyChangeImportantData(@RequestBody @Validated(value = {}) ShopChangeRecord shopChangeRecord, HttpServletRequest request){
        BasicResult basicResult = new BasicResult();

        Merchant loginMerchant = merchantSessionManager.getSession(TokenUtil.getToken());

        if (loginMerchant.getShopId() != shopChangeRecord.getShopId()){
            throw new StoneCustomerException("您没有权限修改此门店信息");
        }

        Shop dbShop = shopService.selectByPrimaryKey(shopChangeRecord.getShopId());
        if(dbShop == null){
            throw new StoneCustomerException("该店铺信息不存在");
        }

        String applyChangeContent = getApplyChangeContent(dbShop, shopChangeRecord);
        if(StringUtils.isBlank(applyChangeContent)){
            throw new StoneCustomerException("没有变更信息，提交失败");
        }
        shopChangeRecord.setApplyChangeContent(applyChangeContent);

        ShopChangeRecord dbShopChangeRecord = shopChangeRecordService.selectLastestByShopId(shopChangeRecord.getShopId());
        if(dbShopChangeRecord==null || dbShopChangeRecord.getAuditStatus()==Quantity.INT_2  || dbShopChangeRecord.getAuditStatus()==Quantity.INT_3){
            shopChangeRecord.setAuditStatus(Quantity.INT_1);
            shopChangeRecord.setAuditReason(null);
            shopChangeRecord.setAuditTime(null);
            shopChangeRecord.setCreateTime(new Date());
            shopChangeRecord.setUpdateTime(new Date());
            shopChangeRecordService.insertSelective(shopChangeRecord);
        }else{
            shopChangeRecord.setId(dbShopChangeRecord.getId());
            shopChangeRecord.setAuditStatus(null);
            shopChangeRecord.setAuditReason(null);
            shopChangeRecord.setAuditTime(null);
            shopChangeRecord.setUpdateTime(new Date());
            shopChangeRecordService.updateByPrimaryKeySelective(shopChangeRecord);
        }

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("修改成功");
        return basicResult;
    }

    public String getApplyChangeContent(Shop dbShop, ShopChangeRecord shopChangeRecord){
        String applyChangeContent = "";
        if(shopChangeRecord.getName()!=null && !shopChangeRecord.getName().equals(dbShop.getName())){
            applyChangeContent += "店铺名称、";
        }
        if(shopChangeRecord.getProvince()!=null && !shopChangeRecord.getProvince().equals(dbShop.getProvince())){
            applyChangeContent += "所在省份、";
        }
        if(shopChangeRecord.getCity()!=null && !shopChangeRecord.getCity().equals(dbShop.getCity())){
            applyChangeContent += "所在城市、";
        }
        if(shopChangeRecord.getArea()!=null && !shopChangeRecord.getArea().equals(dbShop.getArea())){
            applyChangeContent += "所在区/县、";
        }
        if(shopChangeRecord.getStreet()!=null && !shopChangeRecord.getStreet().equals(dbShop.getStreet())){
            applyChangeContent += "门店地址、";
        }
        if(shopChangeRecord.getManagePrimary()!=null && !shopChangeRecord.getManagePrimary().equals(dbShop.getManagePrimary())){
            applyChangeContent += "营业类目、";
        }
        if(shopChangeRecord.getShopWithinImg()!=null && !shopChangeRecord.getShopWithinImg().equals(dbShop.getShopWithinImg())){
            applyChangeContent += "店内照片、";
        }
        if(shopChangeRecord.getShopLogoImg()!=null && !shopChangeRecord.getShopLogoImg().equals(dbShop.getShopLogoImg())){
            applyChangeContent += "门店头像、";
        }
        if(shopChangeRecord.getContactPhone()!=null && !shopChangeRecord.getContactPhone().equals(dbShop.getContactPhone())){
            applyChangeContent += "联系电话、";
        }
        if(shopChangeRecord.getAnnouncement()!=null && !shopChangeRecord.getAnnouncement().equals(dbShop.getAnnouncement())){
            applyChangeContent += "门店公告、";
        }
        if(shopChangeRecord.getBriefIntroduction()!=null && !shopChangeRecord.getBriefIntroduction().equals(dbShop.getBriefIntroduction())){
            applyChangeContent += "门店简介、";
        }
        if(shopChangeRecord.getBusinessLicense()!=null && !shopChangeRecord.getBusinessLicense().equals(dbShop.getBusinessLicense())){
            applyChangeContent += "营业执照、";
        }
        if(shopChangeRecord.getIdCardFrontSide()!=null && !shopChangeRecord.getIdCardFrontSide().equals(dbShop.getIdCardFrontSide())){
            applyChangeContent += "身份证正面、";
        }
        if(shopChangeRecord.getIdCardBackSide()!=null && !shopChangeRecord.getIdCardBackSide().equals(dbShop.getIdCardBackSide())){
            applyChangeContent += "身份证反面、";
        }
        if(shopChangeRecord.getHouseNumber()!=null && !shopChangeRecord.getHouseNumber().equals(dbShop.getHouseNumber())){
            applyChangeContent += "门牌号、";
        }
        if(shopChangeRecord.getLongitude()!=null && dbShop.getLongitude()==null){
            applyChangeContent += "经度、";
        }else if(shopChangeRecord.getLongitude()!=null && dbShop.getLongitude()!=null && shopChangeRecord.getLongitude().compareTo(dbShop.getLongitude())!=0){
            applyChangeContent += "经度、";
        }
        if(shopChangeRecord.getLatitude()!=null && dbShop.getLatitude()==null){
            applyChangeContent += "纬度、";
        }else if(shopChangeRecord.getLatitude()!=null && dbShop.getLatitude()!=null && shopChangeRecord.getLatitude().compareTo(dbShop.getLatitude())!=0){
            applyChangeContent += "纬度、";
        }

        applyChangeContent = applyChangeContent != "" ? applyChangeContent.substring(0, applyChangeContent.length()-1) : applyChangeContent;
        return applyChangeContent;
    }
}
