package com.siam.package_promotion.controller.member;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.constant.BusinessType;
import com.siam.package_common.constant.Quantity;
import com.siam.package_common.entity.BasicData;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_common.util.ImageComposeUtils;
import com.siam.package_common.util.OSSUtils;
import com.siam.package_promotion.entity.Advertisement;
import com.siam.package_promotion.model.param.AdvertisementParam;
import com.siam.package_promotion.service.AdvertisementService;
import com.siam.package_user.auth.cache.MemberSessionManager;
import com.siam.package_user.entity.Member;
import com.siam.package_user.util.TokenUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping(value = "/rest/member/advertisement")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "用户广告轮播图模块相关接口", description = "MemberAdvertisementController")
public class MemberAdvertisementController {

    @Autowired
    private AdvertisementService advertisementService;

    @Autowired
    private OSSUtils ossUtils;

    @Autowired
    private ImageComposeUtils imageComposeUtils;

    @Autowired
    private MemberSessionManager memberSessionManager;

    @Operation(summary = "广告轮播图列表")@PostMapping(value = "/list")
    public BasicResult list(@RequestBody @Validated(value = {}) AdvertisementParam advertisement, HttpServletRequest request){
        BasicData basicResult = new BasicData();
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());

        Page<Advertisement> page = advertisementService.getListByPage(advertisement);

        if(advertisement.getType().equals(Quantity.INT_4)){
            //截取个人邀请分享太阳码的文件名
            int suncode_index = loginMember.getInviteSuncode().lastIndexOf("/");
            String suncode_fileName = loginMember.getInviteSuncode().substring(suncode_index+1);

            //如果类型为分享页面生成美图，则需要合成个人邀请二维码
            page.getRecords().forEach(dbAdvertisement -> {
                int index = dbAdvertisement.getImagePath().lastIndexOf("/");
                String poster_fileName = dbAdvertisement.getImagePath().substring(index+1);
                String fileName = suncode_fileName + "--" + poster_fileName;
                String filePath = "data/images/invite_poster_compose/" + loginMember.getId() + "/" + fileName;
                Boolean isExist = ossUtils.doesObjectExist(filePath);
                if (isExist){
                    dbAdvertisement.setImagePath(filePath);
                } else {
                    //合成并上传图片
                    String compose_path = "data/images/invite_poster_compose/" + loginMember.getId();
                    String compose_fileName = suncode_fileName + "--" + poster_fileName;
                    String savePath = compose_path + "/" + compose_fileName;
                    try {
                        imageComposeUtils.compoundImage(BusinessType.OSS_PREFIX + dbAdvertisement.getImagePath(), BusinessType.OSS_PREFIX + loginMember.getInviteSuncode(), savePath, loginMember.getVipNo());
                        dbAdvertisement.setImagePath(savePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        return BasicResult.success(page);
    }

    public static void main(String[] args) {
        String imagePath = "data/images/admin/1/siam_1590994340097.jpg";
        int index = imagePath.indexOf("siam_");
        String substring = imagePath.substring(index);
        System.out.println(substring);
    }
}