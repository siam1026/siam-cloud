package com.siam.package_goods.model.dto.internal;

import com.siam.package_goods.entity.internal.MemberGoodsCollect;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableField;

@Data
public class MemberGoodsCollectDto extends MemberGoodsCollect {

    @ApiModelProperty(notes = "商品名称")
    private String goodsName;


    //页码
    @TableField(exist = false) private Integer pageNo = 1;

    //页面大小
    @TableField(exist = false) private Integer pageSize = 20;

}