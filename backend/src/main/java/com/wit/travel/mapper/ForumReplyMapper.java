package com.wit.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wit.travel.dto.ForumReplyQueryDTO;
import com.wit.travel.entity.ForumReply;
import com.wit.travel.vo.ForumReplyVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ForumReplyMapper extends BaseMapper<ForumReply> {

    IPage<ForumReplyVO> selectForumReplyVOList(Page<ForumReplyVO> page, @Param("query") ForumReplyQueryDTO queryDTO);
}
