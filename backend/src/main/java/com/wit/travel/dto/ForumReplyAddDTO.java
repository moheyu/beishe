package com.wit.travel.dto;

import javax.validation.constraints.NotBlank;

public class ForumReplyAddDTO {

    @NotBlank(message = "回复内容不能为空")
    private String content;

    private Long postId;

    private Long parentId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
