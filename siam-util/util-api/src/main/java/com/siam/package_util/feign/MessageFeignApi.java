package com.siam.package_util.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_util.entity.SysMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "util-siam")
public interface MessageFeignApi {

    @PostMapping(value = "/api/message/insertSelective")
    BasicResult insertSelective(@RequestBody SysMessage record);
}