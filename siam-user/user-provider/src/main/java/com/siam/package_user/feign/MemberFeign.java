package com.siam.package_user.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_user.entity.Member;
import com.siam.package_user.model.example.MemberExample;
import com.siam.package_user.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
@RequestMapping(value = "/rest/memberFeign")
public class MemberFeign {
    @Autowired
    private MemberService memberService;

    @RequestMapping(value = "/countByExample")
    public int countByExample(@RequestBody Member member){
        return memberService.countByExample(new MemberExample());
    }

    @RequestMapping(value = "/insertSelective")
    public void insertSelective(@RequestBody Member record){
        memberService.insertSelective(record);
    }

    @RequestMapping(value = "/selectByExample")
    public List<Member> selectByExample(@RequestBody Member member){
        return memberService.selectByExample(new MemberExample());
    }

    @RequestMapping(value = "/selectByPrimaryKey")
    public Member selectByPrimaryKey(@RequestParam("id") Integer id){
        return memberService.selectByPrimaryKey(id);
    }

    @RequestMapping(value = "/updateByPrimaryKeySelective")
    public BasicResult updateByPrimaryKeySelective(@RequestBody Member record){
//        if(record.getId() == 2 && !record.getIsNewPeople() && !record.getIsRemindNewPeople()){
//            throw new RuntimeException("分布式事务测试，用户服务-用户状态修改异常");
//        }
        memberService.updateByPrimaryKeySelective(record);
        return BasicResult.success();
    }

    @RequestMapping(value = "/selectByMobile")
    public Member selectByMobile(@RequestParam("mobile") String mobile){
        return memberService.selectByMobile(mobile);
    }

    /**
     * 查询所有注册人数
     *
     * @author 暹罗
     */
    @RequestMapping(value = "/selectCountRegister")
    public int selectCountRegister(@RequestParam("startTime") Date startTime, @RequestParam("endTime") Date endTime){
        return memberService.selectCountRegister(startTime, endTime);
    }
}