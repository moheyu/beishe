package com.wit.travel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wit.travel.entity.UserRating;
import com.wit.travel.mapper.UserRatingMapper;
import com.wit.travel.service.UserRatingService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserRatingServiceImpl extends ServiceImpl<UserRatingMapper, UserRating> implements UserRatingService {

    @Override
    public void saveOrUpdateRating(Long userId, Long scenicId, Integer score) {
        QueryWrapper<UserRating> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("scenic_id", scenicId);
        UserRating existing = baseMapper.selectOne(wrapper);

        if (existing != null) {
            existing.setScore(BigDecimal.valueOf(score));
            baseMapper.updateById(existing);
        } else {
            UserRating rating = new UserRating();
            rating.setUserId(userId);
            rating.setScenicId(scenicId);
            rating.setScore(BigDecimal.valueOf(score));
            baseMapper.insert(rating);
        }
    }

    @Override
    public Double getAverageScore(Long scenicId) {
        BigDecimal avg = baseMapper.selectAverageScoreByScenicId(scenicId);
        return avg != null ? avg.doubleValue() : null;
    }

    @Override
    public Integer getRatingCount(Long scenicId) {
        return baseMapper.selectRatingCountByScenicId(scenicId);
    }

    @Override
    public UserRating getUserRating(Long userId, Long scenicId) {
        QueryWrapper<UserRating> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("scenic_id", scenicId);
        return baseMapper.selectOne(wrapper);
    }
}
