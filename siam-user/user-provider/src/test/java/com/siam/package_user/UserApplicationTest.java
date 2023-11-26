package com.siam.package_user;

import com.siam.package_user.auth.cache.MemberSessionManager;
import com.siam.package_user.entity.Member;
import com.siam.package_user.service.MemberService;
import com.siam.package_user.util.TokenUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableConfigurationProperties
public class UserApplicationTest {

    @Autowired
    private MemberSessionManager memberSessionManager;

    @Autowired
    private MemberService memberService;

    @Test
    public void testGetMaxVipNo(){
        String i = memberService.getNextVipNo();
        System.out.println(i);
    }

    @Test
    public void test(){
        Member member = new Member();
        member.setId(9798798);
        member.setPassword("sfdasfad");
        String token = TokenUtil.generateToken(member);
        System.out.println("\n\ntoken = " + token);
    }

    @Test
    public void test2(){
//        String token = "001";
//        Member member = memberService.selectByMobile("18711389426");
//        memberSessionManager.createSession(token, member);
//
//        Member loginMember = memberSessionManager.getSession(token);
//        System.out.println(loginMember + " -- mobile: " + loginMember.getMobile());


//        memberService.selectByExample(new MemberExample());

        System.out.println(TokenUtil.getToken());
    }
}