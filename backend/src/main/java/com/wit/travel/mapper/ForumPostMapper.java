package com.wit.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wit.travel.dto.ForumPostQueryDTO;
import com.wit.travel.entity.ForumPost;
import com.wit.travel.vo.ForumPostVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ForumPostMapper extends BaseMapper<ForumPost> {

    ForumPostVO selectForumPostVOById(@Param("id") Long id);

    IPage<ForumPostVO> selectForumPostVOList(Page<ForumPostVO> page, @Param("query") ForumPostQueryDTO queryDTO);

    List<ForumPostVO> selectRecommendForumPostVOList(@Param("limit") Integer limit);
}
