package com.siam.package_mall.controller.member;

import com.siam.package_mall.service.PointsMallGoodsSpecificationService;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rest/pointsMall/goodsSpecification")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "商品规格模块相关接口", description = "PointsMallGoodsSpecificationController")
public class PointsMallGoodsSpecificationController {
    @Autowired
    private PointsMallGoodsSpecificationService goodsSpecificationService;

}