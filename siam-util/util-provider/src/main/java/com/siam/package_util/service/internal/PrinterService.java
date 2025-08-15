package com.siam.package_util.service.internal;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.siam.package_util.model.dto.OrderDetailPrintDto;
import com.siam.package_util.model.dto.OrderPrintDto;
import com.siam.package_util.entity.internal.Printer;

import java.util.List;
import java.util.Map;

/**
 *  暹罗
 */
public interface PrinterService extends IService<Printer> {

    void deleteByPrimaryKey(Integer id);

    void insertSelective(Printer printer);

    Printer selectByPrimaryKey(Integer id);

    void updateByPrimaryKeySelective(Printer printer);

    /**
     * 条件查询集合
     * @param printer 查询条件对象
     * @param pageNo 分页所在页
     * @param pageSize 单页数据量大小
     * @return
     */
    Page getList(int pageNo, int pageSize, Printer printer);

    /**
     * 条件查询集合
     * @param printer 查询条件对象
     * @param pageNo 分页所在页
     * @param pageSize 单页数据量大小
     * @return
     */
    Page<Map<String, Object>> getListJoinShop(int pageNo, int pageSize, Printer printer);

    /**
     * 打印后厨总单
     * @param orderPrintDto
     * @param orderDetailPrintDtoList
     * @return
     */
    void kitchenTotalOrderPrint(OrderPrintDto orderPrintDto, List<OrderDetailPrintDto> orderDetailPrintDtoList, Integer printerId);

    /**
     * 打印后厨单商品
     * @param orderPrintDto
     * @param orderDetailPrintDto
     * @return
     */
    void kitchenSingleGoodsOrderPrint(OrderPrintDto orderPrintDto, OrderDetailPrintDto orderDetailPrintDto, Integer printerId);

    /**
     * 结账单打印
     *
     * @param orderPrintDto 订单信息
     * @param orderDetailPrintDtoList  订单商品信息
     */
    void checkoutOrderPrint(OrderPrintDto orderPrintDto, List<OrderDetailPrintDto> orderDetailPrintDtoList, Integer printerId);
}