package com.siam.package_util.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_util.entity.SmsLog;
import com.siam.package_util.service.SmsLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
public class SmsLogFeignProvider implements SmsLogFeignApi {

    @Autowired
    private SmsLogService smsLogService;

    /**
     * 获取最新的短信验证码记录
     **/
    public BasicResult getLastLog(@RequestParam("mobile") String mobile, @RequestParam("type") String type, @RequestParam("minutes") int minutes){
        return BasicResult.success(smsLogService.getLastLog(mobile, type, minutes));
    }
}