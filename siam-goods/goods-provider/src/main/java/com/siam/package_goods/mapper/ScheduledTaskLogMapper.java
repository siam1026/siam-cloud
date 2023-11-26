package com.siam.package_goods.mapper;

import com.siam.package_goods.entity.ScheduledTaskLog;
import com.siam.package_goods.model.example.ScheduledTaskLogExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ScheduledTaskLogMapper {
    int countByExample(ScheduledTaskLogExample example);

    int deleteByExample(ScheduledTaskLogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ScheduledTaskLog record);

    int insertSelective(ScheduledTaskLog record);

    List<ScheduledTaskLog> selectByExample(ScheduledTaskLogExample example);

    ScheduledTaskLog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ScheduledTaskLog record, @Param("example") ScheduledTaskLogExample example);

    int updateByExample(@Param("record") ScheduledTaskLog record, @Param("example") ScheduledTaskLogExample example);

    int updateByPrimaryKeySelective(ScheduledTaskLog record);

    int updateByPrimaryKey(ScheduledTaskLog record);
}