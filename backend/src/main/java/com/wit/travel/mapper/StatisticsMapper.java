package com.wit.travel.mapper;

import com.wit.travel.vo.ScenicStatisticsVO;
import com.wit.travel.vo.RouteStatisticsVO;
import com.wit.travel.vo.StatisticsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface StatisticsMapper {

    List<ScenicStatisticsVO> getTopScenicStatistics(@Param("limit") Integer limit);

    List<RouteStatisticsVO> getTopRouteStatistics(@Param("limit") Integer limit);

    StatisticsVO getOverallStatistics();

    Long getScenicCount();

    Long getRouteCount();

    Long getUserCount();

    Long getCollectionCount();

    Long getScenicTotalViews();

    Long getForumPostCount();

    Long getCommentCount();

    Long getTotalPreferences();

    Long getTotalViewRecords();

    List<Map<String, Object>> getPreferenceCategoryStats();
}
