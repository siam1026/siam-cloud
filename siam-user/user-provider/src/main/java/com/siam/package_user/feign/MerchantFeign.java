package com.siam.package_user.feign;

import com.siam.package_user.entity.Merchant;
import com.siam.package_user.model.example.MerchantExample;
import com.siam.package_user.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
@RequestMapping(value = "/rest/merchantFeign")
public class MerchantFeign {

    @Autowired
    private MerchantService merchantService;

    @RequestMapping(value = "/countByExample")
    int countByExample(@RequestBody MerchantExample example){
        return merchantService.countByExample(example);
    }

    @RequestMapping(value = "/insertSelective")
    void insertSelective(@RequestBody Merchant record){
        merchantService.insertSelective(record);
    }

    @RequestMapping(value = "/selectByExample")
    List<Merchant> selectByExample(@RequestBody MerchantExample example){
        return merchantService.selectByExample(example);
    }

    @RequestMapping(value = "/selectByPrimaryKey")
    Merchant selectByPrimaryKey(@RequestParam("id") Integer id){
        return merchantService.selectByPrimaryKey(id);
    }

    @RequestMapping(value = "/updateByPrimaryKeySelective")
    void updateByPrimaryKeySelective(@RequestBody Merchant record){
        merchantService.updateByPrimaryKeySelective(record);
    }

    @RequestMapping(value = "/selectByMobile")
    Merchant selectByMobile(@RequestParam("mobile") String mobile){
        return merchantService.selectByMobile(mobile);
    }
}