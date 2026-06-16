package com.siam.package_merchant.controller.admin;

import com.siam.package_merchant.service.MerchantService;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rest/admin/merchant")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "后台商家账号模块相关接口", description = "AdminMerchantController")
public class AdminMerchantController {

    @Autowired
    private MerchantService merchantService;

}