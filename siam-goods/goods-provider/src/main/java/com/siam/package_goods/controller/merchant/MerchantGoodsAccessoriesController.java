package com.siam.package_goods.controller.merchant;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.entity.BasicData;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_common.constant.BasicResultCode;
import com.siam.package_goods.entity.GoodsAccessories;
import com.siam.package_goods.service.GoodsAccessoriesService;
import com.siam.package_goods.model.example.GoodsSpecificationOptionExample;
import com.siam.package_goods.service.GoodsSpecificationOptionService;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping(value = "/rest/merchant/goodsAccessories")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "商家端商品辅料模块相关接口", description = "MerchantGoodsAccessoriesController")
public class MerchantGoodsAccessoriesController {

    @Autowired
    private GoodsAccessoriesService goodsAccessoriesService;

    @Autowired
    private GoodsSpecificationOptionService goodsSpecificationOptionService;

    @Operation(summary = "商品辅料列表")@PostMapping(value = "/list")
    public BasicResult list(@RequestBody @Validated(value = {}) GoodsAccessories goodsAccessories){
        BasicData basicResult = new BasicData();
        Page<GoodsAccessories> page = goodsAccessoriesService.getListByPage(goodsAccessories.getPageNo(), goodsAccessories.getPageSize(), goodsAccessories);

        return BasicResult.success(page);
    }

    @Operation(summary = "新增商品辅料")@PostMapping(value = "/insert")
    public BasicResult insert(@RequestBody @Validated(value = {}) GoodsAccessories goodsAccessories){
        BasicResult basicResult = new BasicResult();

        goodsAccessories.setCreateTime(new Date());
        goodsAccessories.setUpdateTime(new Date());
        goodsAccessoriesService.insertSelective(goodsAccessories);

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("创建成功");
        return basicResult;
    }


    @Operation(summary = "修改商品辅料")@PutMapping(value = "/update")
    public BasicResult update(@RequestBody @Validated(value = {}) GoodsAccessories goodsAccessories){
        BasicResult basicResult = new BasicResult();

        goodsAccessories.setUpdateTime(new Date());
        goodsAccessoriesService.updateByPrimaryKeySelective(goodsAccessories);

        //级联修改商品规格选项的价格、库存
        GoodsAccessories dbGoodsAccessories = goodsAccessoriesService.selectByPrimaryKey(goodsAccessories.getId());
        goodsSpecificationOptionService.updateByGoodsAccessories(dbGoodsAccessories);

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("修改成功");
        return basicResult;
    }


    @Operation(summary = "删除商品辅料")@DeleteMapping(value = "/delete")
    public BasicResult delete(@RequestBody @Validated(value = {}) GoodsAccessories param){
        BasicResult basicResult = new BasicResult();

        GoodsAccessories dbGoodsAccessories = goodsAccessoriesService.selectByPrimaryKey(param.getId());
        if(dbGoodsAccessories == null){
            basicResult.setSuccess(false);
            basicResult.setCode(BasicResultCode.ERR);
            basicResult.setMessage("该商品辅料不存在，删除失败");
            return basicResult;
        }

        //暂时不让他删除，后续可能会采取级联删除商品规格选项
        GoodsSpecificationOptionExample example = new GoodsSpecificationOptionExample();
        example.createCriteria().andNameEqualTo(dbGoodsAccessories.getName());
        int result = goodsSpecificationOptionService.countByExample(example);
        if(result > 0){
            basicResult.setSuccess(false);
            basicResult.setCode(BasicResultCode.ERR);
            basicResult.setMessage("该商品辅料关联了商品规格选项，不允许删除");
            return basicResult;
        }

        //删除商品辅料
        goodsAccessoriesService.deleteByPrimaryKey(param.getId());

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("删除成功");
        return basicResult;
    }
}