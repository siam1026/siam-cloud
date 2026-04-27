package com.siam.package_promotion.controller.merchant;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.entity.BasicData;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_common.constant.BasicResultCode;
import com.siam.package_promotion.entity.Advertisement;
import com.siam.package_promotion.model.param.AdvertisementParam;
import com.siam.package_promotion.service.AdvertisementService;
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
@RequestMapping(value = "/rest/merchant/advertisement")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "商家端广告轮播图模块相关接口【暂无调用】", description = "MerchantAdvertisementController")
public class MerchantAdvertisementController {

    @Autowired
    private AdvertisementService advertisementService;

    @Operation(summary = "广告轮播图列表")@PostMapping(value = "/list")
    public BasicResult list(@RequestBody @Validated(value = {}) AdvertisementParam advertisement){
        BasicData basicResult = new BasicData();
        Page<Advertisement> page = advertisementService.getListByPage(advertisement);

        return BasicResult.success(page);
    }

    @Operation(summary = "新增广告轮播图")@PostMapping(value = "/insert")
    public BasicResult insert(@RequestBody @Validated(value = {}) AdvertisementParam advertisement){
        BasicResult basicResult = new BasicResult();

        advertisement.setCreateTime(new Date());
        advertisement.setUpdateTime(new Date());
        advertisementService.insertSelective(advertisement);

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("创建成功");
        return basicResult;
    }


    @Operation(summary = "修改广告轮播图")@PutMapping(value = "/update")
    public BasicResult update(@RequestBody @Validated(value = {}) AdvertisementParam advertisement){
        BasicResult basicResult = new BasicResult();

        //由于商家端编辑时轮播图展示不出来，所以暂时性做逻辑控制，修改时轮播图数组可为空，代表不修改
        if(advertisement.getImagePath().equals("")){
            advertisement.setImagePath(null);
        }
        advertisement.setUpdateTime(new Date());
        advertisementService.updateByPrimaryKeySelective(advertisement);

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("修改成功");
        return basicResult;
    }


    @Operation(summary = "删除广告轮播图")@DeleteMapping(value = "/delete")
    public BasicResult delete(@RequestBody @Validated(value = {}) AdvertisementParam param){
        BasicResult basicResult = new BasicResult();

        Advertisement dbAdvertisement = advertisementService.selectByPrimaryKey(param);
        if(dbAdvertisement == null){
            basicResult.setSuccess(false);
            basicResult.setCode(BasicResultCode.ERR);
            basicResult.setMessage("该广告轮播图不存在，删除失败");
            return basicResult;
        }

        //删除广告轮播图
        advertisementService.deleteByPrimaryKey(param);

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("删除成功");
        return basicResult;
    }
}