package com.wit.travel.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 旅游路线实体
 */
@Data
@TableName("travel_route")
public class TravelRoute implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 路线名称 */
    private String name;

    /** 路线描述 */
    private String description;

    /** 图片 URL 列表（JSON 字符串） */
    private String images;

    /** 行程天数 */
    private Integer durationDays;

    /** 预算（元） */
    private BigDecimal budget;

    /** 分类 ID */
    private Long categoryId;

    /** 状态：0-下架，1-上架 */
    private Integer status;

    /** 逻辑删除标记：0-正常，1-已删除 */
    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
