package com.siam.package_goods.feign.internal;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_goods.entity.internal.PointsMallGoods;
import com.siam.package_goods.mapper.internal.PointsMallGoodsMapper;
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
public class PointsMallGoodsFeignProvider implements PointsMallGoodsFeignApi {

    @Autowired
    private PointsMallGoodsService pointsMallGoodsService;

    @Autowired
    private PointsMallGoodsMapper pointsMallGoodsMapper;

    public BasicResult insertSelective(PointsMallGoods record){
        pointsMallGoodsService.insertSelective(record);
        return BasicResult.success();
    }

    public BasicResult selectByPrimaryKey(@RequestParam("id") Integer id){
        return BasicResult.success(pointsMallGoodsService.selectByPrimaryKey(id));
    }

    public BasicResult updateByPrimaryKeySelective(PointsMallGoods record){
        pointsMallGoodsService.updateByPrimaryKeySelective(record);
        return BasicResult.success();
    }

    /**
     * 商品月销量清零
     */
    public BasicResult monthlySalesReset(){
        pointsMallGoodsService.monthlySalesReset();
        return BasicResult.success();
    }

    /**
     * 更新商品的销量
     * @param goodsId 商品id
     * @param num 销量变动值
     */
    public BasicResult updateSales(@RequestParam("goodsId") Integer goodsId, @RequestParam("num") Integer num){
        pointsMallGoodsService.updateSales(goodsId, num);
        return BasicResult.success();
    }

    @Override
    public BasicResult getListByPointsMallCouponsId(Integer couponsId) {
        return BasicResult.success(pointsMallGoodsMapper.getListByPointsMallCouponsId(couponsId));
    }
}