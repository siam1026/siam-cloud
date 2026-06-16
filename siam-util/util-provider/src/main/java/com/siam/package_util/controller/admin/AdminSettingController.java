package com.siam.package_util.controller.admin;

import com.siam.package_common.annoation.AdminPermission;
import com.siam.package_common.constant.BasicResultCode;
import com.siam.package_common.entity.BasicData;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_goods.feign.GoodsSpecificationOptionFeignApi;
import com.siam.package_util.entity.Setting;
import com.siam.package_util.service.SettingService;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping(value = "/rest/admin/setting")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "后台基础数据设置模块相关接口", description = "AdminSettingController")
public class AdminSettingController {

    @Autowired
    private SettingService settingService;

    @Autowired
    private GoodsSpecificationOptionFeignApi goodsSpecificationOptionFeignApi;

    /*@Operation(summary = "基础数据设置列表")
    @PostMapping(value = "/list")
    public BasicResult list(int pageNo, int pageSize, Setting setting){
        BasicData basicResult = new BasicData();
        Page<Setting> page = settingService.getListByPage(param.getPageNo(), param.getPageSize(), setting);

        return BasicResult.success(page);
    }*/

    @Operation(summary = "基础数据设置列表")
    @PostMapping(value = "/selectCurrent")
    public BasicResult selectCurrent(@RequestBody @Validated(value = {}) Setting setting){
        BasicData basicResult = new BasicData();

        Setting current = settingService.selectCurrent();

        basicResult.setData(current);
        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("查询成功");
        return basicResult;
    }

    @AdminPermission
    @Operation(summary = "修改基础数据设置")
    @PutMapping(value = "/update")
    public BasicResult update(@RequestBody @Validated(value = {}) Setting setting){
        BasicResult basicResult = new BasicResult();

        setting.setUpdateTime(new Date());
        settingService.updateByPrimaryKeySelective(setting);

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("修改成功");
        return basicResult;
    }
}