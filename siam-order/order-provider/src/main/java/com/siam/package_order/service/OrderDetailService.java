package com.siam.package_order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.siam.package_order.entity.OrderDetail;
import com.siam.package_order.model.example.OrderDetailExample;

import java.util.List;

public interface OrderDetailService extends IService<OrderDetail> {

    void deleteByPrimaryKey(Integer id);

    void insertSelective(OrderDetail record);

    OrderDetail selectByPrimaryKey(Integer id);

    void updateByPrimaryKeySelective(OrderDetail record);

    Page getListByPage(int pageNo, int pageSize, OrderDetail orderDetail);

    List<OrderDetail> selectByOrderId(Integer orderId);

}