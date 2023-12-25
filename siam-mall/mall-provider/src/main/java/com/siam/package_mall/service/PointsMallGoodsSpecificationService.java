package com.siam.package_mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_mall.model.dto.PointsMallGoodsSpecificationDto;
import com.siam.package_mall.entity.PointsMallGoodsSpecification;
import com.siam.package_mall.model.example.PointsMallGoodsSpecificationExample;

import java.util.List;
import java.util.Map;

public interface PointsMallGoodsSpecificationService {
    int countByExample(PointsMallGoodsSpecificationExample example);

    void deleteByPrimaryKey(Integer id);

    void insertSelective(PointsMallGoodsSpecification record);

    List<PointsMallGoodsSpecification> selectByExample(PointsMallGoodsSpecificationExample example);

    PointsMallGoodsSpecification selectByPrimaryKey(Integer id);

    void updateByExampleSelective(PointsMallGoodsSpecification record, PointsMallGoodsSpecificationExample example);

    void updateByPrimaryKeySelective(PointsMallGoodsSpecification record);

    Page<PointsMallGoodsSpecification> getListByPage(int pageNo, int pageSize, PointsMallGoodsSpecification goodsSpecification);

    Page<Map<String, Object>> getListByPageJoinPointsMallGoods(int pageNo, int pageSize, PointsMallGoodsSpecificationDto goodsSpecificationDto);

    int selectMaxSortNumberByPointsMallGoodsId(Integer goodsId);

    PointsMallGoodsSpecification selectByPointsMallGoodsIdAndName(Integer goodsId, String name);

    /**
     * 生成商品公共规格
     * @return
     */
    void insertPublicGoodsSpecification(int goodsId);

    void deleteByPointsMallGoodsId(int goodsId);
}