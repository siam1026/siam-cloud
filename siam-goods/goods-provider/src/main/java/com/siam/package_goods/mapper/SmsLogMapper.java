package com.siam.package_goods.mapper;

import com.siam.package_goods.entity.SmsLog;
import com.siam.package_goods.model.example.SmsLogExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SmsLogMapper {
    int countByExample(SmsLogExample example);

    int deleteByExample(SmsLogExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SmsLog record);

    int insertSelective(SmsLog record);

    List<SmsLog> selectByExample(SmsLogExample example);

    SmsLog selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SmsLog record, @Param("example") SmsLogExample example);

    int updateByExample(@Param("record") SmsLog record, @Param("example") SmsLogExample example);

    int updateByPrimaryKeySelective(SmsLog record);

    int updateByPrimaryKey(SmsLog record);

    @ResultMap("BaseResultMap")
    @Select("select * from tb_sms_log where mobile = #{mobile} and type = #{type} order by id desc limit 1 ")
    SmsLog getLastLog(@Param("mobile") String mobile, @Param("type") String type);
}