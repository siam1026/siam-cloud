package com.siam.package_util.service_impl;

import com.siam.package_util.model.example.SmsLogExample;
import com.siam.package_common.util.DateUtilsPlus;
import com.siam.package_util.entity.SmsLog;
import com.siam.package_util.mapper.SmsLogMapper;
import com.siam.package_util.service.SmsLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SmsLogServiceImpl implements SmsLogService {

    @Autowired
    private SmsLogMapper smsLogMapper;

    public int countByExample(SmsLogExample example){
        return smsLogMapper.countByExample(example);
    }

    public void insertSelective(SmsLog record){
        smsLogMapper.insertSelective(record);
    }

    public List<SmsLog> selectByExample(SmsLogExample example){
        return smsLogMapper.selectByExample(example);
    }

    public SmsLog selectByPrimaryKey(Long id){
        return smsLogMapper.selectByPrimaryKey(id);
    }

    public void updateByExampleSelective(SmsLog record, SmsLogExample example){
        smsLogMapper.updateByExampleSelective(record, example);
    }

    public void updateByPrimaryKeySelective(SmsLog record){
        smsLogMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public SmsLog getLastLog(String mobile, String type, int minutes) {
        SmsLog smsLog = smsLogMapper.getLastLog(mobile, type);
        if(smsLog != null){
            long seconds = DateUtilsPlus.diffSeconds(new Date(), smsLog.getCreateTime());
            if(seconds > minutes * 60){
                return null;
            }
        }
        return smsLog;
    }
}
