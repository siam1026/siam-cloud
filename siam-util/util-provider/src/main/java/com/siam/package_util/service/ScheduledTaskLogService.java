package com.siam.package_util.service;

import com.siam.package_util.entity.ScheduledTaskLog;
import com.siam.package_util.model.example.ScheduledTaskLogExample;

import java.util.List;

public interface ScheduledTaskLogService {
    int countByExample(ScheduledTaskLogExample example);

    void insertSelective(ScheduledTaskLog record);

    List<ScheduledTaskLog> selectByExample(ScheduledTaskLogExample example);

    ScheduledTaskLog selectByPrimaryKey(Integer id);

    void updateByExampleSelective(ScheduledTaskLog record, ScheduledTaskLogExample example);

    void updateByPrimaryKeySelective(ScheduledTaskLog record);
}
