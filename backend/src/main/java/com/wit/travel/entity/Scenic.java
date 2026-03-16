package com.wit.travel.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 景点实体类
 */
@Data
@TableName("scenic")
public class Scenic implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 景点ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 景点名称
     */
    private String name;

    /**
     * 官方介绍
     */
    private String description;

    /**
     * 图片URL（存储JSON字符串，适配列表格式）
     */

    private String images;
    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 评分（0-5）
     */
    private BigDecimal score;

    /**
     * 地点
     */
    private String location;

    /**
     * 浏览量
     */
    private Integer viewCount;

    /**
     * 关联分类ID
     */
    private Long categoryId;

    /**
     * 状态：0-下架，1-上架
     */
    private Integer status;

    private Integer recommendLevel;

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
