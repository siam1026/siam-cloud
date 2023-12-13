package com.siam.package_util.service;

import com.siam.package_util.entity.ScheduledTask;
import com.siam.package_util.model.example.ScheduledTaskExample;

import java.util.List;

public interface ScheduledTaskService {
    int countByExample(ScheduledTaskExample example);

    void insertSelective(ScheduledTask record);

    List<ScheduledTask> selectByExample(ScheduledTaskExample example);

    ScheduledTask selectByPrimaryKey(Integer id);

    void updateByExampleSelective(ScheduledTask record, ScheduledTaskExample example);

    void updateByPrimaryKeySelective(ScheduledTask record);

    /**
     * 启动定时任务
     * @return
     */
    int updateByStartScheduledTask(String code);

    /**
     * 终止定时任务
     * @return
     */
    int updateByEndScheduledTask(String code);

    ScheduledTask selectByCode(String code);
}