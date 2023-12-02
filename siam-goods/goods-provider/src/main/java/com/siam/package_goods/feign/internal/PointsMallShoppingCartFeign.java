package com.siam.package_goods.feign.internal;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_goods.entity.internal.PointsMallShoppingCart;
import com.siam.package_goods.service.internal.PointsMallShoppingCartService;
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
@RequestMapping(value = "/rest/pointsMallShoppingCartFeign")
public class PointsMallShoppingCartFeign {

    @Autowired
    private PointsMallShoppingCartService pointsMallShoppingCartService;

    @RequestMapping(value = "/insertSelective")
    void insertSelective(@RequestBody PointsMallShoppingCart record){
        pointsMallShoppingCartService.insertSelective(record);
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
        return pointsMallShoppingCartService.countByIdListAndMemberId(idList, memberId);
    }

    /**
     * 根据id集合进行批量删除
     * @param idList
     * @return
     */
    @RequestMapping(value = "/batchDeleteByIdList")
    BasicResult batchDeleteByIdList(@RequestParam("idList") List<Integer> idList){
        return BasicResult.success(pointsMallShoppingCartService.batchDeleteByIdList(idList));
    }
}