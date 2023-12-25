package com.siam.package_mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_mall.entity.PointsMallOrderDetail;
import com.siam.package_mall.model.example.PointsMallOrderDetailExample;

import java.util.List;

public interface PointsMallOrderDetailService {
    int countByExample(PointsMallOrderDetailExample example);

    void deleteByPrimaryKey(Integer id);

    void insertSelective(PointsMallOrderDetail record);

    List<PointsMallOrderDetail> selectByExample(PointsMallOrderDetailExample example);

    PointsMallOrderDetail selectByPrimaryKey(Integer id);

    void updateByExampleSelective(PointsMallOrderDetail record, PointsMallOrderDetailExample example);

    void updateByPrimaryKeySelective(PointsMallOrderDetail record);

    Page getListByPage(int pageNo, int pageSize, PointsMallOrderDetail orderDetail);

    List<PointsMallOrderDetail> selectByPointsMallOrderId(Long orderId);

}