package com.siam.package_merchant.feign;

import com.siam.package_common.constant.Quantity;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_merchant.model.example.ShopChangeRecordExample;
import com.siam.package_merchant.model.param.ShopChangeRecordParam;
import com.siam.package_merchant.service.ShopChangeRecordService;
import com.siam.package_util.model.example.PictureUploadRecordExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
public class ShopChangeRecordFeignProvider implements ShopChangeRecordFeignApi {

    @Autowired
    private ShopChangeRecordService shopChangeRecordService;

    @Override
    public BasicResult<Integer> countByExample(ShopChangeRecordParam param) {
        ShopChangeRecordExample example = new ShopChangeRecordExample();
        ShopChangeRecordExample.Criteria criteria = example.createCriteria();
        if(param.getAuditStatus() != null){
            criteria.andAuditStatusEqualTo(param.getAuditStatus());
        }
        return BasicResult.success(shopChangeRecordService.countByExample(example));
    }
}