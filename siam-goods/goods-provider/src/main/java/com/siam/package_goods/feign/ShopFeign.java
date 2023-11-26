package com.siam.package_goods.feign;

import com.siam.package_goods.entity.Shop;
import com.siam.package_goods.model.example.ShopExample;
import com.siam.package_goods.service.ShopService;
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
@RequestMapping(value = "/rest/shopFeign")
public class ShopFeign {

    @Autowired
    private ShopService shopService;

    @RequestMapping(value = "/insertSelective")
    public int insertSelective(@RequestBody Shop record){
        shopService.insertSelective(record);
        return record.getId();
    }

    @RequestMapping(value = "/selectByPrimaryKey")
    Shop selectByPrimaryKey(@RequestParam("id") Integer id){
        return shopService.selectByPrimaryKey(id);
    }

    @RequestMapping(value = "/selectByExample")
    List<Shop> selectByExample(@RequestBody ShopExample example){
        return shopService.selectByExample(example);
    }
}