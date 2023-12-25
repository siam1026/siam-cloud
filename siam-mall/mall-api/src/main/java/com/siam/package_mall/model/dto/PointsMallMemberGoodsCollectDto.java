package com.siam.package_mall.model.dto;

import com.siam.package_mall.entity.PointsMallMemberGoodsCollect;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PointsMallMemberGoodsCollectDto extends PointsMallMemberGoodsCollect {

    @ApiModelProperty(notes = "商品名称")
    private String goodsName;

}