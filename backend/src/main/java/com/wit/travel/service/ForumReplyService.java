package com.wit.travel.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wit.travel.dto.ForumReplyQueryDTO;
import com.wit.travel.entity.ForumReply;
import com.wit.travel.vo.ForumReplyVO;

public interface ForumReplyService extends IService<ForumReply> {

    IPage<ForumReplyVO> getForumReplyVOList(Page<ForumReplyVO> page, ForumReplyQueryDTO queryDTO);
}
