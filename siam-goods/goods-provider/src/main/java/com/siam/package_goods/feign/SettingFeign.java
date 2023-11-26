package com.siam.package_goods.feign;

import com.siam.package_goods.entity.Setting;
import com.siam.package_goods.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
@RequestMapping(value = "/rest/settingFeign")
public class SettingFeign {

    @Autowired
    private SettingService settingService;

    /**
     * 查询基础数据设置
     * @author 暹罗
     */
    @RequestMapping(value = "/selectCurrent")
    Setting selectCurrent(){
        return settingService.selectCurrent();
    }
}