package com.wit.travel.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 评论信息VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentVO {

    private Long id;

    private Long userId;

    private String username;

    private String avatar;

    private Long scenicId;

    private String content;

    private String createTime;
}
