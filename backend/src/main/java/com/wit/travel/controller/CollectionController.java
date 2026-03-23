package com.wit.travel.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wit.travel.entity.Scenic;
import com.wit.travel.entity.TravelRoute;
import com.wit.travel.entity.UserCollection;
import com.wit.travel.mapper.UserCollectionMapper;
import com.wit.travel.service.RouteService;
import com.wit.travel.service.ScenicService;
import com.wit.travel.service.UserCollectionService;
import com.wit.travel.util.SecurityUtil;
import com.wit.travel.vo.Result;
import com.wit.travel.vo.RouteVO;
import com.wit.travel.vo.ScenicVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 收藏控制器
 */
@RestController
@RequestMapping("/collection")
public class CollectionController {

    @Autowired
    private UserCollectionService collectionService;

    @Autowired
    private UserCollectionMapper collectionMapper;

    @Autowired
    private ScenicService scenicService;

    @Autowired
    private RouteService routeService;

    @PostMapping("/scenic/{scenicId}")
    public Result<String> addScenicCollection(@PathVariable Long scenicId) {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return Result.error("请先登录");
        }

        Scenic scenic = scenicService.getById(scenicId);
        if (scenic == null) {
            return Result.error("景点不存在");
        }

        UserCollection collection = new UserCollection();
        collection.setUserId(userId);
        collection.setType(1);
        collection.setTargetId(scenicId);
        collectionService.save(collection);

        return Result.success("收藏成功");
    }

    @PostMapping("/route/{routeId}")
    public Result<String> addRouteCollection(@PathVariable Long routeId) {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return Result.error("请先登录");
        }

        TravelRoute route = routeService.getById(routeId);
        if (route == null) {
            return Result.error("路线不存在");
        }

        UserCollection collection = new UserCollection();
        collection.setUserId(userId);
        collection.setType(2);
        collection.setTargetId(routeId);
        collectionService.save(collection);

        return Result.success("收藏成功");
    }

    @DeleteMapping("/scenic/{scenicId}")
    public Result<String> deleteScenicCollection(@PathVariable Long scenicId) {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return Result.error("请先登录");
        }

        QueryWrapper<UserCollection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("type", 1);
        queryWrapper.eq("target_id", scenicId);
        UserCollection collection = collectionMapper.selectOne(queryWrapper);

        if (collection != null) {
            collectionService.removeById(collection.getId());
        }

        return Result.success("取消收藏成功");
    }

    @DeleteMapping("/route/{routeId}")
    public Result<String> deleteRouteCollection(@PathVariable Long routeId) {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return Result.error("请先登录");
        }

        QueryWrapper<UserCollection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("type", 2);
        queryWrapper.eq("target_id", routeId);
        UserCollection collection = collectionMapper.selectOne(queryWrapper);

        if (collection != null) {
            collectionService.removeById(collection.getId());
        }

        return Result.success("取消收藏成功");
    }

    @GetMapping("/list")
    public Result<Map<String, Object>> getCollectionList() {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return Result.error("请先登录");
        }

        List<ScenicVO> scenicList = collectionService.getScenicVOByUserId(userId);
        List<RouteVO> routeList = collectionService.getRouteVOByUserId(userId);

        Map<String, Object> result = new HashMap<>();
        result.put("scenic", scenicList != null ? scenicList : new ArrayList<>());
        result.put("route", routeList != null ? routeList : new ArrayList<>());

        return Result.success(result);
    }

    @GetMapping("/user/{userId}")
    public Result<List<UserCollection>> getUserCollections(@PathVariable Long userId) {
        if (userId == null) {
            return Result.error("用户ID不能为空");
        }

        QueryWrapper<UserCollection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("create_time");
        List<UserCollection> collections = collectionMapper.selectList(queryWrapper);

        return Result.success(collections);
    }
}
