package com.siam.package_mall.service_impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_mall.mapper.PointsMallShoppingCartMapper;
import com.siam.package_mall.service.PointsMallShoppingCartService;
import com.siam.package_mall.entity.PointsMallShoppingCart;
import com.siam.package_mall.model.example.PointsMallShoppingCartExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class PointsMallShoppingCartServiceImpl implements PointsMallShoppingCartService {
    @Autowired
    private PointsMallShoppingCartMapper shoppingCartMapper;

    public int countByExample(PointsMallShoppingCartExample example){
        return shoppingCartMapper.countByExample(example);
    }

    public void deleteByPrimaryKey(Integer id){
        shoppingCartMapper.deleteByPrimaryKey(id);
    }

    public void insertSelective(PointsMallShoppingCart record){
        shoppingCartMapper.insertSelective(record);
    }

    public List<PointsMallShoppingCart> selectByExample(PointsMallShoppingCartExample example){
        return shoppingCartMapper.selectByExample(example);
    }

    public PointsMallShoppingCart selectByPrimaryKey(Integer id){
        return shoppingCartMapper.selectByPrimaryKey(id);
    }

    public void updateByExampleSelective(PointsMallShoppingCart record, PointsMallShoppingCartExample example){
        shoppingCartMapper.updateByExampleSelective(record, example);
    }

    public void updateByPrimaryKeySelective(PointsMallShoppingCart record){
        shoppingCartMapper.updateByPrimaryKeySelective(record);
    }

    public Page getListByPage(int pageNo, int pageSize, PointsMallShoppingCart shoppingCart) {
        Page<Map<String, Object>> page = shoppingCartMapper.getListByPage(new Page(pageNo, pageSize), shoppingCart);
        return page;
    }

    public Page<Map<String, Object>> getListByPageJoinPointsMallGoods(int pageNo, int pageSize, PointsMallShoppingCart shoppingCart) {
        Page<Map<String, Object>> page = shoppingCartMapper.getListByPageJoinPointsMallGoods(new Page(pageNo, pageSize), shoppingCart);
        return page;
    }

    @Override
    public int countByIdListAndMemberId(List<Integer> idList, Integer memberId) {
        return shoppingCartMapper.countByIdListAndMemberId(idList, memberId);
    }

    @Override
    @Transactional
    public int batchDeleteByIdList(List<Integer> idList) {
//        if(idList.size() == 1 && idList.get(0) == 124){
//            throw new RuntimeException("分布式事务测试，商品服务-购物车扣减异常");
//        }
        return shoppingCartMapper.batchDeleteByIdList(idList);
    }

    @Override
    public void updateIsGoodsExistsTo0ByPointsMallGoodsId(int goodsId) {
        shoppingCartMapper.updateIsGoodsExistsTo0ByPointsMallGoodsId(goodsId);
    }

    @Override
    public int selectCountGoodsNumber(Integer shopId, Date startTime, Date endTime) {
        return shoppingCartMapper.selectCountGoodsNumber(shopId, startTime, endTime);
    }
}