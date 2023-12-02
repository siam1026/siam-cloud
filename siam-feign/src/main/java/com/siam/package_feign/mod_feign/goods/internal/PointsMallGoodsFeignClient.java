package com.siam.package_feign.mod_feign.goods.internal;

import com.siam.package_goods.entity.internal.PointsMallGoods;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "goods-siam", path = "/rest/pointsMallGoodsFeign")
public interface PointsMallGoodsFeignClient {

    @RequestMapping(value = "/insertSelective")
    void insertSelective(@RequestBody PointsMallGoods record);

    @RequestMapping(value = "/selectByPrimaryKey")
    PointsMallGoods selectByPrimaryKey(@RequestParam("id") Integer id);

    @RequestMapping(value = "/updateByPrimaryKeySelective")
    void updateByPrimaryKeySelective(@RequestBody PointsMallGoods record);

    /**
     * 商品月销量清零
     */
    @RequestMapping(value = "/monthlySalesReset")
    void monthlySalesReset();

    /**
     * 更新商品的销量
     * @param goodsId 商品id
     * @param num 销量变动值
     */
    @RequestMapping(value = "/updateSales")
    void updateSales(@RequestParam("goodsId") Integer goodsId, @RequestParam("num") Integer num);
}