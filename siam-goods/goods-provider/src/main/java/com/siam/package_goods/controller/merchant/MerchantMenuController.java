package com.siam.package_goods.controller.merchant;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.annoation.MerchantPermission;
import com.siam.package_common.constant.BasicResultCode;
import com.siam.package_common.entity.BasicData;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_common.exception.StoneCustomerException;
import com.siam.package_goods.entity.Menu;
import com.siam.package_goods.model.example.MenuGoodsRelationExample;
import com.siam.package_goods.service.MenuGoodsRelationService;
import com.siam.package_goods.service.MenuService;
import com.siam.package_merchant.auth.cache.MerchantSessionManager;
import com.siam.package_merchant.entity.Merchant;
import com.siam.package_user.util.TokenUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping(value = "/rest/merchant/menu")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "商家端菜单分类模块相关接口", description = "MerchantMenuController")
public class MerchantMenuController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGoodsRelationService menuGoodsRelationService;

//    @Autowired
//    private MerchantService merchantService;

    @Autowired
    private MerchantSessionManager merchantSessionManager;

    @Operation(summary = "菜单列表")
    @PostMapping(value = "/list")
    public BasicResult list(@RequestBody @Validated(value = {}) Menu menu, HttpServletRequest request){
        BasicData basicResult = new BasicData();

        //获取当前登录用户绑定的门店编号
        Merchant loginMerchant = merchantSessionManager.getSession(TokenUtil.getToken());

        menu.setShopId(loginMerchant.getShopId());
        Page<Menu> page = menuService.getListByAdmin(menu);

        return BasicResult.success(page);
    }

    @Operation(summary = "菜单创建")
    @PostMapping(value = "/insert")
    public BasicResult insert(@RequestBody @Validated(value = {}) Menu menu, HttpServletRequest request){
        BasicResult basicResult = new BasicResult();

        //获取当前登录用户绑定的门店编号
        Merchant loginMerchant = merchantSessionManager.getSession(TokenUtil.getToken());

        menu.setShopId(loginMerchant.getShopId());
        menu.setCreateTime(new Date());
        menu.setUpdateTime(new Date());
        menuService.insertSelective(menu);

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("创建成功");
        return basicResult;
    }

    @MerchantPermission
    @Operation(summary = "菜单修改")
    @PutMapping(value = "/update")
    public BasicResult update(@RequestBody @Validated(value = {}) Menu menu, HttpServletRequest request){
        BasicResult basicResult = new BasicResult();

        //获取当前登录用户绑定的门店编号
        Merchant loginMerchant = merchantSessionManager.getSession(TokenUtil.getToken());

        Menu dbMenu = menuService.selectByPrimaryKey(menu.getId());
        if (dbMenu == null){
            throw new StoneCustomerException("该菜单不存在");
        } else if (loginMerchant.getShopId() != dbMenu.getShopId()){
            throw new StoneCustomerException("您没有权限操作该菜单");
        }

        menu.setUpdateTime(new Date());
        menuService.updateByPrimaryKeySelective(menu);

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("修改成功");
        return basicResult;
    }

    @MerchantPermission
    @Operation(summary = "菜单删除")
    @DeleteMapping(value = "/delete")
    public BasicResult delete(@RequestBody @Validated(value = {}) Menu param                   , HttpServletRequest request){
        BasicResult basicResult = new BasicResult();

        //获取当前登录用户绑定的门店编号
        Merchant loginMerchant = merchantSessionManager.getSession(TokenUtil.getToken());

        for (Integer id : param.getIds()) {
            Menu dbMenu = menuService.selectByPrimaryKey(id);
            if (dbMenu == null){
                throw new StoneCustomerException("该菜单不存在");
            } else if (loginMerchant.getShopId() != dbMenu.getShopId()){
                throw new StoneCustomerException("您没有权限操作该菜单");
            }

            //判断菜单下面有关联商品，则不能删除
            MenuGoodsRelationExample example = new MenuGoodsRelationExample();
            example.createCriteria().andMenuIdEqualTo(id);
            int result = menuGoodsRelationService.countByExample(example);
            if(result > 0){
                basicResult.setSuccess(false);
                basicResult.setCode(BasicResultCode.ERR);
                basicResult.setMessage("该类别下有关联奶茶信息，不允许删除");
                return basicResult;
            }

            menuService.deleteByPrimaryKey(id);
        }
        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("删除成功");
        return basicResult;
    }
}