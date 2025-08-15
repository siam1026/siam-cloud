package com.siam.package_order.service_impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.siam.package_order.mapper.OrderMapper;
import com.siam.package_order.entity.OrderDetail;
import com.siam.package_order.model.example.OrderDetailExample;
import com.siam.package_order.mapper.OrderDetailMapper;
import com.siam.package_order.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private OrderMapper orderMapper;

    public void deleteByPrimaryKey(Integer id){
        orderDetailMapper.deleteById(id);
    }

    public void insertSelective(OrderDetail record){
        orderDetailMapper.insert(record);
    }

    public OrderDetail selectByPrimaryKey(Integer id){
        return orderDetailMapper.selectById(id);
    }

    public void updateByPrimaryKeySelective(OrderDetail record){
        orderDetailMapper.updateById(record);
    }

    public Page getListByPage(int pageNo, int pageSize, OrderDetail orderDetail) {
        Page<Map<String, Object>> page = orderDetailMapper.getListByPage(new Page(pageNo, pageSize), orderDetail);
        return page;
    }

    @Override
    public List<OrderDetail> selectByOrderId(Integer orderId) {
        return orderDetailMapper.selectByOrderId(orderId);
    }


}