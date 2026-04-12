package com.wit.travel.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ScenicUpdateDTO {

    private String name;

    private Long categoryId;

    private BigDecimal price;

    private String location;

    private String openingHours;

    private String description;

    private List<String> images;

    private Integer status;

    private Integer recommendLevel;
}