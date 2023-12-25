package com.siam.package_mall.service_impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_mall.mapper.PointsMallOrderMapper;
import com.siam.package_mall.mapper.PointsMallOrderRefundGoodsMapper;
import com.siam.package_mall.service.PointsMallOrderRefundGoodsService;
import com.siam.package_mall.entity.PointsMallOrderRefundGoods;
import com.siam.package_mall.model.example.PointsMallOrderRefundGoodsExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PointsMallOrderRefundGoodsServiceImpl implements PointsMallOrderRefundGoodsService {

    @Autowired
    private PointsMallOrderRefundGoodsMapper orderRefundGoodsMapper;

    @Autowired
    private PointsMallOrderMapper orderMapper;

    public int countByExample(PointsMallOrderRefundGoodsExample example){
        return orderRefundGoodsMapper.countByExample(example);
    }

    public void deleteByPrimaryKey(Integer id){
        orderRefundGoodsMapper.deleteByPrimaryKey(id);
    }

    public void insertSelective(PointsMallOrderRefundGoods record){
        orderRefundGoodsMapper.insertSelective(record);
    }

    public List<PointsMallOrderRefundGoods> selectByExample(PointsMallOrderRefundGoodsExample example){
        return orderRefundGoodsMapper.selectByExample(example);
    }

    public PointsMallOrderRefundGoods selectByPrimaryKey(Integer id){
        return orderRefundGoodsMapper.selectByPrimaryKey(id);
    }

    public void updateByExampleSelective(PointsMallOrderRefundGoods record, PointsMallOrderRefundGoodsExample example){
        orderRefundGoodsMapper.updateByExampleSelective(record, example);
    }

    public void updateByPrimaryKeySelective(PointsMallOrderRefundGoods record){
        orderRefundGoodsMapper.updateByPrimaryKeySelective(record);
    }

    public Page getListByPage(int pageNo, int pageSize, PointsMallOrderRefundGoods orderRefundGoods) {
        Page<Map<String, Object>> page = orderRefundGoodsMapper.getListByPage(new Page(pageNo, pageSize), orderRefundGoods);
        return page;
    }

    @Override
    public List<PointsMallOrderRefundGoods> selectByPointsMallOrderRefundId(Integer orderRefundId) {
        return orderRefundGoodsMapper.selectByPointsMallOrderRefundId(orderRefundId);
    }
}