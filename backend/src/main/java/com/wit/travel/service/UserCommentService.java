package com.wit.travel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wit.travel.entity.UserComment;
import com.wit.travel.vo.CommentVO;

import java.util.List;

/**
 * 用户评论Service接口
 */
public interface UserCommentService extends IService<UserComment> {

    List<CommentVO> getCommentVOByScenicId(Long scenicId);

    List<CommentVO> getCommentVOByRouteId(Long routeId);

    List<CommentVO> getCommentVOByUserId(Long userId);
}
