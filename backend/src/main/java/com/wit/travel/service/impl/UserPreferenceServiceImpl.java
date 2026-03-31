package com.wit.travel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wit.travel.entity.UserPreference;
import com.wit.travel.mapper.UserPreferenceMapper;
import com.wit.travel.service.UserPreferenceService;
import com.wit.travel.vo.PreferenceDetailVO;
import com.wit.travel.vo.ScenicVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户偏好 Service 实现
 */
@Service
public class UserPreferenceServiceImpl extends ServiceImpl<UserPreferenceMapper, UserPreference>
        implements UserPreferenceService {

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

    /**
     * 混合推荐：交替合并偏好推荐与浏览历史推荐，使用 LinkedHashSet 保序去重，时间复杂度 O(n)。
     * <p>策略：先各取前半部分交替插入，再补充剩余未出现的条目。
     */
    @Override
    public List<ScenicVO> getHybridRecommend(Long userId) {
        List<ScenicVO> prefList = baseMapper.selectScenicVOByUserId(userId);
        List<ScenicVO> browseList = baseMapper.selectRecommendByBrowseHistory(userId);

        // LinkedHashSet 保证插入顺序且自动去重（依赖 ScenicVO.equals/hashCode by id）
        Set<Long> seenIds = new LinkedHashSet<>();
        List<ScenicVO> result = new ArrayList<>(prefList.size() + browseList.size());

        int halfPref = prefList.size() / 2;
        int halfBrowse = browseList.size() / 2;
        int interleaveCount = Math.max(halfPref, halfBrowse);

        // 第一阶段：交替插入各自前半部分
        for (int i = 0; i < interleaveCount; i++) {
            if (i < halfPref) {
                addIfAbsent(prefList.get(i), seenIds, result);
            }
            if (i < halfBrowse) {
                addIfAbsent(browseList.get(i), seenIds, result);
            }
        }

        // 第二阶段：补充剩余未出现的条目
        for (ScenicVO scenic : prefList) {
            addIfAbsent(scenic, seenIds, result);
        }
        for (ScenicVO scenic : browseList) {
            addIfAbsent(scenic, seenIds, result);
        }

        return result;
    }

    @Override
    public List<Object> getPreferenceStatistics() {
        return baseMapper.selectPreferenceStatistics();
    }

    private void addIfAbsent(ScenicVO scenic, Set<Long> seenIds, List<ScenicVO> result) {
        if (scenic.getId() != null && seenIds.add(scenic.getId())) {
            result.add(scenic);
        }
    }
}
