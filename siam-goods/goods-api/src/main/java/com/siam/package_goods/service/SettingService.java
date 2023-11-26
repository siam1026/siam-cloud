package com.siam.package_goods.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_goods.entity.Setting;

/**
 *  jianyang
 */
public interface SettingService {

    void deleteByPrimaryKey(Integer id);

    void insertSelective(Setting setting);

    Setting selectByPrimaryKey(Integer id);

    void updateByPrimaryKeySelective(Setting setting);

    Page getListByPage(int pageNo, int pageSize, Setting setting);

    /**
     * 查询基础数据设置
     * @author 暹罗
     */
    Setting selectCurrent();
}