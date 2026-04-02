package com.wit.travel.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsVO {

    private Integer totalScenics;
    private Integer totalRoutes;
    private Integer totalUsers;
    private Integer totalPosts;
    private Integer totalComments;
    private Integer totalViews;
    private Integer totalCollections;
    private Integer activeUsers;
    private Integer activePostsToday;
    private Integer activeUsersWeek;
    private Double avgDailyViews;
    private Integer topScenicId;
    private String topScenicName;
    private Integer topRouteId;
    private String topRouteName;
}
