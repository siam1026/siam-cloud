package com.siam.package_mall.model.dto;

import com.siam.package_mall.entity.PointsMallMemberGoodsCollect;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PointsMallMemberGoodsCollectDto extends PointsMallMemberGoodsCollect {

    @Schema(description = "商品名称")
    private String goodsName;

}