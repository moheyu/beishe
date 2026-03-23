package com.wit.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wit.travel.entity.UserComment;
import com.wit.travel.vo.CommentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户评论Mapper接口
 */
@Mapper
public interface UserCommentMapper extends BaseMapper<UserComment> {

    List<CommentVO> selectCommentVOByScenicId(@Param("scenicId") Long scenicId);

    List<CommentVO> selectCommentVOByRouteId(@Param("routeId") Long routeId);

    List<CommentVO> selectCommentVOByUserId(@Param("userId") Long userId);
}
