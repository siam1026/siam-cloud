package com.siam.package_util.controller.member;

import com.siam.package_common.constant.BasicResultCode;
import com.siam.package_common.entity.BasicData;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_util.entity.Setting;
import com.siam.package_util.service.SettingService;
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
@RequestMapping(value = "/rest/setting")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "基础数据设置模块相关接口", description = "SettingController")
public class SettingController {
    @Autowired
    private SettingService settingService;

    @Operation(summary = "查询基础数据设置")@PostMapping(value = "/selectCurrent")
    public BasicResult selectCurrent(@RequestBody @Validated(value = {}) Setting setting){
        BasicData basicResult = new BasicData();

        Setting currentSetting = settingService.selectCurrent();

        basicResult.setData(currentSetting);
        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("查询成功");
        return basicResult;
    }
}