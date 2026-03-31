package com.wit.travel.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户偏好详情 VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreferenceDetailVO {

    private Long id;

    private Long userId;

    /** 偏好分类 ID */
    private Long categoryId;

    /** 偏好分类名称 */
    private String categoryName;

    /** 偏好标签 ID */
    private Long tagId;

    /** 偏好标签名称 */
    private String tagName;

    private LocalDateTime createTime;
}
