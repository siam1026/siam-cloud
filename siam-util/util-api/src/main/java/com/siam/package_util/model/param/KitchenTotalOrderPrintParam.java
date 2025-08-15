package com.siam.package_util.model.param;

import com.siam.package_util.model.dto.OrderDetailPrintDto;
import com.siam.package_util.model.dto.OrderPrintDto;
import lombok.Data;

import java.util.List;

@Data
public class KitchenTotalOrderPrintParam {

    private OrderPrintDto orderPrintDto;

    private List<OrderDetailPrintDto> orderDetailPrintDtoList;

    private Integer printerId;
}