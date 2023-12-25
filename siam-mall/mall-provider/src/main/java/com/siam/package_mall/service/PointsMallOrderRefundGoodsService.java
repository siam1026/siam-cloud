package com.siam.package_mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_mall.entity.PointsMallOrderRefundGoods;
import com.siam.package_mall.model.example.PointsMallOrderRefundGoodsExample;

import java.util.List;

public interface PointsMallOrderRefundGoodsService {
    int countByExample(PointsMallOrderRefundGoodsExample example);

    void deleteByPrimaryKey(Integer id);

    void insertSelective(PointsMallOrderRefundGoods record);

    List<PointsMallOrderRefundGoods> selectByExample(PointsMallOrderRefundGoodsExample example);

    PointsMallOrderRefundGoods selectByPrimaryKey(Integer id);

    void updateByExampleSelective(PointsMallOrderRefundGoods record, PointsMallOrderRefundGoodsExample example);

    void updateByPrimaryKeySelective(PointsMallOrderRefundGoods record);

    Page getListByPage(int pageNo, int pageSize, PointsMallOrderRefundGoods orderRefundGoods);

    List<PointsMallOrderRefundGoods> selectByPointsMallOrderRefundId(Integer orderRefundId);
}