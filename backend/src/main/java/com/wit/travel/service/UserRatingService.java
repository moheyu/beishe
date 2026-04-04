package com.wit.travel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wit.travel.entity.UserRating;

public interface UserRatingService extends IService<UserRating> {

    void saveOrUpdateRating(Long userId, Long scenicId, Integer score);

    Double getAverageScore(Long scenicId);

    Integer getRatingCount(Long scenicId);

    UserRating getUserRating(Long userId, Long scenicId);
}
