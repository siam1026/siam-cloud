package com.siam.package_feign.mod_feign.fallback;

import com.siam.package_feign.mod_feign.goods.GoodsSpecificationFeignClient;
import com.siam.package_goods.entity.GoodsSpecification;
import org.springframework.stereotype.Component;

@Component
public class GoodsSpecificationFallback implements GoodsSpecificationFeignClient {

    @Override
    public GoodsSpecification selectByPrimaryKey(Integer id) {
        return null;
    }
}