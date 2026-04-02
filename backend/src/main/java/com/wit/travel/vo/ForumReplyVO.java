package com.wit.travel.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForumReplyVO {

    private Long id;

    private Long postId;

    private Long userId;

    private Long parentId;

    private String content;

    private String username;

    private String avatar;

    private String parentUsername;

    private LocalDateTime createTime;
}
