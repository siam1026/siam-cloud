package com.siam.package_promotion.mapper.internal;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.siam.package_promotion.entity.internal.PointsMallCouponsMemberRelation;
import com.siam.package_promotion.model.example.internal.PointsMallCouponsMemberRelationExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;

public interface PointsMallCouponsMemberRelationMapper extends BaseMapper<PointsMallCouponsMemberRelation> {
    int countByExample(PointsMallCouponsMemberRelationExample example);

    int deleteByExample(PointsMallCouponsMemberRelationExample example);

    int deleteByPrimaryKey(Integer id);

    int insertSelective(PointsMallCouponsMemberRelation record);

    List<PointsMallCouponsMemberRelation> selectByExample(PointsMallCouponsMemberRelationExample example);

    PointsMallCouponsMemberRelation selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PointsMallCouponsMemberRelation record, @Param("example") PointsMallCouponsMemberRelationExample example);

    int updateByExample(@Param("record") PointsMallCouponsMemberRelation record, @Param("example") PointsMallCouponsMemberRelationExample example);

    int updateByPrimaryKeySelective(PointsMallCouponsMemberRelation record);

    int updateByPrimaryKey(PointsMallCouponsMemberRelation record);

    @Select("select count(id) from tb_points_mall_coupons_member_relation where coupons_id = #{couponsId}")
    Integer getCountByPointsMallCouponsId(@Param("couponsId") int couponsId);

    @ResultMap("CustomResultMap")
    @Select("<script>select cmr.*, c.preferential_type as preferentialType, c.discount_amount as discountAmount, c.limited_price as limitedPrice, c.reduced_price as reducedPrice, c.description, c.source from tb_points_mall_coupons_member_relation cmr " +
            "left join tb_points_mall_coupons c on cmr.coupons_id = c.id" +
            "<where> 1=1 " +
            "<if test=\"couponsMemberRelation.id != null\"> AND cmr.id = #{couponsMemberRelation.id} </if>" +
            "<if test=\"couponsMemberRelation.couponsId != null\"> AND cmr.coupons_id like '%${couponsMemberRelation.couponsId}%' </if>" +
            "<if test=\"couponsMemberRelation.couponsName != null and couponsMemberRelation.couponsName !=''\"> AND cmr.coupons_name = #{couponsMemberRelation.couponsName} </if>" +
            "<if test=\"couponsMemberRelation.memberId != null\"> AND cmr.member_id = #{couponsMemberRelation.memberId} </if>" +
            "<if test=\"couponsMemberRelation.isUsed != null\"> AND cmr.is_used = #{couponsMemberRelation.isUsed} </if>" +
            "<if test=\"couponsMemberRelation.isExpired != null\"> AND cmr.is_expired = #{couponsMemberRelation.isExpired} </if>" +
            " AND cmr.is_valid = 1" +
            "</where> order by cmr.create_time desc" +
            "</script>")
    Page<Map<String, Object>> getListByPage(@Param("page") Page page, @Param("couponsMemberRelation") PointsMallCouponsMemberRelation couponsMemberRelation);

    @Update("update tb_points_mall_coupons_member_relation set is_expired=1,update_time=NOW() where end_time<=#{date} and is_expired=0")
    int updateOverduePointsMallCoupons(@Param("date") Date date);

    @Select("select count(id) from tb_points_mall_coupons_member_relation where member_id=#{memberId} and is_used=0 and is_expired=0 and is_valid=1 ")
    Integer getCountsByMemberId(@Param("memberId") Integer memberId);

    @Update("update tb_points_mall_coupons_member_relation set is_valid=0,update_time=NOW() where coupons_id=#{couponsId}")
    int updateValidToFalseByPointsMallCouponsId(@Param("couponsId") Integer couponsId);

    @Update("update tb_points_mall_coupons_member_relation set end_time=#{date},update_time=NOW() where coupons_id=#{couponsId}")
    int updateEndTime(@Param("date") Date date, @Param("couponsId") Integer couponsId);

    @Update("update tb_points_mall_coupons_member_relation set end_time=date_add(start_time,interval #{days} day),update_time=NOW() where coupons_id=#{couponsId}")
    int updaetEndTimeByDays(@Param("days") Integer days, @Param("couponsId") Integer couponsId);
}