package com.wit.travel.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 浏览记录实体
 */
@Data
@TableName("view_record")
public class ViewRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long scenicId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime viewTime;
}
