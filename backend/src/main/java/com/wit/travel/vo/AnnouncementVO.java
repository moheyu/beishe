package com.wit.travel.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementVO {

    private Long id;

    private String title;

    private String content;

    private Integer type;

    private Integer isTop;

    private Integer status;

    private LocalDateTime publishTime;

    private LocalDateTime createTime;
}
