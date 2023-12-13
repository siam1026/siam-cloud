package com.siam.package_util.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_util.service.SystemUsageRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
public class SystemUsageRecordFeignProvider implements SystemUsageRecordFeignApi {

    @Autowired
    private SystemUsageRecordService systemUsageRecordService;

    @Override
    public BasicResult<Integer> selectCountIntoShop(Integer shopId, Date startTime, Date endTime) {
        return BasicResult.success(systemUsageRecordService.selectCountIntoShop(shopId, startTime, endTime));
    }

    @Override
    public BasicResult<Integer> selectCountIntoPointsMall(Date startTime, Date endTime) {
        return BasicResult.success(systemUsageRecordService.selectCountIntoPointsMall(startTime, endTime));
    }
}