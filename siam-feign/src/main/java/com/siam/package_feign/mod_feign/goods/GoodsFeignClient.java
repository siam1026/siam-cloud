package com.siam.package_feign.mod_feign.goods;

import com.siam.package_goods.entity.Goods;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "goods-siam", path = "/rest/goodsFeign")
public interface GoodsFeignClient {

    @RequestMapping(value = "/insertSelective")
    void insertSelective(@RequestBody Goods record);

    @RequestMapping(value = "/selectByPrimaryKey")
    Goods selectByPrimaryKey(@RequestParam("id") Integer id);

    @RequestMapping(value = "/updateByPrimaryKeySelective")
    void updateByPrimaryKeySelective(@RequestBody Goods record);

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