package com.siam.package_util.controller.member;

import com.siam.package_common.constant.BasicResultCode;
import com.siam.package_common.constant.Quantity;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_common.exception.StoneCustomerException;
import com.siam.package_common.service.AliyunSms;
import com.siam.package_common.util.CommonUtils;
import com.siam.package_common.util.RedisUtils;
import com.siam.package_util.entity.SmsLog;
import com.siam.package_util.service.SmsLogService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping(value = "/rest/smsLog")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "短信验证码记录模块相关接口", description = "SmsLogController")
public class SmsLogController {

    @Autowired
    private SmsLogService smsLogService;

    @Autowired
    private AliyunSms aliyunSms;

    @Autowired
    private RedisUtils redisUtils;

    @PostMapping(value = "/sendMobileCode")
    @Operation(summary = "发送手机验证码")public BasicResult sendMobileCode(@RequestBody @Validated(value = {}) SmsLog smsLog){
        BasicResult basicResult = new BasicResult();
        if(!CommonUtils.isMobile(smsLog.getMobile())){
            basicResult.setSuccess(false);
            basicResult.setCode(BasicResultCode.ERR);
            basicResult.setMessage("手机号不合法");
            return basicResult;
        }

        String smsSwitch = (String) redisUtils.get("smsSwitch");
        if(smsSwitch != null && smsSwitch.equals("false")){
            throw new StoneCustomerException("暂不支持获取验证码，请用临时验证码 123456 进行操作");
        }

        // 发送短信验证码
        String mobileCode = aliyunSms.sendVerificationCodeMessage(smsLog.getMobile());

        // 添加smslog记录
        SmsLog insertSmsLog = new SmsLog();
        insertSmsLog.setMemberId(null);
        insertSmsLog.setMobile(smsLog.getMobile());
        insertSmsLog.setType(smsLog.getType());
        insertSmsLog.setCreateTime(new Date());
        insertSmsLog.setIp(CommonUtils.getServerIP());
        insertSmsLog.setVerifyCode(mobileCode);
        insertSmsLog.setDescription(null);
        /*insertSmsLog.setStates(Quantity.INT_1);*/
        smsLogService.insertSelective(insertSmsLog);

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("发送成功");
        return basicResult;
    }
}