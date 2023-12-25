package com.siam.package_mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_mall.entity.PointsMallOrderLogistics;
import com.siam.package_mall.model.example.PointsMallOrderLogisticsExample;

import java.util.List;

public interface PointsMallOrderLogisticsService {
    int countByExample(PointsMallOrderLogisticsExample example);

    void deleteByPrimaryKey(Integer id);

    void deleteByExample(PointsMallOrderLogisticsExample example);

    void insertSelective(PointsMallOrderLogistics record);

    List<PointsMallOrderLogistics> selectByExample(PointsMallOrderLogisticsExample example);

    PointsMallOrderLogistics selectByPrimaryKey(Integer id);

    void updateByExampleSelective(PointsMallOrderLogistics record, PointsMallOrderLogisticsExample example);

    void updateByPrimaryKeySelective(PointsMallOrderLogistics record);

    Page getListByPage(int pageNo, int pageSize, PointsMallOrderLogistics orderLogistics);

    Boolean updateOrderLogisticsInfo(Long orderId, String logisticsNo);
}