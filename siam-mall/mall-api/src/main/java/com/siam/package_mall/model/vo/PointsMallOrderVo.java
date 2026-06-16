package com.siam.package_mall.model.vo;

import com.siam.package_mall.entity.PointsMallOrderDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
 @Schema(description= "订单表")
public class PointsMallOrderVo {

    private Object order;

    private List<PointsMallOrderDetail> orderDetailList;

}