package com.siam.package_util.model.param;

import com.siam.package_util.entity.PictureUploadRecord;
import com.siam.package_util.model.dto.OrderDetailPrintDto;
import com.siam.package_util.model.dto.OrderPrintDto;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Data
public class CheckoutOrderPrintParam {

    private OrderPrintDto orderPrintDto;

    private List<OrderDetailPrintDto> orderDetailPrintDtoList;

    private Integer printerId;
}