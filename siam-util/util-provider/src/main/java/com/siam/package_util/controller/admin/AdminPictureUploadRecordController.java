package com.siam.package_util.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_util.service.PictureUploadRecordService;
import com.siam.package_common.entity.BasicData;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_util.entity.PictureUploadRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/rest/admin/pictureUploadRecord")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "后台图片上传记录模块相关接口", description = "AdminPictureUploadRecordController")
public class AdminPictureUploadRecordController {

    @Autowired
    private PictureUploadRecordService pictureUploadRecordService;

    @Operation(summary = "图片上传记录列表")
    @PostMapping(value = "/list")
    public BasicResult list(@RequestBody @Validated(value = {}) PictureUploadRecord pictureUploadRecord){
        BasicData basicResult = new BasicData();
        Page<PictureUploadRecord> page = pictureUploadRecordService.getList(pictureUploadRecord.getPageNo(), pictureUploadRecord.getPageSize(), pictureUploadRecord);

        return BasicResult.success(page);
    }

    /*@Operation(summary = "图片上传记录创建")@PostMapping(value = "/insert")
    public BasicResult insert(PictureUploadRecord pictureUploadRecord){
        BasicResult basicResult = new BasicResult();

        pictureUploadRecord.setCreateTime(new Date());
        pictureUploadRecord.setUpdateTime(new Date());
        pictureUploadRecordService.insertSelective(pictureUploadRecord);

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("创建成功");
        return basicResult;
    }


    @Operation(summary = "图片上传记录修改")@PutMapping(value = "/update")
    public BasicResult update(PictureUploadRecord pictureUploadRecord){
        BasicResult basicResult = new BasicResult();

        pictureUploadRecord.setUpdateTime(new Date());
        pictureUploadRecordService.updateByPrimaryKeySelective(pictureUploadRecord);

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("修改成功");
        return basicResult;
    }*/


//    @Operation(summary = "图片上传记录删除")
////    @DeleteMapping(value = "/delete")
//    public BasicResult delete(@RequestBody @Validated(value = {}) AdminParam param  @RequestParam(value = "ids") List<Integer> ids){
//        BasicResult basicResult = new BasicResult();
//        for (Integer id : ids) {
//            pictureUploadRecordService.deleteByPrimaryKey(id);
//        }
//        basicResult.setSuccess(true);
//        basicResult.setCode(BasicResultCode.SUCCESS);
//        basicResult.setMessage("删除成功");
//        return basicResult;
//    }
}