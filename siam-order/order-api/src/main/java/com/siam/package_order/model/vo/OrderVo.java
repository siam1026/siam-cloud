package com.siam.package_order.model.vo;

import com.siam.package_order.entity.OrderDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
 @Schema(description= "订单表")
public class OrderVo {

    private Object order;

    private List<OrderDetail> orderDetailList;
}