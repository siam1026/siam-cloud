package com.siam.package_promotion.model.dto;

import com.siam.package_promotion.entity.Coupons;
import lombok.Data;

@Data
public class CouponsDto extends Coupons {

    //店铺id
    private Integer shopId;

}