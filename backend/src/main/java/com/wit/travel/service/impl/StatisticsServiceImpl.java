package com.wit.travel.service.impl;

import com.wit.travel.mapper.StatisticsMapper;
import com.wit.travel.service.StatisticsService;
import com.wit.travel.vo.ScenicStatisticsVO;
import com.wit.travel.vo.RouteStatisticsVO;
import com.wit.travel.vo.StatisticsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private StatisticsMapper statisticsMapper;

    @Override
    public List<ScenicStatisticsVO> getTopScenicStatistics(Integer limit) {
        return statisticsMapper.getTopScenicStatistics(limit);
    }

    @Override
    public List<RouteStatisticsVO> getTopRouteStatistics(Integer limit) {
        return statisticsMapper.getTopRouteStatistics(limit);
    }

    @Override
    public StatisticsVO getOverallStatistics() {
        StatisticsVO vo = new StatisticsVO();
        try {
            Long scenicCount = statisticsMapper.getScenicCount();
            vo.setTotalScenics(scenicCount != null ? scenicCount.intValue() : 0);
        } catch (Exception e) {
            vo.setTotalScenics(0);
        }
        
        try {
            Long routeCount = statisticsMapper.getRouteCount();
            vo.setTotalRoutes(routeCount != null ? routeCount.intValue() : 0);
        } catch (Exception e) {
            vo.setTotalRoutes(0);
        }
        
        try {
            Long userCount = statisticsMapper.getUserCount();
            vo.setTotalUsers(userCount != null ? userCount.intValue() : 0);
        } catch (Exception e) {
            vo.setTotalUsers(0);
        }
        
        try {
            Long collectionCount = statisticsMapper.getCollectionCount();
            vo.setTotalCollections(collectionCount != null ? collectionCount.intValue() : 0);
        } catch (Exception e) {
            vo.setTotalCollections(0);
        }
        
        try {
            Long scenicViews = statisticsMapper.getScenicTotalViews();
            vo.setTotalViews(scenicViews != null ? scenicViews.intValue() : 0);
        } catch (Exception e) {
            vo.setTotalViews(0);
        }

        try {
            Long postCount = statisticsMapper.getForumPostCount();
            vo.setTotalPosts(postCount != null ? postCount.intValue() : 0);
        } catch (Exception e) {
            vo.setTotalPosts(0);
        }

        try {
            Long commentCount = statisticsMapper.getCommentCount();
            vo.setTotalComments(commentCount != null ? commentCount.intValue() : 0);
        } catch (Exception e) {
            vo.setTotalComments(0);
        }

        vo.setActiveUsers(0);
        vo.setActivePostsToday(0);
        vo.setActiveUsersWeek(0);
        vo.setAvgDailyViews(0.0);
        vo.setTopScenicId(null);
        vo.setTopScenicName(null);
        vo.setTopRouteId(null);
        vo.setTopRouteName(null);

        return vo;
    }

    @Override
    public Long getTotalPreferences() {
        try {
            return statisticsMapper.getTotalPreferences();
        } catch (Exception e) {
            return 0L;
        }
    }

    @Override
    public Long getTotalViewRecords() {
        try {
            return statisticsMapper.getTotalViewRecords();
        } catch (Exception e) {
            return 0L;
        }
    }

    @Override
    public Long getTotalCollections() {
        try {
            return statisticsMapper.getCollectionCount();
        } catch (Exception e) {
            return 0L;
        }
    }

    @Override
    public List<Map<String, Object>> getPreferenceCategoryStats() {
        try {
            return statisticsMapper.getPreferenceCategoryStats();
        } catch (Exception e) {
            return null;
        }
    }
}
