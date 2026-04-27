package com.siam.package_util.controller.merchant;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.entity.BasicData;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_merchant.auth.cache.MerchantSessionManager;
import com.siam.package_merchant.entity.Merchant;
import com.siam.package_user.util.TokenUtil;
import com.siam.package_util.entity.PictureUploadRecord;
import com.siam.package_util.service.PictureUploadRecordService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/rest/merchant/pictureUploadRecord")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "商家端图片上传记录模块相关接口【暂无调用】", description = "MerchantPictureUploadRecordController")
public class MerchantPictureUploadRecordController {

    @Autowired
    private PictureUploadRecordService pictureUploadRecordService;

    @Autowired
    private MerchantSessionManager merchantSessionManager;

    @Operation(summary = "图片上传记录列表")@PostMapping(value = "/list")
    public BasicResult list(@RequestBody @Validated(value = {}) PictureUploadRecord pictureUploadRecord, HttpServletRequest request){
        BasicData basicResult = new BasicData();

        //获取当前登录用户绑定的门店编号
        Merchant loginMerchant = merchantSessionManager.getSession(TokenUtil.getToken());

        pictureUploadRecord.setShopId(loginMerchant.getShopId());
        Page<PictureUploadRecord> page = pictureUploadRecordService.getList(pictureUploadRecord.getPageNo(), pictureUploadRecord.getPageSize(), pictureUploadRecord);

        return BasicResult.success(page);
    }
}