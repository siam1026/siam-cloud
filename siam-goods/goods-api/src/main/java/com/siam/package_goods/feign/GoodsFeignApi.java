package com.siam.package_goods.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_goods.entity.Goods;
import com.siam.package_goods.model.param.GoodsParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "goods-siam")
public interface GoodsFeignApi {

    @PostMapping(value = "/api/goods/insertSelective")
    BasicResult insertSelective(@RequestBody Goods record);

    @PostMapping(value = "/api/goods/selectByPrimaryKey")
    BasicResult<Goods> selectByPrimaryKey(@RequestParam("id") Integer id);

    @PostMapping(value = "/api/goods/updateByPrimaryKeySelective")
    BasicResult updateByPrimaryKeySelective(@RequestBody Goods record);

    /**
     * 商品月销量清零
     */
    @PostMapping(value = "/api/goods/monthlySalesReset")
    BasicResult monthlySalesReset();

    /**
     * 更新商品的销量
     * @param goodsId 商品id
     * @param num 销量变动值
     */
    @PostMapping(value = "/api/goods/updateSales")
    BasicResult updateSales(@RequestParam("goodsId") Integer goodsId, @RequestParam("num") Integer num);

    @PostMapping(value = "/api/goods/getListByCouponsId")
    BasicResult<List<Goods>> getListByCouponsId(@RequestParam("couponsId") Integer couponsId);

    @PostMapping(value = "/api/goods/selectByExample")
    BasicResult<List<Goods>> selectByExample(@RequestBody GoodsParam param);
}