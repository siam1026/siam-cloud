package com.siam.package_util.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_util.entity.internal.Printer;
import com.siam.package_util.model.param.CheckoutOrderPrintParam;
import com.siam.package_util.model.param.KitchenSingleGoodsOrderPrintParam;
import com.siam.package_util.model.param.KitchenTotalOrderPrintParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "util-siam")
public interface PrinterFeignApi {

    @PostMapping(value = "/api/printer/selectByPrimaryKey")
    BasicResult<Printer> selectByPrimaryKey(@RequestParam("id") Integer id);

    /**
     * 打印后厨总单
     * @param param
     * @return
     */
    @PostMapping(value = "/api/printer/kitchenTotalOrderPrint")
    BasicResult kitchenTotalOrderPrint(@RequestBody KitchenTotalOrderPrintParam param);

    /**
     * 打印后厨单商品
     * @param param
     * @return
     */
    @PostMapping(value = "/api/printer/kitchenSingleGoodsOrderPrint")
    BasicResult kitchenSingleGoodsOrderPrint(@RequestBody KitchenSingleGoodsOrderPrintParam param);

    /**
     * 结账单打印
     *
     * @param param 订单信息
     */
    @PostMapping(value = "/api/printer/checkoutOrderPrint")
    BasicResult checkoutOrderPrint(@RequestBody CheckoutOrderPrintParam param);
}