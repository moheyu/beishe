package com.wit.travel.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户偏好实体类
 */
@Data
@TableName("user_preference")
public class UserPreference implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 偏好ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 偏好分类ID（可空）
     */
    private Long categoryId;

    /**
     * 偏好标签ID（可空）
     */
    private Long tagId;

    /**
     * 预算范围
     */
    private Integer budget;

    /**
     * 出行季节：spring/summer/autumn/winter/all
     */
    private String season;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
