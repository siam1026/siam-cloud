package com.siam.package_goods.controller.merchant;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.entity.BasicData;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_common.constant.BasicResultCode;
import com.siam.package_common.util.GsonUtils;
import com.siam.package_common.util.StringUtils;
import com.siam.package_goods.entity.Goods;
import com.siam.package_goods.service.GoodsService;
import com.siam.package_goods.entity.GoodsRawmaterialRelation;
import com.siam.package_goods.model.example.GoodsRawmaterialRelationExample;
import com.siam.package_goods.service.GoodsRawmaterialRelationService;
import com.siam.package_goods.entity.Rawmaterial;
import com.siam.package_goods.service.RawmaterialService;
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

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/rest/merchant/goodsRawmaterialRelation")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "商家端原料配比模块相关接口", description = "MerchantGoodsRawmaterialRelationController")
public class MerchantGoodsRawmaterialRelationController {

    @Autowired
    private GoodsRawmaterialRelationService goodsRawmaterialRelationService;

    @Autowired
    private RawmaterialService rawmaterialService;

    @Autowired
    private GoodsService goodsService;

    @Operation(summary = "原料配比列表")@PostMapping(value = "/list")
    public BasicResult list(@RequestBody @Validated(value = {}) GoodsRawmaterialRelation goodsRawmaterialRelation){
        BasicData basicResult = new BasicData();
        Page<GoodsRawmaterialRelation> page = goodsRawmaterialRelationService.getListByPage(goodsRawmaterialRelation.getPageNo(), goodsRawmaterialRelation.getPageSize(), goodsRawmaterialRelation);

        return BasicResult.success(page);
    }

    @Operation(summary = "设置原料配比")@PostMapping(value = "/insert")
    public BasicResult insert(@RequestBody @Validated(value = {}) GoodsRawmaterialRelation param){
        BasicResult basicResult = new BasicResult();

        if(StringUtils.isEmpty(param.getGoodsIdListStr())){
            basicResult.setSuccess(false);
            basicResult.setCode(BasicResultCode.ERR);
            basicResult.setMessage("请选择要设置原料配比的商品信息");
            return basicResult;
        }
        if(StringUtils.isEmpty(param.getRelationListStr())){
            basicResult.setSuccess(false);
            basicResult.setCode(BasicResultCode.ERR);
            basicResult.setMessage("请选择商品关联的原料");
            return basicResult;
        }
        List<Integer> goodsIdList = GsonUtils.toList(param.getGoodsIdListStr(), Integer.class);
        List<GoodsRawmaterialRelation> relationList = GsonUtils.toList(param.getRelationListStr(), GoodsRawmaterialRelation.class);

        //双层循环插入关联信息
        for (Integer goodsId : goodsIdList) {
            //校验商品信息是否存在，后续优化可以在循环外面批量校验
            Goods dbGoods = goodsService.selectByPrimaryKey(goodsId);
            if(dbGoods == null){
                basicResult.setSuccess(false);
                basicResult.setCode(BasicResultCode.ERR);
                basicResult.setMessage("要设置原料配比的商品信息不存在");
                return basicResult;
            }
            for (GoodsRawmaterialRelation goodsRawmaterialRelation : relationList) {
                int rawmaterialId = goodsRawmaterialRelation.getRawmaterialId();
                BigDecimal consumedQuantity = goodsRawmaterialRelation.getConsumedQuantity();
                //校验原料信息是否存在，后续优化可以在循环外面批量校验
                Rawmaterial dbRawmaterial = rawmaterialService.selectByPrimaryKey(rawmaterialId);
                if(dbRawmaterial == null){
                    basicResult.setSuccess(false);
                    basicResult.setCode(BasicResultCode.ERR);
                    basicResult.setMessage("关联的原料信息不存在");
                    return basicResult;
                }
                //判断该原料配比关系是否已存在，存在则做更新操作
                GoodsRawmaterialRelationExample example = new GoodsRawmaterialRelationExample();
                example.createCriteria().andGoodsIdEqualTo(goodsId).andRawmaterialIdEqualTo(rawmaterialId);
                List<GoodsRawmaterialRelation> goodsRawmaterialRelations = goodsRawmaterialRelationService.selectByExample(example);
                if(goodsRawmaterialRelations!=null && goodsRawmaterialRelations.size()>0){
                    //该原料配比关系存在，做更新操作
                    GoodsRawmaterialRelation updateGoodsRawmaterialRelation = new GoodsRawmaterialRelation();
                    updateGoodsRawmaterialRelation.setId(goodsRawmaterialRelations.get(0).getId());
                    updateGoodsRawmaterialRelation.setConsumedQuantity(consumedQuantity);
                    updateGoodsRawmaterialRelation.setUpdateTime(new Date());
                    goodsRawmaterialRelationService.updateByPrimaryKeySelective(updateGoodsRawmaterialRelation);

                }else{
                    //该原料配比关系不存在，做新增操作
                    GoodsRawmaterialRelation insertGoodsRawmaterialRelation = new GoodsRawmaterialRelation();
                    insertGoodsRawmaterialRelation.setGoodsId(goodsId);
                    insertGoodsRawmaterialRelation.setRawmaterialId(rawmaterialId);
                    insertGoodsRawmaterialRelation.setConsumedQuantity(consumedQuantity);
                    insertGoodsRawmaterialRelation.setCreateTime(new Date());
                    insertGoodsRawmaterialRelation.setUpdateTime(new Date());
                    goodsRawmaterialRelationService.insertSelective(insertGoodsRawmaterialRelation);
                }
            }
        }
        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("设置成功");
        return basicResult;
    }


