package com.wit.travel.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteDetailVO {

    private Long id;

    private String name;

    private String description;

    private Long categoryId;

    private String categoryName;

    private BigDecimal budget;

    private Integer durationDays;

    private String bestSeason;

    private List<String> images;

    private Integer viewCount;

    private Integer status;

    private String createTime;

    private List<RouteDayVO> routeDays;
}
