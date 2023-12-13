package com.siam.package_goods.feign.fallback;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_goods.entity.Goods;
import com.siam.package_goods.feign.GoodsFeignApi;
import com.siam.package_goods.model.example.GoodsExample;
import com.siam.package_goods.model.param.GoodsParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GoodsFallback implements GoodsFeignApi {

    @Override
    public BasicResult insertSelective(Goods record) {
        return null;
    }

    @Override
    public BasicResult<Goods> selectByPrimaryKey(Integer id) {
        return null;
    }

    @Override
    public BasicResult updateByPrimaryKeySelective(Goods record) {
        return null;
    }

    @Override
    public BasicResult monthlySalesReset() {
        return null;
    }

    @Override
    public BasicResult updateSales(Integer goodsId, Integer num) {
        return null;
    }

    @Override
    public BasicResult<List<Goods>> getListByCouponsId(Integer couponsId) {
        return null;
    }

    @Override
    public BasicResult<List<Goods>> selectByExample(GoodsParam param) {
        return null;
    }
}