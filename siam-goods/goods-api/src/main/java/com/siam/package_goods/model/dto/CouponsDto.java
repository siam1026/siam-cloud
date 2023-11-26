package com.siam.package_goods.model.dto;

import com.siam.package_goods.entity.Coupons;
import lombok.Data;

@Data
public class CouponsDto extends Coupons {

    //店铺id
    private Integer shopId;

}