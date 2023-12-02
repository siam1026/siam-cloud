package com.siam.package_goods.model.dto.internal;

import com.siam.package_goods.entity.internal.PointsMallMemberGoodsCollect;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PointsMallMemberGoodsCollectDto extends PointsMallMemberGoodsCollect {

    @ApiModelProperty(notes = "商品名称")
    private String goodsName;

}