package com.siam.package_util.entity;

import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

@TableName("tb_scheduled_task_log")
 @Schema(description= "定时任务执行日志表")
public class ScheduledTaskLog {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @Schema(description = "任务代码")
    private String scheduledTaskCode;

    @Schema(description = "执行状态 1=执行成功 2=执行出错")
    private Integer state;

    @Schema(description = "错误描述")
    private String error;

    @Schema(description = "执行主机名称")
    private String hostName;

    @Schema(description = "执行主机ip地址")
    private String hostIpAddress;

    @Schema(description = "创建时间")
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getScheduledTaskCode() {
        return scheduledTaskCode;
    }

    public void setScheduledTaskCode(String scheduledTaskCode) {
        this.scheduledTaskCode = scheduledTaskCode == null ? null : scheduledTaskCode.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error == null ? null : error.trim();
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName == null ? null : hostName.trim();
    }

    public String getHostIpAddress() {
        return hostIpAddress;
    }

    public void setHostIpAddress(String hostIpAddress) {
        this.hostIpAddress = hostIpAddress == null ? null : hostIpAddress.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}