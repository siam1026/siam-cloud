package com.siam.package_promotion.entity;

import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@TableName("tb_advertisement")
 @Schema(description= "广告轮播图表")
public class Advertisement {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @Schema(description = "轮播图名称")
    private String imageName;

    @Schema(description = "轮播图路径")
    private String imagePath;

    @Schema(description = "说明")
    private String description;

    @Schema(description = "轮播图类型 1=首页轮播图 2=菜单页轮播图")
    private Integer type;

    @Schema(description = "排序号")
    private Integer sortNumber;

    @Schema(description = "点击轮播图跳转的链接")
    private String imageLinkUrl;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "修改时间")
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName == null ? null : imageName.trim();
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath == null ? null : imagePath.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(Integer sortNumber) {
        this.sortNumber = sortNumber;
    }

    public String getImageLinkUrl() {
        return imageLinkUrl;
    }

    public void setImageLinkUrl(String imageLinkUrl) {
        this.imageLinkUrl = imageLinkUrl == null ? null : imageLinkUrl.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}