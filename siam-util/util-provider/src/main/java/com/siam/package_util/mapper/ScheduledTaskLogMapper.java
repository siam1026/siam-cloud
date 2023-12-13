package com.siam.package_util.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.siam.package_util.entity.ScheduledTaskLog;
import com.siam.package_util.model.example.ScheduledTaskLogExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ScheduledTaskLogMapper extends BaseMapper<ScheduledTaskLog> {
    int countByExample(ScheduledTaskLogExample example);

    int deleteByExample(ScheduledTaskLogExample example);

    int deleteByPrimaryKey(Integer id);

    int insertSelective(ScheduledTaskLog record);

    List<ScheduledTaskLog> selectByExample(ScheduledTaskLogExample example);

    ScheduledTaskLog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ScheduledTaskLog record, @Param("example") ScheduledTaskLogExample example);

    int updateByExample(@Param("record") ScheduledTaskLog record, @Param("example") ScheduledTaskLogExample example);

    int updateByPrimaryKeySelective(ScheduledTaskLog record);

    int updateByPrimaryKey(ScheduledTaskLog record);
}