package com.siam.package_promotion.controller.member;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.entity.BasicData;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_promotion.entity.Advertisement;
import com.siam.package_promotion.model.param.AdvertisementParam;
import com.siam.package_promotion.service.AdvertisementService;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/rest/advertisement")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "广告轮播图模块相关接口", description = "AdvertisementController")
public class AdvertisementController {
    @Autowired
    private AdvertisementService advertisementService;

    @Operation(summary = "广告轮播图列表")
    @PostMapping(value = "/list")
    public BasicResult list(@RequestBody @Validated(value = {}) AdvertisementParam advertisement){
        BasicData basicResult = new BasicData();

        Page<Advertisement> page = advertisementService.getListByPage(advertisement);

        return BasicResult.success(page);
    }
}