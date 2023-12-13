package com.siam.package_rider.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_rider.entity.Rider;

import java.util.Map;

/**
 *  暹罗
 */
public interface RiderService {

    void deleteByPrimaryKey(Integer id);

    void insertSelective(Rider rider);

    Rider selectByPrimaryKey(Integer id);

    void updateByPrimaryKeySelective(Rider rider);

    /**
     * 条件查询集合
     * @param rider 查询条件对象
     * @param pageNo 分页所在页
     * @param pageSize 单页数据量大小
     * @return
     */
    Page getList(int pageNo, int pageSize, Rider rider);

    /**
     * 条件查询集合
     * @param rider 查询条件对象
     * @param pageNo 分页所在页
     * @param pageSize 单页数据量大小
     * @return
     */
    Page<Map<String, Object>> getListJoinShop(int pageNo, int pageSize, Rider rider);
}