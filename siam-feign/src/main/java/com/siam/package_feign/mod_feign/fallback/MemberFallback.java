package com.siam.package_feign.mod_feign.fallback;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_feign.mod_feign.user.MemberFeignClient;
import com.siam.package_user.entity.Member;
import com.siam.package_user.model.example.MemberExample;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Component
public class MemberFallback implements MemberFeignClient {
    @Override
    public int countByExample(Member example) {
        return 0;
    }

    @Override
    public void insertSelective(Member record) {

    }

    @Override
    public List<Member> selectByExample(MemberExample example) {
        return null;
    }

    @Override
    public Member selectByPrimaryKey(Integer id) {
        return null;
    }

    @Override
    public BasicResult updateByPrimaryKeySelective(Member record) {
        return null;
    }

    @Override
    public Member selectByMobile(String mobile) {
        return null;
    }

    @Override
    public int selectCountRegister(Date startTime, Date endTime) {
        return 0;
    }

    @Override
    public void updateIsRemindNewPeople() {

    }

    @Override
    public void updateIsRequestWxNotify() {

    }

    @Override
    public void queryWxPublicPlatformOpenId() throws IOException {

    }
}