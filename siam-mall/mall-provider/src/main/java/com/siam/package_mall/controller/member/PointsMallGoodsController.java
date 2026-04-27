package com.siam.package_mall.controller.member;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.constant.BasicResultCode;
import com.siam.package_common.constant.Quantity;
import com.siam.package_common.entity.BasicData;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_common.exception.StoneCustomerException;

import com.siam.package_common.util.BeanUtils;
import com.siam.package_common.util.DateUtilsPlus;
import com.siam.package_mall.entity.PointsMallGoods;
import com.siam.package_mall.model.dto.PointsMallGoodsMenuDto;
import com.siam.package_mall.service.PointsMallGoodsService;
import com.siam.package_user.model.param.AdminParam;
import com.siam.package_util.feign.SettingFeignApi;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/rest/pointsMall/goods")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "商品模块相关接口", description = "PointsMallGoodsController")
public class PointsMallGoodsController {
    @Autowired
    private PointsMallGoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

//    @Autowired
//    private CommonService commonService;

    @Autowired
    private SettingFeignApi settingFeignApi;

    /*@Operation(summary = "商品列表")@PostMapping(value = "/list")
    public BasicResult list(int pageNo, int pageSize, PointsMallGoods goods){
        BasicData basicResult = new BasicData();

        Page<PointsMallGoods> page = goodsService.getListByPage(param.getPageNo(), param.getPageSize(), goods);

        return BasicResult.success(page);
    }*/

    /**
     * 查看商品详情
     *
     * @return
     * @author 暹罗
     */
    @PostMapping(value = "/selectById")
    public BasicResult selectById(@RequestBody @Validated(value = {}) PointsMallGoods param){
        BasicData basicResult = new BasicData();

        PointsMallGoods dbPointsMallGoods = goodsService.selectByPrimaryKey(param.getId());
        if(dbPointsMallGoods == null) throw new StoneCustomerException("该商品不存在");

        Map<String, Object> resultMap = BeanUtils.beanToMap(dbPointsMallGoods);

        //查询近一月销量
        long latelyMonthlySales = goodsService.selectLatelyMonthlySalesById(dbPointsMallGoods.getId());
        resultMap.put("latelyMonthlySales", latelyMonthlySales);

        basicResult.setData(resultMap);
        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("查询成功");
        return basicResult;
    }

    /**
     * 指定菜单下的商品列表
     *
     * @return
     * @author 暹罗
     */
    @PostMapping(value = "/listByMenuId")
    public BasicResult listByPointsMallMenuId(@RequestBody @Validated(value = {}) PointsMallGoodsMenuDto goodsPointsMallMenuDto){
        BasicData basicResult = new BasicData();

        if(goodsPointsMallMenuDto.getMenuId() == null){
            throw new StoneCustomerException("菜单id不能为空");
        }

        //按照近一月销量进行排序，如4-11~5-11、4-12~5-12区间
        //计算结束时间
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(new Date());
        endCalendar.set(Calendar.HOUR_OF_DAY, 23);
        endCalendar.set(Calendar.MINUTE, 59);
        endCalendar.set(Calendar.SECOND, 59);
        endCalendar.set(Calendar.MILLISECOND, 999);
        Date endTime = endCalendar.getTime();
        //计算开始时间
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(DateUtilsPlus.subtractDays(endTime, 30));
        startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);
        Date startTime = startCalendar.getTime();

        goodsPointsMallMenuDto.setStartTime(startTime);
        goodsPointsMallMenuDto.setEndTime(endTime);

        //查询商品 2=已上架 4=售罄
        goodsPointsMallMenuDto.setGoodsStatusIn2And4(true);
        Page<Map<String, Object>> page = goodsService.getListByPageJoinPointsMallMenuPointsMallOrderByLatelyMonthlySales(goodsPointsMallMenuDto.getPageNo(), goodsPointsMallMenuDto.getPageSize(), goodsPointsMallMenuDto);

