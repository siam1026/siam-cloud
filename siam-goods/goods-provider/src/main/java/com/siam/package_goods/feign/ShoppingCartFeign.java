package com.siam.package_goods.feign;

import com.siam.package_goods.entity.ShoppingCart;
import com.siam.package_goods.service.ShoppingCartService;
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
@RequestMapping(value = "/rest/shoppingCartFeign")
public class ShoppingCartFeign {

    @Autowired
    private ShoppingCartService shoppingCartService;


    @RequestMapping(value = "/insertSelective")
    void insertSelective(@RequestBody ShoppingCart record){
        shoppingCartService.insertSelective(record);
    }

    /**
     * 根据id集合与用户id统计购物车数量
     * 用于校验id是否存在、以及该购物车数据是否属于当前登录用户
     * @param idList
     * @param memberId
     * @return
     */
    @RequestMapping(value = "/countByIdListAndMemberId")
    int countByIdListAndMemberId(@RequestParam("idList") List<Integer> idList, @RequestParam("memberId") Integer memberId){
        return shoppingCartService.countByIdListAndMemberId(idList, memberId);
    }

    /**
     * 根据id集合进行批量删除
     * @param idList
     * @return
     */
    @RequestMapping(value = "/batchDeleteByIdList")
    int batchDeleteByIdList(@RequestParam("idList") List<Integer> idList){
        return shoppingCartService.batchDeleteByIdList(idList);
    }
}