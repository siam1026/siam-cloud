package com.siam.package_feign.mod_feign.goods;

import com.siam.package_goods.entity.Setting;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "goods-siam", path = "/rest/settingFeign")
public interface SettingFeignClient {

    /**
     * 查询基础数据设置
     * @author 暹罗
     */
    @RequestMapping(value = "/selectCurrent")
    Setting selectCurrent();
}