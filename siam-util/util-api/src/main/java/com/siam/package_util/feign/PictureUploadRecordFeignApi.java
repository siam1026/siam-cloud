package com.siam.package_util.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_util.entity.PictureUploadRecord;
import com.siam.package_util.model.param.PictureUploadRecordParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "util-siam")
public interface PictureUploadRecordFeignApi {

    @PostMapping(value = "/api/pictureUploadRecord/countByExample")
    BasicResult<Integer> countByExample(@RequestBody PictureUploadRecordParam param);

    @PostMapping(value = "/api/pictureUploadRecord/insertSelective")
    BasicResult insertSelective(@RequestBody PictureUploadRecord pictureUploadRecord);
}