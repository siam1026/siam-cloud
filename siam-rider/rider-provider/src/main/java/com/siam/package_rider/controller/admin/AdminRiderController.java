package com.siam.package_rider.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.entity.BasicData;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_rider.entity.Rider;
import com.siam.package_rider.service.RiderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/rest/admin/rider")
@Transactional(rollbackFor = Exception.class)
@Api(tags = "后台商家自配送骑手信息模块相关接口", description = "AdminRiderController")
public class AdminRiderController {

    @Autowired
    private RiderService riderService;

    @ApiOperation(value = "骑手信息列表")
    @PostMapping(value = "/list")
    public BasicResult list(@RequestBody @Validated(value = {}) Rider param) {
        BasicData basicResult = new BasicData();
        Page<Map<String, Object>> page = riderService.getListJoinShop(param.getPageNo(), param.getPageSize(), param);

        return BasicResult.success(page);
    }

//    @ApiOperation(value = "骑手信息创建")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "骑手信息主键id", required = false, paramType = "query", dataType = "int"),
//            @ApiImplicitParam(name = "name", value = "骑手信息名称", required = true, paramType = "query", dataType = "string"),
//            @ApiImplicitParam(name = "sortNumber", value = "排序", required = false, paramType = "query", dataType = "int"),
//            @ApiImplicitParam(name = "isDisabled", value = "是否启用 0-启用、1-禁用", required = false, paramType = "query", dataType = "int"),
//            @ApiImplicitParam(name = "detail", value = "商家自配送骑手信息详情描述", required = false, paramType = "query", dataType = "string")
//    })
//    @PostMapping(value = "/insert")
//    public BasicResult insert(Rider rider){
//        BasicResult basicResult = new BasicResult();
//
//        rider.setCreateTime(new Date());
//        rider.setUpdateTime(new Date());
//        riderService.insertSelective(rider);
//
//        basicResult.setSuccess(true);
//        basicResult.setCode(BasicResultCode.SUCCESS);
//        basicResult.setMessage("创建成功");
//        return basicResult;
//    }
//
//
//    @ApiOperation(value = "骑手信息修改")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "骑手信息主键id", required = false, paramType = "query", dataType = "int"),
//            @ApiImplicitParam(name = "name", value = "骑手信息名称", required = true, paramType = "query", dataType = "string"),
//            @ApiImplicitParam(name = "sortNumber", value = "排序", required = false, paramType = "query", dataType = "int"),
//            @ApiImplicitParam(name = "isDisabled", value = "是否启用 0-启用、1-禁用", required = false, paramType = "query", dataType = "int"),
//            @ApiImplicitParam(name = "detail", value = "商家自配送骑手信息详情描述", required = false, paramType = "query", dataType = "string")
//    })
//    @PutMapping(value = "/update")
//    public BasicResult update(Rider rider){
//        BasicResult basicResult = new BasicResult();
//
//        rider.setUpdateTime(new Date());
//        riderService.updateByPrimaryKeySelective(rider);
//
//        basicResult.setSuccess(true);
//        basicResult.setCode(BasicResultCode.SUCCESS);
//        basicResult.setMessage("修改成功");
//        return basicResult;
//    }
//
//
//    @ApiOperation(value = "骑手信息删除")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "骑手信息主键id集合(批量删除时id以逗号分隔)", required = true, paramType = "query", dataType = "String")
//    })
//    @DeleteMapping(value = "/delete")
//    public BasicResult delete(@RequestParam(value = "ids") List<Integer> ids){
//        BasicResult basicResult = new BasicResult();
//        for (Integer id : ids) {
//            riderService.deleteByPrimaryKey(id);
//        }
//        basicResult.setSuccess(true);
//        basicResult.setCode(BasicResultCode.SUCCESS);
//        basicResult.setMessage("删除成功");
//        return basicResult;
//    }
}