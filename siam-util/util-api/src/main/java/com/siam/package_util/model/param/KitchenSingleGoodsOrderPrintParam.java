package com.siam.package_util.model.param;

import com.siam.package_util.model.dto.OrderDetailPrintDto;
import com.siam.package_util.model.dto.OrderPrintDto;
import lombok.Data;

import java.util.List;

@Data
public class KitchenSingleGoodsOrderPrintParam {

    private OrderPrintDto orderPrintDto;

    private OrderDetailPrintDto orderDetailPrintDto;

    private Integer printerId;
}