package com.wit.travel.dto;

import lombok.Data;

import java.util.List;

/**
 * 偏好添加DTO
 */
@Data
public class PreferenceAddDTO {

    private List<Long> categoryIds;

    private Long tagId;

    private Integer budget;

    private String season;
}
