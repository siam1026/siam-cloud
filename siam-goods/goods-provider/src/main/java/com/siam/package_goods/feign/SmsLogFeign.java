package com.siam.package_goods.feign;

import com.siam.package_goods.entity.SmsLog;
import com.siam.package_goods.service.SmsLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
@RequestMapping(value = "/rest/smsLogFeign")
public class SmsLogFeign {

    @Autowired
    private SmsLogService smsLogService;

    /**
     * 获取最新的短信验证码记录
     **/
    @RequestMapping(value = "/getLastLog")
    public SmsLog getLastLog(@RequestParam("mobile") String mobile, @RequestParam("type") String type, @RequestParam("minutes") int minutes){
        return smsLogService.getLastLog(mobile, type, minutes);
    }
}