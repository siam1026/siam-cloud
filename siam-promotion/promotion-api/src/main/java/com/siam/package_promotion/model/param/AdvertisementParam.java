package com.siam.package_promotion.model.param;

import com.siam.package_promotion.entity.Advertisement;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "广告轮播图表")
public class AdvertisementParam extends Advertisement {

    //开始日期
    private Date startCreateTime;

    //结束日期
    private Date endCreateTime;

    //页码
    private Integer pageNo = 1;

    //页面大小
    private Integer pageSize = 20;
}