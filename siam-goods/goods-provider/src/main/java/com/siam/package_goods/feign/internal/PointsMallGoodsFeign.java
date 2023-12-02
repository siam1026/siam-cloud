package com.siam.package_goods.feign.internal;

import com.siam.package_goods.entity.internal.PointsMallGoods;
import com.siam.package_goods.service.internal.PointsMallGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
@RequestMapping(value = "/rest/pointsMallGoodsFeign")
public class PointsMallGoodsFeign {

    @Autowired
    private PointsMallGoodsService pointsMallGoodsService;

    @RequestMapping(value = "/insertSelective")
    void insertSelective(@RequestBody PointsMallGoods record){
        pointsMallGoodsService.insertSelective(record);
    }

    @RequestMapping(value = "/selectByPrimaryKey")
    PointsMallGoods selectByPrimaryKey(@RequestParam("id") Integer id){
        return pointsMallGoodsService.selectByPrimaryKey(id);
    }

    @RequestMapping(value = "/updateByPrimaryKeySelective")
    void updateByPrimaryKeySelective(@RequestBody PointsMallGoods record){
        pointsMallGoodsService.updateByPrimaryKeySelective(record);
    }

    /**
     * 商品月销量清零
     */
    @RequestMapping(value = "/monthlySalesReset")
    void monthlySalesReset(){
        pointsMallGoodsService.monthlySalesReset();
    }

    /**
     * 更新商品的销量
     * @param goodsId 商品id
     * @param num 销量变动值
     */
    @RequestMapping(value = "/updateSales")
    void updateSales(@RequestParam("goodsId") Integer goodsId, @RequestParam("num") Integer num){
        pointsMallGoodsService.updateSales(goodsId, num);
    }
}