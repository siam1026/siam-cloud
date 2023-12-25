package com.siam.package_mall.service_impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_mall.mapper.PointsMallOrderDetailMapper;
import com.siam.package_mall.mapper.PointsMallOrderMapper;
import com.siam.package_mall.service.PointsMallOrderDetailService;
import com.siam.package_mall.entity.PointsMallOrderDetail;
import com.siam.package_mall.model.example.PointsMallOrderDetailExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PointsMallOrderDetailServiceImpl implements PointsMallOrderDetailService {

    @Autowired
    private PointsMallOrderDetailMapper orderDetailMapper;

    @Autowired
    private PointsMallOrderMapper orderMapper;

    public int countByExample(PointsMallOrderDetailExample example){
        return orderDetailMapper.countByExample(example);
    }

    public void deleteByPrimaryKey(Integer id){
        orderDetailMapper.deleteByPrimaryKey(id);
    }

    public void insertSelective(PointsMallOrderDetail record){
        orderDetailMapper.insertSelective(record);
    }

    public List<PointsMallOrderDetail> selectByExample(PointsMallOrderDetailExample example){
        return orderDetailMapper.selectByExample(example);
    }

    public PointsMallOrderDetail selectByPrimaryKey(Integer id){
        return orderDetailMapper.selectByPrimaryKey(id);
    }

    public void updateByExampleSelective(PointsMallOrderDetail record, PointsMallOrderDetailExample example){
        orderDetailMapper.updateByExampleSelective(record, example);
    }

    public void updateByPrimaryKeySelective(PointsMallOrderDetail record){
        orderDetailMapper.updateByPrimaryKeySelective(record);
    }

    public Page getListByPage(int pageNo, int pageSize, PointsMallOrderDetail orderDetail) {
        Page<Map<String, Object>> page = orderDetailMapper.getListByPage(new Page(pageNo, pageSize), orderDetail);
        return page;
    }

    @Override
    public List<PointsMallOrderDetail> selectByPointsMallOrderId(Long orderId) {
        return orderDetailMapper.selectByPointsMallOrderId(orderId);
    }


}