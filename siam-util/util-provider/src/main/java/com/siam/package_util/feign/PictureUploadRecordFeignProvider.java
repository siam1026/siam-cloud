package com.siam.package_util.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_merchant.model.example.MerchantBillingRecordExample;
import com.siam.package_util.entity.PictureUploadRecord;
import com.siam.package_util.model.example.PictureUploadRecordExample;
import com.siam.package_util.model.param.PictureUploadRecordParam;
import com.siam.package_util.service.PictureUploadRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
public class PictureUploadRecordFeignProvider implements PictureUploadRecordFeignApi {

    @Autowired
    private PictureUploadRecordService pictureUploadRecordService;


    @Override
    public BasicResult<Integer> countByExample(PictureUploadRecordParam param) {
        PictureUploadRecordExample example = new PictureUploadRecordExample();
        PictureUploadRecordExample.Criteria criteria = example.createCriteria();
        if(param.getUrl() != null){
            criteria.andUrlEqualTo(param.getUrl());
        }
        return BasicResult.success(pictureUploadRecordService.countByExample(example));
    }

    @Override
    public BasicResult insertSelective(PictureUploadRecord pictureUploadRecord) {
        pictureUploadRecordService.insertSelective(pictureUploadRecord);
        return BasicResult.success();
    }
}