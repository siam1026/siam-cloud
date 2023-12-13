package com.siam.package_rider.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_rider.entity.Rider;
import com.siam.package_rider.model.example.RiderExample;
import com.siam.package_rider.model.param.RiderParam;
import com.siam.package_rider.service.RiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
public class RiderFeignProvider implements RiderFeignApi {

    @Autowired
    private RiderService riderService;

    public BasicResult countByExample(RiderParam param) {
//        return BasicResult.success(riderService.countByExample(example));
        return BasicResult.success();
    }

    public BasicResult insertSelective(Rider record){
        riderService.insertSelective(record);
        return BasicResult.success();
    }

    public BasicResult selectByExample(RiderParam param) {
//        return BasicResult.success(riderService.selectByExample(example));
        return BasicResult.success();
    }

    public BasicResult selectByPrimaryKey(@RequestParam("id") Integer id){
        return BasicResult.success(riderService.selectByPrimaryKey(id));
    }

    public BasicResult updateByPrimaryKeySelective(Rider record){
        riderService.updateByPrimaryKeySelective(record);
        return BasicResult.success();
    }

    public BasicResult selectByMobile(@RequestParam("mobile") String mobile){
//        return BasicResult.success(riderService.selectByMobile(mobile));
        return BasicResult.success();
    }
}