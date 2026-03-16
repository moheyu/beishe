package com.wit.travel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wit.travel.entity.UserComment;
import com.wit.travel.mapper.UserCommentMapper;
import com.wit.travel.service.UserCommentService;
import com.wit.travel.vo.CommentVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户评论Service实现类
 */
@Service
public class UserCommentServiceImpl extends ServiceImpl<UserCommentMapper, UserComment> implements UserCommentService {

    @Override
    public List<CommentVO> getCommentVOByScenicId(Long scenicId) {
        List<CommentVO> voList = baseMapper.selectCommentVOByScenicId(scenicId);
        voList.forEach(vo -> {
            if (vo.getAvatar() == null) {
                vo.setAvatar("");
            }
        });
        return voList;
    }

    @Override
    public List<CommentVO> getCommentVOByUserId(Long userId) {
        List<CommentVO> voList = baseMapper.selectCommentVOByUserId(userId);
        voList.forEach(vo -> {
            if (vo.getAvatar() == null) {
                vo.setAvatar("");
            }
        });
        return voList;
    }
}
