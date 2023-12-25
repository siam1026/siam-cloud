package com.siam.package_mall.model.vo;

import com.siam.package_mall.entity.PointsMallOrderDetail;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "订单表")
public class PointsMallOrderVo {

    private Object order;

    private List<PointsMallOrderDetail> orderDetailList;

}