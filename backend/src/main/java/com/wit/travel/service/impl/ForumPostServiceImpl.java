package com.wit.travel.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wit.travel.dto.ForumPostQueryDTO;
import com.wit.travel.entity.ForumPost;
import com.wit.travel.mapper.ForumPostMapper;
import com.wit.travel.service.ForumPostService;
import com.wit.travel.vo.ForumPostVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForumPostServiceImpl extends ServiceImpl<ForumPostMapper, ForumPost> implements ForumPostService {

    @Override
    public ForumPostVO getForumPostVOById(Long id) {
        return baseMapper.selectForumPostVOById(id);
    }

    @Override
    public IPage<ForumPostVO> getForumPostVOList(Page<ForumPostVO> page, ForumPostQueryDTO queryDTO) {
        return baseMapper.selectForumPostVOList(page, queryDTO);
    }

    @Override
    public List<ForumPostVO> getRecommendForumPostVOList(Integer limit) {
        return baseMapper.selectRecommendForumPostVOList(limit);
    }

    @Override
    public boolean save(ForumPost forumPost) {
        return super.save(forumPost);
    }

    @Override
    public boolean updateById(ForumPost forumPost) {
        return super.updateById(forumPost);
    }

    @Override
    public boolean removeById(Long id) {
        return super.removeById(id);
    }
}
