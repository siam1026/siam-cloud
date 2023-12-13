package com.siam.package_goods.model.param;

import com.siam.package_goods.entity.Goods;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * 用户表
 *
 * @author 暹罗
 */
@Data
public class GoodsParam extends Goods {

    //搜索关键字
    private String keywords;

    //排除在外的shopId
    List<Integer> filterList1;
}