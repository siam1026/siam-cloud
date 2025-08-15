package com.siam.package_goods.feign;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_goods.entity.Goods;
import com.siam.package_goods.mapper.GoodsMapper;
import com.siam.package_goods.model.example.GoodsExample;
import com.siam.package_goods.model.param.GoodsParam;
import com.siam.package_goods.service.GoodsService;
import com.siam.package_user.model.example.AdminExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
public class GoodsFeignProvider implements GoodsFeignApi {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsMapper goodsMapper;

    public BasicResult insertSelective(Goods record){
        goodsService.insertSelective(record);
        return BasicResult.success();
    }

    public BasicResult selectByPrimaryKey(@RequestParam("id") Integer id){
        return BasicResult.success(goodsService.selectByPrimaryKey(id));
    }

    public BasicResult updateByPrimaryKeySelective(Goods record){
        goodsService.updateByPrimaryKeySelective(record);
        return BasicResult.success();
    }

    /**
     * 商品月销量清零
     */
    public BasicResult monthlySalesReset(){
        goodsService.monthlySalesReset();
        return BasicResult.success();
    }

    /**
     * 更新商品的销量
     * @param goodsId 商品id
     * @param num 销量变动值
     */
    public BasicResult updateSales(@RequestParam("goodsId") Integer goodsId, @RequestParam("num") Integer num){
        goodsService.updateSales(goodsId, num);
        return BasicResult.success();
    }

    @Override
    public BasicResult getListByCouponsId(Integer couponsId) {
        return BasicResult.success(goodsMapper.getListByCouponsId(couponsId));
    }

    @Override
    public BasicResult<List<Goods>> selectByExample(GoodsParam param) {
        GoodsExample example = new GoodsExample();
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        if(param.getShopId() != null){
            wrapper.eq(Goods::getShopId, param.getShopId());
        }
        if(param.getKeywords() != null){
            wrapper.like(Goods::getName, "%"+ param.getKeywords() +"%");
        }
        if(param.getFilterList1() != null){
            wrapper.notIn(Goods::getShopId, param.getFilterList1());
        }

        return BasicResult.success(goodsMapper.selectList(wrapper));
    }
}