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
        comment.setContent(addDTO.getContent());
        comment.setIsAudit(1);

        if (addDTO.getScenicId() != null) {
            comment.setType(1);
            comment.setTargetId(addDTO.getScenicId());
        } else if (addDTO.getRouteId() != null) {
            comment.setType(2);
            comment.setTargetId(addDTO.getRouteId());
        } else {
            return Result.error("景点ID或路线ID不能为空");
        }

        commentService.save(comment);

        return Result.success("评论成功");
    }

    @GetMapping("/scenic/{scenicId}")
    public Result<List<CommentVO>> getCommentsByScenicId(@PathVariable Long scenicId) {
        List<CommentVO> voList = commentService.getCommentVOByScenicId(scenicId);
        return Result.success(voList);
    }

    @GetMapping("/route/{routeId}")
    public Result<List<CommentVO>> getCommentsByRouteId(@PathVariable Long routeId) {
        List<CommentVO> voList = commentService.getCommentVOByRouteId(routeId);
        return Result.success(voList);
    }

    @GetMapping("/user/list")
    public Result<List<CommentVO>> getUserCommentList() {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return Result.error("请先登录");
        }
        List<CommentVO> voList = commentService.getCommentVOByUserId(userId);
        return Result.success(voList);
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteComment(@PathVariable Long id) {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return Result.error("请先登录");
        }

        UserComment comment = commentService.getById(id);
        if (comment == null) {
            return Result.error("评论不存在");
        }

        // 检查是否是评论所有者或管理员
        if (!comment.getUserId().equals(userId)) {
            // TODO: 管理员权限检查
            return Result.error("无权删除此评论");
        }

        commentService.removeById(id);
        return Result.success("删除成功");
    }
}
