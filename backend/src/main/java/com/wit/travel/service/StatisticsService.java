package com.wit.travel.service;

import com.wit.travel.vo.ScenicStatisticsVO;
import com.wit.travel.vo.RouteStatisticsVO;
import com.wit.travel.vo.StatisticsVO;

import java.util.List;
import java.util.Map;

public interface StatisticsService {

    List<ScenicStatisticsVO> getTopScenicStatistics(Integer limit);

    List<RouteStatisticsVO> getTopRouteStatistics(Integer limit);

    StatisticsVO getOverallStatistics();

    Long getTotalPreferences();

    Long getTotalViewRecords();

    Long getTotalCollections();

    List<Map<String, Object>> getPreferenceCategoryStats();
}
