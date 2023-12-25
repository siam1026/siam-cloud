package com.siam.package_mall.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_mall.entity.PointsMallShoppingCart;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@FeignClient(value = "mall-siam")
public interface PointsMallShoppingCartFeignApi {

    @PostMapping(value = "/api/pointsMallShoppingCart/insertSelective")
    BasicResult insertSelective(@RequestBody PointsMallShoppingCart record);

    /**
     * 根据id集合与用户id统计购物车数量
     * 用于校验id是否存在、以及该购物车数据是否属于当前登录用户
     * @param idList
     * @param memberId
     * @return
     */
    @PostMapping(value = "/api/pointsMallShoppingCart/countByIdListAndMemberId")
    BasicResult<Integer> countByIdListAndMemberId(@RequestParam("idList") List<Integer> idList, @RequestParam("memberId") Integer memberId);

    /**
     * 根据id集合进行批量删除
     * @param idList
     * @return
     */
    @PostMapping(value = "/api/pointsMallShoppingCart/batchDeleteByIdList")
    BasicResult batchDeleteByIdList(@RequestParam("idList") List<Integer> idList);

    /**
     * 查询加购商品数量
     */
    @PostMapping(value = "/api/pointsMallShoppingCart/selectCountGoodsNumber")
    BasicResult<Integer> selectCountGoodsNumber(@RequestParam(value = "shopId", required = false) Integer shopId, @RequestParam("startTime") Date startTime, @RequestParam("endTime") Date endTime);
}