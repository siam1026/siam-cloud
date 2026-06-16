package com.siam.package_user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

@TableName("tb_admin_token")
 @Schema(description= "管理员登录token令牌表")
public class AdminToken {
    @Schema(description = "主键id")
    private Integer id;

    @Schema(description = "管理员id")
    private Integer adminId;

    @Schema(description = "管理员用户名")
    private String username;

    @Schema(description = "登录令牌")
    private String token;

    @Schema(description = "登录方式 wap")
    private String type;

    @Schema(description = "创建时间")
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}