package com.siam.package_mall.model.result;

import com.siam.package_mall.entity.PointsMallOrder;
import com.siam.package_mall.entity.PointsMallOrderDetail;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "订单表")
public class PointsMallOrderResult extends PointsMallOrder {

    //订单商品详情列表
    private List<PointsMallOrderDetail> orderDetailList;
}