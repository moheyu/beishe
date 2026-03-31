package com.wit.travel.dto;

import lombok.Data;

/**
 * 路线查询 DTO
 */
@Data
public class RouteQueryDTO {

    /** 路线名称（模糊搜索） */
    private String name;

    /** 分类 ID */
    private Long categoryId;

    /** 状态：0-下架，1-上架 */
    private Integer status;

    /** 当前页码，默认第 1 页 */
    private Integer pageNum;

    /** 每页条数，默认 10 条 */
    private Integer pageSize;
}
