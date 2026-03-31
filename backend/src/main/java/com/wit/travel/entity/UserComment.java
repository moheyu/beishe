package com.wit.travel.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户评论实体
 */
@Data
@TableName("user_comment")
public class UserComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    /** 评论目标类型：1-景点，2-路线 */
    private Integer type;

    /** 目标 ID（景点 ID 或路线 ID） */
    private Long targetId;

    private String content;

    /** 审核状态：0-待审核，1-已通过 */
    private Integer isAudit;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
