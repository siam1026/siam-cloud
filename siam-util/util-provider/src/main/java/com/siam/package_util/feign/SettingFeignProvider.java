package com.siam.package_util.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_util.entity.Setting;
import com.siam.package_util.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
public class SettingFeignProvider implements SettingFeignApi {

    @Autowired
    private SettingService settingService;

    /**
     * 查询基础数据设置
     * @author 暹罗
     */
    public BasicResult selectCurrent(){
        return BasicResult.success(settingService.selectCurrent());
    }
}