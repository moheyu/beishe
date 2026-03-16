package com.wit.travel.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 景点信息VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScenicVO {

    private Long id;

    private String name;

    private String description;


    private List<String> images;

    private BigDecimal price;

    private BigDecimal score;

    private String location;

    private Integer viewCount;

    private Long categoryId;

    private String categoryName;

    private List<String> tags;

    private Integer status;

    private String createTime;
}
