package com.siam.package_goods.feign;

import com.siam.package_goods.entity.Goods;
import com.siam.package_goods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
@RequestMapping(value = "/rest/goodsFeign")
public class GoodsFeign {

    @Autowired
    private GoodsService goodsService;

    @RequestMapping(value = "/insertSelective")
    public void insertSelective(Goods record){
        goodsService.insertSelective(record);
    }

    @RequestMapping(value = "/selectByPrimaryKey")
    public Goods selectByPrimaryKey(@RequestParam("id") Integer id){
        return goodsService.selectByPrimaryKey(id);
    }

    @RequestMapping(value = "/updateByPrimaryKeySelective")
    public void updateByPrimaryKeySelective(@RequestBody Goods record){
        goodsService.updateByPrimaryKeySelective(record);
    }

    /**
     * 商品月销量清零
     */
    @RequestMapping(value = "/monthlySalesReset")
    void monthlySalesReset(){
        goodsService.monthlySalesReset();
    }

    /**
     * 更新商品的销量
     * @param goodsId 商品id
     * @param num 销量变动值
     */
    @RequestMapping(value = "/updateSales")
    void updateSales(@RequestParam("goodsId") Integer goodsId, @RequestParam("num") Integer num){
        goodsService.updateSales(goodsId, num);
    }
}