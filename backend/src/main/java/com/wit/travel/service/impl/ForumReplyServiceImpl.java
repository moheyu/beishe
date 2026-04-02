package com.wit.travel.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wit.travel.dto.ForumReplyQueryDTO;
import com.wit.travel.entity.ForumReply;
import com.wit.travel.mapper.ForumReplyMapper;
import com.wit.travel.service.ForumReplyService;
import com.wit.travel.vo.ForumReplyVO;
import org.springframework.stereotype.Service;

@Service
public class ForumReplyServiceImpl extends ServiceImpl<ForumReplyMapper, ForumReply> implements ForumReplyService {

    @Override
    public IPage<ForumReplyVO> getForumReplyVOList(Page<ForumReplyVO> page, ForumReplyQueryDTO queryDTO) {
        return baseMapper.selectForumReplyVOList(page, queryDTO);
    }
}
