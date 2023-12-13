package com.siam.package_user.feign.fallback;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_user.entity.Admin;
import com.siam.package_user.feign.AdminFeignApi;
import com.siam.package_user.model.example.AdminExample;
import org.springframework.stereotype.Component;

@Component
public class AdminFallback implements AdminFeignApi {

//    @Override
//    public BasicResult countByExample(AdminExample example) {
//        return null;
//    }

    @Override
    public BasicResult insertSelective(Admin record) {
        return null;
    }

//    @Override
//    public BasicResult selectByExample(AdminExample example) {
//        return null;
//    }

    @Override
    public BasicResult selectByPrimaryKey(Integer id) {
        return null;
    }

    @Override
    public BasicResult updateByPrimaryKeySelective(Admin record) {
        return null;
    }
}