        return BasicResult.success(page);
    }


    /*@Operation(summary = "查询商品的规格组合信息")@PostMapping(value = "/selectSpecificationById")
    public BasicResult selectSpecificationById(PointsMallGoodsSpecificationDto goodsSpecificationDto){
        BasicData basicResult = new BasicData();

        Page<Map<String, Object>> page = goodsService.getListByPageJoinSpecification(-1, 10, goodsSpecificationDto);

        List<Map<String, Object>> list = page.getRecords();

        Map<String, List> specificationMap = new LinkedHashMap<>();

        list.forEach(map -> {
            String name = (String) map.get("specificationName");
            if(specificationMap.containsKey(name)){
                specificationMap.get(name).add(map);
            }else{
                specificationMap.put(name, new ArrayList());
            }
        });

        basicResult.setData(specificationMap);
        basicResult.setSuccess(true);
        basicResult.setCode(BaseCode.SUCCESS);
        basicResult.setMessage("查询成功");
        return basicResult;
    }*/

    /*@Operation(summary = "本月上新商品列表")
    @PostMapping(value = "/weekNewGoodsList")
    public BasicResult weekNewPointsMallGoodsList(){
        BasicData basicResult = new BasicData();

        //查询最新上架的一件商品
        List<PointsMallGoods> goodsList = goodsService.getListByLastestShelvesTop1();

        basicResult.setData(goodsList);
        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("查询成功");
        return basicResult;
    }*/

    /*@Operation(summary = "好友推荐商品列表")
    @PostMapping(value = "/recommendGoodsList")
    public BasicResult recommendPointsMallGoodsList(){
        BasicData basicResult = new BasicData();

        //查询近一月销量最高的前三件商品，如4-11~5-11、4-12~5-12区间
        //计算结束时间
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(new Date());
        endCalendar.set(Calendar.HOUR_OF_DAY, 23);
        endCalendar.set(Calendar.MINUTE, 59);
        endCalendar.set(Calendar.SECOND, 59);
        endCalendar.set(Calendar.MILLISECOND, 999);
        Date endTime = endCalendar.getTime();
        //计算开始时间
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(DateUtils.subtractDays(endTime, 30));
        startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);
        Date startTime = startCalendar.getTime();
        List<Map<String, Object>> goodsList = goodsService.getListByLatelyMonthlySalesTop3(startTime, endTime);

        basicResult.setData(goodsList);
        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("查询成功");
        return basicResult;
    }*/

    @Operation(summary = "猜你喜欢商品列表")
    @PostMapping(value = "/guessLikeGoodsList")
    public BasicResult guessLikePointsMallGoodsList(@RequestBody @Validated(value = {}) AdminParam param){
        BasicData basicResult = new BasicData();

        //查询近一月销量最高的前三件商品，如4-11~5-11、4-12~5-12区间
        //计算结束时间
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(new Date());
        endCalendar.set(Calendar.HOUR_OF_DAY, 23);
        endCalendar.set(Calendar.MINUTE, 59);
        endCalendar.set(Calendar.SECOND, 59);
        endCalendar.set(Calendar.MILLISECOND, 999);
        Date endTime = endCalendar.getTime();
        //计算开始时间
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(DateUtilsPlus.subtractDays(endTime, 30));
        startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);
        Date startTime = startCalendar.getTime();

        List<Map<String, Object>> goodsList = goodsService.getListByLatelyMonthlySalesTopNumber(startTime, endTime, Quantity.INT_6);

        basicResult.setData(goodsList);
        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("查询成功");
        return basicResult;
    }

    /*@Operation(summary = "首页商品推荐列表")
    @PostMapping(value = "/homePage/recommendGoodsList")
    public BasicResult recommendPointsMallGoodsListOfHomePage(){
        BasicData basicResult = new BasicData();

        //TODO-按照浏览量由高到低查询出前6件商品(暂存)
        //查询近一月销量最高的前6件商品，如4-11~5-11、4-12~5-12区间

        //计算结束时间
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(new Date());
        endCalendar.set(Calendar.HOUR_OF_DAY, 23);
        endCalendar.set(Calendar.MINUTE, 59);
        endCalendar.set(Calendar.SECOND, 59);
        endCalendar.set(Calendar.MILLISECOND, 999);
        Date endTime = endCalendar.getTime();
        //计算开始时间
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(DateUtils.subtractDays(endTime, 30));
        startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);
        Date startTime = startCalendar.getTime();
        List<Map<String, Object>> goodsList = goodsService.getListByLatelyMonthlySalesTopNumber(startTime, endTime, Quantity.INT_6);

        basicResult.setData(goodsList);
        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("查询成功");
        return basicResult;
    }*/

    @Operation(summary = "推荐展示的商品列表")
    @PostMapping(value = "/recommendGoodsList")
    public BasicResult recommendGoodsList(@RequestBody @Validated(value = {}) PointsMallGoods goods){
        BasicData basicResult = new BasicData();
        
        Page<Map<String, Object>> page = goodsService.recommendGoodsList(goods.getPageNo(), goods.getPageSize(), goods);

        return BasicResult.success(page);
    }
}