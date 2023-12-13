package com.siam.package_promotion.service_impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_promotion.entity.PaperworkPush;
import com.siam.package_promotion.mapper.PaperworkPushMapper;
import com.siam.package_promotion.service.PaperworkPushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PaperworkPushServiceImpl implements PaperworkPushService {

    @Autowired
    private PaperworkPushMapper paperworkPushMapper;

    @Override
    public void deleteByPrimaryKey(Integer id) {
        paperworkPushMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void insertSelective(PaperworkPush paperworkPush) {
        paperworkPushMapper.insertSelective(paperworkPush);
    }

    @Override
    public PaperworkPush selectByPrimaryKey(Integer id) {
        return paperworkPushMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateByPrimaryKeySelective(PaperworkPush paperworkPush) {
        paperworkPushMapper.updateByPrimaryKeySelective(paperworkPush);
    }

    @Override
    public Page getList(int pageNo, int pageSize, PaperworkPush paperworkPush) {
        Page<Map<String, Object>> page = paperworkPushMapper.getListByPage(new Page(pageNo, pageSize), paperworkPush);
        return page;
    }
}