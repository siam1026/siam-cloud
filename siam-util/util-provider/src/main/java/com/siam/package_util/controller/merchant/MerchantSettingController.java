package com.siam.package_util.controller.merchant;

/*
@RestController
@RequestMapping(value = "/rest/merchant/setting")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "商家端基础数据设置模块相关接口", description = "MerchantSettingController")
public class MerchantSettingController {

    @Autowired
    private SettingService settingService;

    @Autowired
    private GoodsSpecificationOptionFeignApi goodsSpecificationOptionService;

    @Operation(summary = "基础数据设置列表")@PostMapping(value = "/list")
    public BasicResult list(int pageNo, int pageSize, Setting setting){
        BasicData basicResult = new BasicData();
        Page<Setting> page = settingService.getListByPage(param.getPageNo(), param.getPageSize(), setting);

        return BasicResult.success(page);
    }

    @Operation(summary = "修改基础数据设置")@PutMapping(value = "/update")
    public BasicResult update(Setting setting){
        BasicResult basicResult = new BasicResult();

        setting.setUpdateTime(new Date());
        settingService.updateByPrimaryKeySelective(setting);

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("修改成功");
        return basicResult;
    }
}*/
