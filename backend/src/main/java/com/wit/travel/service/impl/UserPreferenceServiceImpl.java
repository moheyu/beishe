package com.wit.travel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wit.travel.entity.UserPreference;
import com.wit.travel.mapper.UserPreferenceMapper;
import com.wit.travel.service.UserPreferenceService;
import com.wit.travel.vo.PreferenceDetailVO;
import com.wit.travel.vo.ScenicVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户偏好Service实现类
 */
@Service
public class UserPreferenceServiceImpl extends ServiceImpl<UserPreferenceMapper, UserPreference> implements UserPreferenceService {

    @Override
    public List<PreferenceDetailVO> getPreferenceDetailByUserId(Long userId) {
        return baseMapper.selectPreferenceDetailByUserId(userId);
    }

    @Override
    public List<ScenicVO> getRecommendScenicByUserId(Long userId) {
        return baseMapper.selectScenicVOByUserId(userId);
    }
}
