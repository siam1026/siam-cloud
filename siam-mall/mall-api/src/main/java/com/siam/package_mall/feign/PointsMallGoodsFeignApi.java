package com.siam.package_mall.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_mall.entity.PointsMallGoods;
import com.siam.package_mall.model.example.PointsMallGoodsExample;
import com.siam.package_mall.model.param.PointsMallGoodsParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "mall-siam")
public interface PointsMallGoodsFeignApi {

    @PostMapping(value = "/api/pointsMallGoods/insertSelective")
    BasicResult insertSelective(@RequestBody PointsMallGoods record);

    @PostMapping(value = "/api/pointsMallGoods/selectByPrimaryKey")
    BasicResult<PointsMallGoods> selectByPrimaryKey(@RequestParam("id") Integer id);

    @PostMapping(value = "/api/pointsMallGoods/updateByPrimaryKeySelective")
    BasicResult updateByPrimaryKeySelective(@RequestBody PointsMallGoods record);

    /**
     * 商品月销量清零
     */
    @PostMapping(value = "/api/pointsMallGoods/monthlySalesReset")
    BasicResult monthlySalesReset();

    /**
     * 更新商品的销量
     * @param goodsId 商品id
     * @param num 销量变动值
     */
    @PostMapping(value = "/api/pointsMallGoods/updateSales")
    BasicResult updateSales(@RequestParam("goodsId") Integer goodsId, @RequestParam("num") Integer num);

    @PostMapping(value = "/api/pointsMallGoods/getListByPointsMallCouponsId")
    BasicResult<List<PointsMallGoods>> getListByPointsMallCouponsId(@RequestParam("couponsId") Integer couponsId);

    @PostMapping(value = "/api/pointsMallGoods/countByExample")
    BasicResult<Integer> countByExample(@RequestBody PointsMallGoodsParam param);
}