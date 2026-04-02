package com.wit.travel.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForumPostVO {

    private Long id;

    private String title;

    private String content;

    private Long userId;

    private String username;

    private String avatar;

    private Long scenicId;

    private Long routeId;

    private Integer viewCount;

    private Integer replyCount;

    private Integer isTop;

    private Integer isEssence;

    private Integer isAudit;

    private LocalDateTime createTime;
}
