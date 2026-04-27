package com.siam.package_promotion.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.annoation.AdminPermission;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_promotion.entity.Advertisement;
import com.siam.package_promotion.model.param.AdvertisementParam;
import com.siam.package_promotion.service.AdvertisementService;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/rest/admin/advertisement")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "后台广告轮播图模块相关接口", description = "AdminAdvertisementController")
public class AdminAdvertisementController {

    @Autowired
    private AdvertisementService advertisementService;

    @Operation(summary = "广告轮播图列表")
    @PostMapping(value = "/list")
    public BasicResult list(@RequestBody @Validated(value = {}) AdvertisementParam param) {
        Page<Advertisement> page = advertisementService.getListByPage(param);
        return BasicResult.success(page);
    }

    @Operation(summary = "新增广告轮播图")
    @PostMapping(value = "/insert")
    public BasicResult insert(@RequestBody @Validated(value = {}) AdvertisementParam param) {
        advertisementService.insertSelective(param);
        return BasicResult.success();
    }


    @AdminPermission
    @Operation(summary = "修改广告轮播图")
    @PutMapping(value = "/update")
    public BasicResult update(@RequestBody @Validated(value = {}) AdvertisementParam param) {
        advertisementService.updateByPrimaryKeySelective(param);
        return BasicResult.success();
    }

    @AdminPermission
    @Operation(summary = "删除广告轮播图")
    @DeleteMapping(value = "/delete")
    public BasicResult delete(@RequestBody @Validated(value = {}) AdvertisementParam param) {
        //删除广告轮播图
        advertisementService.deleteByPrimaryKey(param);
        return BasicResult.success();
    }

    @Operation(summary = "获取单个广告轮播图详情信息")
    @PostMapping(value = "/getById")
    public BasicResult getById(@RequestBody @Validated(value = {}) AdvertisementParam param) {
        Advertisement advertisement = advertisementService.selectByPrimaryKey(param);
        return BasicResult.success(advertisement);
    }
}