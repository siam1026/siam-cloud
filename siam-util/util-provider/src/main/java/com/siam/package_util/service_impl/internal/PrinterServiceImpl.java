package com.siam.package_util.service_impl.internal;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.siam.package_common.constant.Quantity;
import com.siam.package_util.entity.internal.Printer;
import com.siam.package_util.mapper.internal.PrinterMapper;
import com.siam.package_util.model.dto.OrderDetailPrintDto;
import com.siam.package_util.model.dto.OrderPrintDto;
import com.siam.package_util.service.internal.PrinterService;
import com.siam.package_util.util.XinYeYunUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PrinterServiceImpl extends ServiceImpl<PrinterMapper, Printer> implements PrinterService {

    @Autowired
    private PrinterMapper printerMapper;

    @Autowired
    private XinYeYunUtils xinYeYunUtils;

    @Override
    public void deleteByPrimaryKey(Integer id) {
        printerMapper.deleteById(id);
    }

    @Override
    public void insertSelective(Printer printer) {
        printerMapper.insert(printer);
    }

    @Override
    public Printer selectByPrimaryKey(Integer id) {
        return printerMapper.selectById(id);
    }

    @Override
    public void updateByPrimaryKeySelective(Printer printer) {
        printerMapper.updateById(printer);
    }

    @Override
    public Page getList(int pageNo, int pageSize, Printer printer) {
        Page<Map<String, Object>> page = printerMapper.getListByPage(new Page(pageNo, pageSize), printer);
        return page;
    }

    @Override
    public Page<Map<String, Object>> getListJoinShop(int pageNo, int pageSize, Printer printer) {
        Page<Map<String, Object>> page = printerMapper.getListJoinShop(new Page(pageNo, pageSize), printer);
        return page;
    }

    @Override
    public void kitchenTotalOrderPrint(OrderPrintDto orderPrintDto, List<OrderDetailPrintDto> orderDetailPrintDtoList, Integer printerId) {
        // 查询打印机信息
        Printer dbPrinter = this.getById(printerId);
        if (dbPrinter == null) {
            log.error("后厨总单打印机不存在,printerId=" + printerId);
            return;
        }

        String sn = dbPrinter.getIdentifyingCode();
        Integer brand = dbPrinter.getBrand();
//        String name = dbPrinter.getName();

        // 设置sn
        orderPrintDto.setPrinterSn(sn);

        // todo - 改成策略模式
        if(brand == Quantity.INT_2){
            JSONObject response = xinYeYunUtils.kitchenTotalOrderDataPrint(orderPrintDto, orderDetailPrintDtoList);
            if (response.getBoolean("success")) {
                JSONObject data = response.getJSONObject("data");
                if (data.getInteger("code") == 0) {
                    log.info("后厨总单打印成功");
                }else{
                    log.error("后厨总单打印失败，" + data.getString("msg"));
                    return;
                }
            }
        }else{
            log.warn("后厨总单打印机品牌不对,brand=" + brand);
        }
    }

    @Override
    public void kitchenSingleGoodsOrderPrint(OrderPrintDto orderPrintDto, OrderDetailPrintDto orderDetailPrintDto, Integer printerId) {
        // 打印机信息
        Printer dbPrinter = this.getById(printerId);
        if (dbPrinter == null) {
            log.error("后厨单商品打印机不存在,printerId=" + printerId);
            return;
        }

        String sn = dbPrinter.getIdentifyingCode();
        Integer brand = dbPrinter.getBrand();
        String name = dbPrinter.getName();

        // 设置sn
        orderDetailPrintDto.setPrinterSn(sn);
        orderDetailPrintDto.setPrinterName(name);

        // todo - 改成策略模式
        if(brand == Quantity.INT_2){
            JSONObject response = xinYeYunUtils.kitchenTotalOrderDataPrint(orderPrintDto, orderDetailPrintDto);
            if (response.getBoolean("success")) {
                JSONObject data = response.getJSONObject("data");
                if (data.getInteger("code") == 0) {
                    log.info("后厨单商品打印成功");
                }else{
                    log.error("后厨单商品打印失败，" + data.getString("msg"));
                    return;
                }
            }
        }else{
            log.warn("后厨单商品打印机品牌不对,brand=" + brand);
        }
    }

    @Override
    public void checkoutOrderPrint(OrderPrintDto orderPrintDto, List<OrderDetailPrintDto> orderDetailPrintDtoList, Integer printerId) {
        // 查询打印机信息
        Printer dbPrinter = this.getById(printerId);
        if (dbPrinter == null) {
            log.error("结账单打印机不存在,printerId=" + printerId);
            return;
        }

        String sn = dbPrinter.getIdentifyingCode();
        Integer brand = dbPrinter.getBrand();
//        String name = dbPrinter.getName();

        // 设置sn
        orderPrintDto.setPrinterSn(sn);

        // todo - 改成策略模式
        if(brand == Quantity.INT_2){
            JSONObject response = xinYeYunUtils.checkoutDataPrint( orderPrintDto, orderDetailPrintDtoList);
            if (response.getBoolean("success")) {
                JSONObject data = response.getJSONObject("data");
                if (data.getInteger("code") == 0) {
                    log.info("结账单打印成功");
                }else{
                    log.error("结账单打印失败，" + data.getString("msg"));
                    return;
                }
            }
        }else{
            log.warn("结账单打印机品牌不对,brand=" + brand);
        }
    }
}