package com.siam.package_goods.feign;

import com.siam.package_goods.entity.SysMessage;
import com.siam.package_goods.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
@RequestMapping(value = "/rest/messageFeign")
public class MessageFeign {

    @Autowired
    private MessageService messageService;

    @RequestMapping(value = "/insertSelective")
    public void insertSelective(@RequestBody SysMessage sysMessage){
        messageService.insertSelective(sysMessage);
    }
}