package com.siam.package_feign.mod_feign.fallback;

import com.siam.package_user.entity.Admin;
import com.siam.package_user.model.example.AdminExample;
import com.siam.package_feign.mod_feign.user.AdminFeignClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminFallback implements AdminFeignClient {

    @Override
    public int countByExample(AdminExample example) {
        return 0;
    }

    @Override
    public void insertSelective(Admin record) {

    }

    @Override
    public List<Admin> selectByExample(AdminExample example) {
        return null;
    }

    @Override
    public Admin selectByPrimaryKey(Integer id) {
        return null;
    }

    @Override
    public void updateByPrimaryKeySelective(Admin record) {

    }
}