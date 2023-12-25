package com.siam.package_mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_mall.entity.PointsMallOrderRefundProcess;
import com.siam.package_mall.model.example.PointsMallOrderRefundProcessExample;

import java.util.List;

public interface PointsMallOrderRefundProcessService {
    int countByExample(PointsMallOrderRefundProcessExample example);

    void deleteByPrimaryKey(Integer id);

    void insertSelective(PointsMallOrderRefundProcess record);

    List<PointsMallOrderRefundProcess> selectByExample(PointsMallOrderRefundProcessExample example);

    PointsMallOrderRefundProcess selectByPrimaryKey(Integer id);

    void updateByExampleSelective(PointsMallOrderRefundProcess record, PointsMallOrderRefundProcessExample example);

    void updateByPrimaryKeySelective(PointsMallOrderRefundProcess record);

    Page getListByPage(int pageNo, int pageSize, PointsMallOrderRefundProcess orderRefundProcess);

    List<PointsMallOrderRefundProcess> selectByPointsMallOrderRefundId(Integer orderRefundId);
}