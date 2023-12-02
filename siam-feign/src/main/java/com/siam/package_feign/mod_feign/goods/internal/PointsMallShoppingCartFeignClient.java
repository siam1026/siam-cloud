package com.siam.package_feign.mod_feign.goods.internal;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_goods.entity.Shop;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "goods-siam", path = "/rest/pointsMallShoppingCartFeign")
public interface PointsMallShoppingCartFeignClient {

    @RequestMapping(value = "/insertSelective")
    int insertSelective(@RequestBody Shop record);

    /**
     * 根据id集合与用户id统计购物车数量
     * 用于校验id是否存在、以及该购物车数据是否属于当前登录用户
     * @param idList
     * @param memberId
     * @return
     */
    @RequestMapping(value = "/countByIdListAndMemberId")
    int countByIdListAndMemberId(@RequestParam("idList") List<Integer> idList, @RequestParam("memberId") Integer memberId);

    /**
     * 根据id集合进行批量删除
     * @param idList
     * @return
     */
    @RequestMapping(value = "/batchDeleteByIdList")
    BasicResult batchDeleteByIdList(@RequestParam("idList") List<Integer> idList);
}