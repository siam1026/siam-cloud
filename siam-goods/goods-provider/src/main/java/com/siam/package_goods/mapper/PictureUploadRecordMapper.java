package com.siam.package_goods.mapper;

import com.siam.package_goods.entity.PictureUploadRecord;
import com.siam.package_goods.model.example.PictureUploadRecordExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;

public interface PictureUploadRecordMapper {
    int countByExample(PictureUploadRecordExample example);

    int deleteByExample(PictureUploadRecordExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(PictureUploadRecord record);

    int insertSelective(PictureUploadRecord record);

    List<PictureUploadRecord> selectByExample(PictureUploadRecordExample example);

    PictureUploadRecord selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PictureUploadRecord record, @Param("example") PictureUploadRecordExample example);

    int updateByExample(@Param("record") PictureUploadRecord record, @Param("example") PictureUploadRecordExample example);

    int updateByPrimaryKeySelective(PictureUploadRecord record);

    int updateByPrimaryKey(PictureUploadRecord record);

    @ResultMap("BaseResultMap")
    @Select("<script>select pur.* from tb_picture_upload_record pur" +
            "<where> 1=1 " +
            "<if test=\"pictureUploadRecord.id != null\"> AND pur.id = #{pictureUploadRecord.id} </if>" +
            "<if test=\"pictureUploadRecord.shopId != null\"> AND pur.shop_id = #{pictureUploadRecord.shopId} </if>" +
            "<if test=\"pictureUploadRecord.module != null\"> AND pur.module = #{pictureUploadRecord.module} </if>" +
            "</where> order by pur.id desc" +
            "</script>")
    Page<Map<String, Object>> getListByPage(@Param("page") Page page, @Param("pictureUploadRecord") PictureUploadRecord pictureUploadRecord);
}