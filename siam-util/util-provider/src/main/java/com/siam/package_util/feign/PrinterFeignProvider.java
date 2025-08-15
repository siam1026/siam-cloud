package com.siam.package_util.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_util.entity.internal.Printer;
import com.siam.package_util.model.param.CheckoutOrderPrintParam;
import com.siam.package_util.model.param.KitchenSingleGoodsOrderPrintParam;
import com.siam.package_util.model.param.KitchenTotalOrderPrintParam;
import com.siam.package_util.service.internal.PrinterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
public class PrinterFeignProvider implements PrinterFeignApi {

    @Autowired
    private PrinterService printerService;


    @Override
    public BasicResult<Printer> selectByPrimaryKey(Integer id) {
        return BasicResult.success(printerService.selectByPrimaryKey(id));
    }

    @Override
    public BasicResult kitchenTotalOrderPrint(KitchenTotalOrderPrintParam param) {
        printerService.kitchenTotalOrderPrint(param.getOrderPrintDto(), param.getOrderDetailPrintDtoList(), param.getPrinterId());
        return BasicResult.success();
    }

    @Override
    public BasicResult kitchenSingleGoodsOrderPrint(KitchenSingleGoodsOrderPrintParam param) {
        printerService.kitchenSingleGoodsOrderPrint(param.getOrderPrintDto(), param.getOrderDetailPrintDto(), param.getPrinterId());
        return BasicResult.success();
    }

    @Override
    public BasicResult checkoutOrderPrint(CheckoutOrderPrintParam param) {
        printerService.checkoutOrderPrint(param.getOrderPrintDto(), param.getOrderDetailPrintDtoList(), param.getPrinterId());
        return BasicResult.success();
    }
}