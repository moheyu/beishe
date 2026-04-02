package com.wit.travel.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("route_scenic_relation")
public class RouteScenicRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long routeId;

    private Long scenicId;

    private Integer dayNumber;

    private Integer sortOrder;

    private String description;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
