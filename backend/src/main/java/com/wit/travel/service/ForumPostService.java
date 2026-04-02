package com.wit.travel.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wit.travel.dto.ForumPostQueryDTO;
import com.wit.travel.entity.ForumPost;
import com.wit.travel.vo.ForumPostVO;

import java.util.List;

public interface ForumPostService extends IService<ForumPost> {

    ForumPostVO getForumPostVOById(Long id);

    IPage<ForumPostVO> getForumPostVOList(Page<ForumPostVO> page, ForumPostQueryDTO queryDTO);

    List<ForumPostVO> getRecommendForumPostVOList(Integer limit);

    boolean save(ForumPost forumPost);

    boolean updateById(ForumPost forumPost);

    boolean removeById(Long id);
}
