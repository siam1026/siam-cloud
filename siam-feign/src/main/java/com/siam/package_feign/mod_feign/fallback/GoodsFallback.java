package com.siam.package_feign.mod_feign.fallback;

import com.siam.package_feign.mod_feign.goods.GoodsFeignClient;
import com.siam.package_goods.entity.Goods;
import org.springframework.stereotype.Component;

@Component
public class GoodsFallback implements GoodsFeignClient {

    @Override
    public void insertSelective(Goods record) {

    }

    @Override
    public Goods selectByPrimaryKey(Integer id) {
        return null;
    }

    @Override
    public void updateByPrimaryKeySelective(Goods record) {

    }

    @Override
    public void monthlySalesReset() {

    }

    @Override
    public void updateSales(Integer goodsId, Integer num) {

    }
}