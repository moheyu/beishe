package com.wit.travel.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScenicStatisticsVO {

    private Long id;

    private String name;

    private Long viewCount;

    private Long collectionCount;

    private Long commentCount;

    private Double avgScore;

    private Integer recommendLevel;
}
