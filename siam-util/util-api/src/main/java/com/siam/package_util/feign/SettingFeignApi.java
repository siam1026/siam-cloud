package com.siam.package_util.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_util.entity.Setting;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "util-siam")
public interface SettingFeignApi {

    /**
     * 查询基础数据设置
     * @author 暹罗
     */
    @PostMapping(value = "/api/setting/selectCurrent")
    BasicResult<Setting> selectCurrent();
}