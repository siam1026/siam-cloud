package com.siam.package_goods.feign;

import com.siam.package_common.entity.BasicResult;
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
public class ShoppingCartFeignProvider implements ShoppingCartFeignApi {

    @Autowired
    private ShoppingCartService shoppingCartService;


    public BasicResult insertSelective(ShoppingCart record){
        shoppingCartService.insertSelective(record);
        return BasicResult.success();
    }

    /**
     * 根据id集合与用户id统计购物车数量
     * 用于校验id是否存在、以及该购物车数据是否属于当前登录用户
     * @param idList
     * @param memberId
     * @return
     */
    public BasicResult countByIdListAndMemberId(@RequestParam("idList") List<Integer> idList, @RequestParam("memberId") Integer memberId){
        return BasicResult.success(shoppingCartService.countByIdListAndMemberId(idList, memberId));
    }

    /**
     * 根据id集合进行批量删除
     * @param idList
     * @return
     */
    public BasicResult batchDeleteByIdList(@RequestParam("idList") List<Integer> idList){
        return BasicResult.success(shoppingCartService.batchDeleteByIdList(idList));
    }
}