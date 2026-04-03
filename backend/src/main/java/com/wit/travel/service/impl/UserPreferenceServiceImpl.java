package com.wit.travel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wit.travel.entity.UserPreference;
import com.wit.travel.mapper.UserPreferenceMapper;
import com.wit.travel.service.ScenicService;
import com.wit.travel.service.UserPreferenceService;
import com.wit.travel.vo.PreferenceDetailVO;
import com.wit.travel.vo.ScenicVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户偏好Service实现类
 */
@Service
public class UserPreferenceServiceImpl extends ServiceImpl<UserPreferenceMapper, UserPreference> implements UserPreferenceService {

    @Autowired
    private ScenicService scenicService;

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
    public List<ScenicVO> getHybridRecommend(Long userId, Integer limit) {
        List<ScenicVO> preferenceRecommend = baseMapper.selectScenicVOByUserId(userId);
        List<ScenicVO> browseRecommend = baseMapper.selectRecommendByBrowseHistory(userId);

        List<ScenicVO> result = new ArrayList<>();
        Set<Long> addedIds = new HashSet<>();

        // 1. 各取前半部分交叉合并
        int halfPref = preferenceRecommend.size() / 2;
        int halfBrowse = browseRecommend.size() / 2;

        int maxHalf = Math.max(halfPref, halfBrowse);
        for (int i = 0; i < maxHalf; i++) {
            if (i < halfPref) {
                ScenicVO prefScenic = preferenceRecommend.get(i);
                if (addedIds.add(prefScenic.getId())) {
                    result.add(prefScenic);
                }
            }
            if (i < halfBrowse) {
                ScenicVO browseScenic = browseRecommend.get(i);
                if (addedIds.add(browseScenic.getId())) {
                    result.add(browseScenic);
                }
            }
        }

        // 2. 补充剩余部分（去重）
        for (ScenicVO scenic : preferenceRecommend) {
            if (addedIds.add(scenic.getId())) {
                result.add(scenic);
            }
        }
        for (ScenicVO scenic : browseRecommend) {
            if (addedIds.add(scenic.getId())) {
                result.add(scenic);
            }
        }

        // 3. 如果还不够，从热点推荐中补充（去重）
        if (limit != null && result.size() < limit) {
            int remaining = limit - result.size();
            List<ScenicVO> hotScenics = scenicService.getRecommendScenicVOList(remaining * 2); // 多取一些用于去重
            for (ScenicVO scenic : hotScenics) {
                if (addedIds.add(scenic.getId())) {
                    result.add(scenic);
                    if (result.size() >= limit) {
                        break;
                    }
                }
            }
        }

        // 4. 最终限制数量
        if (limit != null && result.size() > limit) {
            return result.subList(0, limit);
        }
        return result;
    }

    @Override
    public List<Object> getPreferenceStatistics() {
        return baseMapper.selectPreferenceStatistics();
    }
}
