package com.siam.package_merchant.feign;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.constant.Quantity;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_merchant.entity.Shop;
import com.siam.package_merchant.mapper.ShopMapper;
import com.siam.package_merchant.model.example.ShopChangeRecordExample;
import com.siam.package_merchant.model.example.ShopExample;
import com.siam.package_merchant.model.param.ShopParam;
import com.siam.package_merchant.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
public class ShopFeignProvider implements ShopFeignApi {

    @Autowired
    private ShopService shopService;

    @Autowired
    private ShopMapper shopMapper;

    public BasicResult insertSelective(Shop record){
        shopService.insertSelective(record);
        return BasicResult.success(record.getId());
    }

    public BasicResult selectByPrimaryKey(@RequestParam("id") Integer id){
        return BasicResult.success(shopService.selectByPrimaryKey(id));
    }

    public BasicResult selectByExample(ShopParam param) {
        LambdaQueryWrapper<Shop> wrapper = new LambdaQueryWrapper();
        if(param.getShopIdList() != null){
            wrapper.in(Shop::getId, param.getShopIdList());
        }
        return BasicResult.success(shopService.list(wrapper));
    }

    @Override
    public BasicResult getListByCouponsId(Integer couponsId) {
        return BasicResult.success(shopMapper.getListByCouponsId(couponsId));
    }

    @Override
    public BasicResult<Page<Shop>> selectByDistance(int pageNo, int pageSize, BigDecimal positionLongitude, BigDecimal positionLatitude, BigDecimal deliveryDistanceLimit) {
        return BasicResult.success(shopService.selectByDistance(pageNo, pageSize, positionLongitude, positionLatitude, deliveryDistanceLimit));
    }

    @Override
    public BasicResult<Integer> countByExample(ShopParam param) {
        ShopExample example = new ShopExample();
        LambdaQueryWrapper<Shop> wrapper = new LambdaQueryWrapper();
        if(param.getAuditStatus() != null){
            wrapper.eq(Shop::getAuditStatus, param.getAuditStatus());
        }
        if(param.getStatus() != null){
            wrapper.eq(Shop::getStatus, param.getStatus());
        }
        return BasicResult.success(shopService.count(wrapper));
    }
}