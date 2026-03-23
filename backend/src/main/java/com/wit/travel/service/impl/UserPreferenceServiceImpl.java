package com.wit.travel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wit.travel.entity.UserPreference;
import com.wit.travel.mapper.UserPreferenceMapper;
import com.wit.travel.service.UserPreferenceService;
import com.wit.travel.vo.PreferenceDetailVO;
import com.wit.travel.vo.ScenicVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<ScenicVO> getRecommendByBrowseHistory(Long userId) {
        return baseMapper.selectRecommendByBrowseHistory(userId);
    }

    @Override
    public List<ScenicVO> getHybridRecommend(Long userId) {
        List<ScenicVO> preferenceRecommend = baseMapper.selectScenicVOByUserId(userId);
        List<ScenicVO> browseRecommend = baseMapper.selectRecommendByBrowseHistory(userId);

        List<ScenicVO> result = new ArrayList<>();

        int prefSize = preferenceRecommend.size();
        int browseSize = browseRecommend.size();
        int halfPref = prefSize / 2;
        int halfBrowse = browseSize / 2;

        for (int i = 0; i < Math.max(halfPref, halfBrowse); i++) {
            if (i < halfPref) {
                result.add(preferenceRecommend.get(i));
            }
            if (i < halfBrowse) {
                result.add(browseRecommend.get(i));
            }
        }

        List<Long> addedIds = result.stream().map(ScenicVO::getId).collect(Collectors.toList());

        for (ScenicVO scenic : preferenceRecommend) {
            if (!addedIds.contains(scenic.getId())) {
                result.add(scenic);
            }
        }
        for (ScenicVO scenic : browseRecommend) {
            if (!addedIds.contains(scenic.getId())) {
                result.add(scenic);
            }
        }

        return result;
    }

    @Override
    public List<Object> getPreferenceStatistics() {
        return baseMapper.selectPreferenceStatistics();
    }
}
