package com.wit.travel.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ScenicUpdateDTO {

    private String name;

    private Long categoryId;

    private BigDecimal price;

    private String location;

    private String description;

    private String images;

    private Integer status;

    private Integer recommendLevel;
}