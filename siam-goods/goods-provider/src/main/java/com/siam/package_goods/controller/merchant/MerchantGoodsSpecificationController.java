package com.siam.package_goods.controller.merchant;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.annoation.MerchantPermission;
import com.siam.package_common.constant.BasicResultCode;
import com.siam.package_common.entity.BasicData;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_goods.entity.GoodsSpecification;
import com.siam.package_goods.model.example.GoodsSpecificationExample;
import com.siam.package_goods.service.GoodsSpecificationService;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rest/merchant/goodsSpecification")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "商家端商品规格模块相关接口", description = "MerchantGoodsSpecificationController")
public class MerchantGoodsSpecificationController {
    @Autowired
    private GoodsSpecificationService goodsSpecificationService;

    @Operation(summary = "商品规格列表")
    @PostMapping(value = "/list")
    public BasicResult list(@RequestBody @Validated(value = {}) GoodsSpecification goodsSpecification){
        BasicData basicResult = new BasicData();

        Page page = goodsSpecificationService.getListByPage(goodsSpecification.getPageNo(), goodsSpecification.getPageSize(), goodsSpecification);

        return BasicResult.success(page);
    }

    @MerchantPermission
    @Operation(summary = "修改商品规格")
    @PostMapping(value = "/update")
    public BasicResult update(@RequestBody @Validated(value = {}) GoodsSpecification goodsSpecification){
        BasicResult basicResult = new BasicResult();

        GoodsSpecification dbSpecification = goodsSpecificationService.selectByPrimaryKey(goodsSpecification.getId());
        if(dbSpecification == null){
            basicResult.setSuccess(false);
            basicResult.setCode(BasicResultCode.ERR);
            basicResult.setMessage("该奶茶口味类别不存在，修改失败");
            return basicResult;
        }

        //判断商品规格选项是否重复
        GoodsSpecificationExample example = new GoodsSpecificationExample();
        example.createCriteria().andGoodsIdEqualTo(dbSpecification.getGoodsId()).andNameEqualTo(goodsSpecification.getName()).andIdNotEqualTo(goodsSpecification.getId());
        int result = goodsSpecificationService.countByExample(example);
        if(result > 0){
            basicResult.setSuccess(false);
            basicResult.setCode(BasicResultCode.ERR);
            basicResult.setMessage("奶茶口味类别重复，修改失败");
            return basicResult;
        }

        //修改规格记录
        goodsSpecificationService.updateByPrimaryKeySelective(goodsSpecification);

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("修改成功");
        return basicResult;
    }

    /*@Operation(summary = "新增商品规格")@PostMapping(value = "/insert")
    public BasicResult insert(Specification specification){
        BasicResult basicResult = new BasicResult();

        specificationService.insertSelective(specification);

        basicResult.setSuccess(true);
        basicResult.setCode(BaseCode.SUCCESS);
        basicResult.setMessage("新增成功");
        return basicResult;
    }*/

    /*@Operation(summary = "删除商品规格(含批量操作)")@PostMapping(value = "/delete")
    public BasicResult delete(@RequestParam(value = "ids", required = true) List<String> ids){
        BasicResult basicResult = new BasicResult();

        for(String id : ids){
            specificationService.deleteByPrimaryKey(Integer.valueOf(id));
        }

        basicResult.setSuccess(true);
        basicResult.setCode(BaseCode.SUCCESS);
        basicResult.setMessage("删除成功");
        return basicResult;
    }*/
}