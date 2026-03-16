package com.wit.travel.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 景点添加DTO
 */
@Data
public class ScenicAddDTO {
    /**
     * 景点名称（和前端传参字段名统一为name）
     */
    @NotBlank(message = "景点名称不能为空")
    private String name;

    @NotNull(message = "景点分类ID不能为空")
    private Long categoryId;

    // 可选：保留 category 字段，并标记为非必须
    private String category;

    @NotNull(message = "门票价格不能为空")
    private BigDecimal price;

    @NotBlank(message = "景区地址不能为空")
    private String address;

    private String openingHours;

    @NotBlank(message = "景区描述不能为空")
    private String description;

    private List<String> images;
}
