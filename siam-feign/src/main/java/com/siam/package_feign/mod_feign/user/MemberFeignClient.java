package com.siam.package_feign.mod_feign.user;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_user.entity.Member;
import com.siam.package_user.model.example.MemberExample;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@FeignClient(value = "user-siam", path = "/rest/memberFeign")
public interface MemberFeignClient {

    @RequestMapping(value = "/countByExample")
    int countByExample(@RequestBody Member example);

    @RequestMapping(value = "/insertSelective")
    void insertSelective(@RequestBody Member record);

    @RequestMapping(value = "/selectByExample")
    List<Member> selectByExample(@RequestBody MemberExample example);

    @RequestMapping(value = "/selectByPrimaryKey")
    Member selectByPrimaryKey(@RequestParam("id") Integer id);

    @RequestMapping(value = "/updateByPrimaryKeySelective")
    BasicResult updateByPrimaryKeySelective(@RequestBody Member record);

    @RequestMapping(value = "/selectByMobile")
    Member selectByMobile(@RequestParam("mobile") String mobile);

    /**
     * 查询所有注册人数
     *
     * @author 暹罗
     */
    @RequestMapping(value = "/selectCountRegister")
    int selectCountRegister(@RequestParam("startTime") Date startTime, @RequestParam("endTime") Date endTime);

    /**
     * 将新用户的"是否需要弹出新人引导提示"字段值改成是
     *
     * @author 暹罗
     */
    @RequestMapping(value = "/updateIsRemindNewPeople")
    void updateIsRemindNewPeople();

    /**
     * 修改用户的是否需要请求授权服务通知状态
     *
     * @author 暹罗
     */
    @RequestMapping(value = "/updateIsRequestWxNotify")
    void updateIsRequestWxNotify();

    /**
     * 同步微信公众号openId
     *
     * @author 暹罗
     */
    @RequestMapping(value = "/queryWxPublicPlatformOpenId")
    void queryWxPublicPlatformOpenId() throws IOException;
}