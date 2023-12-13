package com.siam.package_rider.service_impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_rider.entity.Rider;
import com.siam.package_rider.mapper.RiderMapper;
import com.siam.package_rider.service.RiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RiderServiceImpl implements RiderService {

    @Autowired
    private RiderMapper riderMapper;

    @Override
    public void deleteByPrimaryKey(Integer id) {
        riderMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void insertSelective(Rider rider) {
        riderMapper.insertSelective(rider);
    }

    @Override
    public Rider selectByPrimaryKey(Integer id) {
        return riderMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateByPrimaryKeySelective(Rider rider) {
        riderMapper.updateByPrimaryKeySelective(rider);
    }

    @Override
    public Page getList(int pageNo, int pageSize, Rider rider) {
        Page<Map<String, Object>> page = riderMapper.getListByPage(new Page(pageNo, pageSize), rider);
        return page;
    }

    @Override
    public Page<Map<String, Object>> getListJoinShop(int pageNo, int pageSize, Rider rider) {
        Page<Map<String, Object>> page = riderMapper.getListJoinShop(new Page(pageNo, pageSize), rider);
        return page;
    }
}