package com.siam.package_goods.feign.fallback;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_goods.feign.GoodsSpecificationFeignApi;
import org.springframework.stereotype.Component;

@Component
public class GoodsSpecificationFallback implements GoodsSpecificationFeignApi {

    @Override
    public BasicResult selectByPrimaryKey(Integer id) {
        return null;
    }
}