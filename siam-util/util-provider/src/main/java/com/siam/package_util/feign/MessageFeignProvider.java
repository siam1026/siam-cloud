package com.siam.package_util.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_util.entity.SysMessage;
import com.siam.package_util.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
public class MessageFeignProvider implements MessageFeignApi {

    @Autowired
    private MessageService messageService;

    public BasicResult insertSelective(SysMessage sysMessage){
        messageService.insertSelective(sysMessage);
        return BasicResult.success();
    }
}