package com.siam.package_user.feign.fallback;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_user.entity.Member;
import com.siam.package_user.feign.MemberFeignApi;
import com.siam.package_user.model.example.MemberExample;
import com.siam.package_user.model.param.MemberParam;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Component
public class MemberFallback implements MemberFeignApi {

    @Override
    public BasicResult countByExample(Member example) {
        return null;
    }

    @Override
    public BasicResult insertSelective(Member record) {
        return null;
    }

    @Override
    public BasicResult selectByExample(MemberParam example) {
        return null;
    }

    @Override
    public BasicResult selectByPrimaryKey(Integer id) {
        return null;
    }

    @Override
    public BasicResult updateByPrimaryKeySelective(Member record) {
        return null;
    }

    @Override
    public BasicResult selectByMobile(String mobile) {
        return null;
    }

    @Override
    public BasicResult selectCountRegister(Date startTime, Date endTime) {
        return null;
    }

    @Override
    public BasicResult updateIsRemindNewPeople() {
        return null;
    }

    @Override
    public BasicResult updateIsRequestWxNotify() {
        return null;
    }

    @Override
    public BasicResult queryWxPublicPlatformOpenId() throws IOException {
        return null;
    }

    @Override
    public BasicResult<List<Member>> selectAllMemberNoneCoupons(Integer couponsId) {
        return null;
    }

    @Override
    public BasicResult<List<Member>> selectAllMemberNoneCouponsByPointsMall(Integer couponsId) {
        return null;
    }
}