package com.wit.travel.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 评论添加DTO
 */
@Data
public class CommentAddDTO {

    private Long scenicId;

    private Long routeId;

    @NotBlank(message = "评论内容不能为空")
    private String content;
}
