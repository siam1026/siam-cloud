package com.siam.package_user.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_user.entity.Member;
import com.siam.package_user.model.example.MemberExample;
import com.siam.package_user.model.param.MemberParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@FeignClient(value = "user-siam")
public interface MemberFeignApi {

    @PostMapping(value = "/api/member/countByExample")
    BasicResult<Integer> countByExample(@RequestBody Member example);

    @PostMapping(value = "/api/member/insertSelective")
    BasicResult insertSelective(@RequestBody Member record);

    @PostMapping(value = "/api/member/selectByExample")
    BasicResult<List<Member>> selectByExample(@RequestBody MemberParam param);

    @PostMapping(value = "/api/member/selectByPrimaryKey")
    BasicResult<Member> selectByPrimaryKey(@RequestParam("id") Integer id);

    @PostMapping(value = "/api/member/updateByPrimaryKeySelective")
    BasicResult updateByPrimaryKeySelective(@RequestBody Member record);

    @PostMapping(value = "/api/member/selectByMobile")
    BasicResult selectByMobile(@RequestParam("mobile") String mobile);

    /**
     * 查询所有注册人数
     *
     * @author 暹罗
     */
    @PostMapping(value = "/api/member/selectCountRegister")
    BasicResult<Integer> selectCountRegister(@RequestParam("startTime") Date startTime, @RequestParam("endTime") Date endTime);

    /**
     * 将新用户的"是否需要弹出新人引导提示"字段值改成是
     *
     * @author 暹罗
     */
    @PostMapping(value = "/api/member/updateIsRemindNewPeople")
    BasicResult updateIsRemindNewPeople();

    /**
     * 修改用户的是否需要请求授权服务通知状态
     *
     * @author 暹罗
     */
    @PostMapping(value = "/api/member/updateIsRequestWxNotify")
    BasicResult updateIsRequestWxNotify();

    /**
     * 同步微信公众号openId
     *
     * @author 暹罗
     */
    @PostMapping(value = "/api/member/queryWxPublicPlatformOpenId")
    BasicResult queryWxPublicPlatformOpenId() throws IOException;
}