    @Operation(summary = "修改原料配比")@PutMapping(value = "/update")
    public BasicResult update(@RequestBody @Validated(value = {}) GoodsRawmaterialRelation goodsRawmaterialRelation){
        BasicResult basicResult = new BasicResult();

        goodsRawmaterialRelation.setUpdateTime(new Date());
        goodsRawmaterialRelationService.updateByPrimaryKeySelective(goodsRawmaterialRelation);

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("修改成功");
        return basicResult;
    }


    @Operation(summary = "删除原料配比")@DeleteMapping(value = "/delete")
    public BasicResult delete(@RequestBody @Validated(value = {}) GoodsRawmaterialRelation param){
        BasicResult basicResult = new BasicResult();

        GoodsRawmaterialRelation dbGoodsRawmaterialRelation = goodsRawmaterialRelationService.selectByPrimaryKey(param.getId());
        if(dbGoodsRawmaterialRelation == null){
            basicResult.setSuccess(false);
            basicResult.setCode(BasicResultCode.ERR);
            basicResult.setMessage("该原料配比不存在，删除失败");
            return basicResult;
        }

        //删除原料配比
        goodsRawmaterialRelationService.deleteByPrimaryKey(param.getId());

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("删除成功");
        return basicResult;
    }
    @Operation(summary = "查询原料配比信息")@PostMapping(value = "/find/goodsRawmaterialRelation")
    public BasicResult findGoodsRawmaterialRelation(@RequestBody @Validated(value = {}) GoodsRawmaterialRelation param){
        BasicData basicResult = new BasicData();
        Page<GoodsRawmaterialRelation> page = goodsRawmaterialRelationService.findGoodsRawmaterialRelation(param.getPageNo(), param.getPageSize(), param.getGoodsName());
        return BasicResult.success(page);
    }

    @Operation(summary = "通过商品查询原料配比信息详情")@PostMapping(value = "/find/goodsRawmaterialRelationByGoodsId")
    public BasicResult findGoodsRawmaterialRelationByGoodsId(@RequestBody @Validated(value = {}) GoodsRawmaterialRelation param){
        BasicData basicResult = new BasicData();
        Page<GoodsRawmaterialRelation> page = goodsRawmaterialRelationService.selectByGoodsId(param.getPageNo(), param.getPageSize(), param.getGoodsId());
        return BasicResult.success(page);
    }
}