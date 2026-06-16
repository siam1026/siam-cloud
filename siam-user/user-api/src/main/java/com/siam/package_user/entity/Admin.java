package com.siam.package_user.entity;

import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@TableName("tb_admin")
 @Schema(description= "管理员表")
public class Admin {
    @Schema(description = "主键id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @Schema(description = "管理员用户名")
    private String username;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "密码盐值")
    private String passwordSalt;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "权限")
    private String roles;

    @Schema(description = "是否禁用 0=启用 1=禁用")
    private Boolean isDisabled;

    @Schema(description = "注册时间")
    private Date createTime;

    @Schema(description = "修改时间")
    private Date updateTime;

    @Schema(description = "最后登录时间")
    private Date lastLoginTime;
}