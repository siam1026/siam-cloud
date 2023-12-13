package com.siam.package_user.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_user.entity.Member;
import com.siam.package_user.model.example.MemberExample;
import com.siam.package_user.model.param.MemberParam;
import com.siam.package_user.service.MemberService;
import com.siam.package_util.model.example.PictureUploadRecordExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
public class MemberFeignProvider implements MemberFeignApi {

    @Autowired
    private MemberService memberService;

    public BasicResult countByExample(Member member){
        return BasicResult.success(memberService.countByExample(new MemberExample()));
    }

    public BasicResult insertSelective(Member record){
        memberService.insertSelective(record);
        return BasicResult.success();
    }

    public BasicResult selectByExample(MemberParam param){
        MemberExample example = new MemberExample();
        MemberExample.Criteria criteria = example.createCriteria();
        if(param.getUsername() != null){
            criteria.andUsernameEqualTo(param.getUsername());
        }
        if(param.getMobile() != null){
            criteria.andMobileEqualTo(param.getMobile());
        }
        return BasicResult.success(memberService.selectByExample(example));
    }

    public BasicResult selectByPrimaryKey(@RequestParam("id") Integer id){
        return BasicResult.success(memberService.selectByPrimaryKey(id));
    }

    public BasicResult updateByPrimaryKeySelective(Member record){
//        if(record.getId() == 2 && !record.getIsNewPeople() && !record.getIsRemindNewPeople()){
//            throw new RuntimeException("分布式事务测试，用户服务-用户状态修改异常"));
//        }
        memberService.updateByPrimaryKeySelective(record);
        return BasicResult.success();
    }

    public BasicResult selectByMobile(@RequestParam("mobile") String mobile){
        return BasicResult.success(memberService.selectByMobile(mobile));
    }

    @Override
    public BasicResult updateIsRemindNewPeople() {
        memberService.updateIsRemindNewPeople();
        return BasicResult.success();
    }

    @Override
    public BasicResult updateIsRequestWxNotify() {
        memberService.updateIsRequestWxNotify();
        return BasicResult.success();
    }

    @Override
    public BasicResult queryWxPublicPlatformOpenId() throws IOException {
        memberService.queryWxPublicPlatformOpenId();
        return BasicResult.success();
    }

    /**
     * 查询所有注册人数
     *
     * @author 暹罗
     */
    public BasicResult selectCountRegister(Date startTime, Date endTime){
        return BasicResult.success(memberService.selectCountRegister(startTime, endTime));
    }
}