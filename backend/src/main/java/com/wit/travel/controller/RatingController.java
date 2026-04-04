package com.wit.travel.controller;

import com.wit.travel.entity.UserRating;
import com.wit.travel.service.UserRatingService;
import com.wit.travel.util.SecurityUtil;
import com.wit.travel.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rating")
public class RatingController {

    @Autowired
    private UserRatingService userRatingService;

    @PostMapping("/{scenicId}")
    public Result<String> rateScenic(@PathVariable Long scenicId, @RequestParam Integer score) {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return Result.error("请先登录");
        }

        if (score < 1 || score > 5) {
            return Result.error("评分范围为1-5分");
        }

        userRatingService.saveOrUpdateRating(userId, scenicId, score);
        return Result.success("评分成功");
    }

    @GetMapping("/{scenicId}")
    public Result<Map<String, Object>> getScenicRating(@PathVariable Long scenicId) {
        Long userId = SecurityUtil.getCurrentUserId();
        
        Double avgScore = userRatingService.getAverageScore(scenicId);
        Integer ratingCount = userRatingService.getRatingCount(scenicId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("averageScore", avgScore != null ? avgScore : 0.0);
        result.put("ratingCount", ratingCount != null ? ratingCount : 0);
        
        if (userId != null) {
            UserRating userRating = userRatingService.getUserRating(userId, scenicId);
            result.put("userScore", userRating != null ? userRating.getScore().intValue() : null);
        }
        
        return Result.success(result);
    }
}
