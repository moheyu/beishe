package com.wit.travel.controller;

import com.wit.travel.dto.CommentAddDTO;
import com.wit.travel.entity.UserComment;
import com.wit.travel.service.UserCommentService;
import com.wit.travel.util.SecurityUtil;
import com.wit.travel.vo.CommentVO;
import com.wit.travel.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 评论控制器
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private UserCommentService commentService;

    @PostMapping
    public Result<String> addComment(@RequestBody CommentAddDTO addDTO) {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return Result.error("请先登录");
        }

        UserComment comment = new UserComment();
        comment.setUserId(userId);
        comment.setType(1);
        comment.setTargetId(addDTO.getScenicId());
        comment.setContent(addDTO.getContent());
        comment.setIsAudit(1);
        commentService.save(comment);

        return Result.success("评论成功");
    }

    @GetMapping("/scenic/{scenicId}")
    public Result<List<CommentVO>> getCommentsByScenicId(@PathVariable Long scenicId) {
        List<CommentVO> voList = commentService.getCommentVOByScenicId(scenicId);
        return Result.success(voList);
    }
}
