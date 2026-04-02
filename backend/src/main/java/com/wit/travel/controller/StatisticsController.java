package com.wit.travel.controller;

import com.wit.travel.service.StatisticsService;
import com.wit.travel.vo.ScenicStatisticsVO;
import com.wit.travel.vo.RouteStatisticsVO;
import com.wit.travel.vo.StatisticsVO;
import com.wit.travel.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据统计控制器（管理端）
 */
@Slf4j
@RestController
@RequestMapping("/admin/statistics")
@PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/scenic/top")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    public Result<List<ScenicStatisticsVO>> getTopScenicStatistics(@RequestParam(defaultValue = "10") Integer limit) {
        List<ScenicStatisticsVO> voList = statisticsService.getTopScenicStatistics(limit);
        return Result.success(voList);
    }

    @GetMapping("/route/top")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    public Result<List<RouteStatisticsVO>> getTopRouteStatistics(@RequestParam(defaultValue = "10") Integer limit) {
        List<RouteStatisticsVO> voList = statisticsService.getTopRouteStatistics(limit);
        return Result.success(voList);
    }

    @GetMapping("/overall")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    public Result<StatisticsVO> getOverallStatistics() {
        StatisticsVO vo = statisticsService.getOverallStatistics();
        return Result.success(vo);
    }

    @GetMapping("/recommendation")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    public Result<Map<String, Object>> getRecommendationStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalPreferences", statisticsService.getTotalPreferences());
        stats.put("totalViewRecords", statisticsService.getTotalViewRecords());
        stats.put("totalCollections", statisticsService.getTotalCollections());
        stats.put("preferenceCategoryStats", statisticsService.getPreferenceCategoryStats());
        
        return Result.success(stats);
    }
}
