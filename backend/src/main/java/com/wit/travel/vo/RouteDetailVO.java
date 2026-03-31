package com.wit.travel.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 旅游路线详情 VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteDetailVO {

    private Long id;

    private String name;

    private String description;

    private List<String> images;

    /** 行程天数 */
    private Integer durationDays;

    /** 预算（元） */
    private BigDecimal budget;

    /** 分类 ID */
    private Long categoryId;

    /** 分类名称 */
    private String categoryName;

    /** 路线包含的景点列表 */
    private List<ScenicVO> scenicList;

    /** 评论列表 */
    private List<CommentVO> comments;

    /** 状态：0-下架，1-上架 */
    private Integer status;

    private String createTime;
}
