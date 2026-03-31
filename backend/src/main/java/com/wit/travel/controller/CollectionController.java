package com.wit.travel.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wit.travel.entity.UserCollection;
import com.wit.travel.service.RouteService;
import com.wit.travel.service.ScenicService;
import com.wit.travel.service.UserCollectionService;
import com.wit.travel.util.SecurityUtil;
import com.wit.travel.vo.Result;
import com.wit.travel.vo.RouteVO;
import com.wit.travel.vo.ScenicVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 收藏控制器
 */
@Slf4j
@RestController
@RequestMapping("/collection")
public class CollectionController {

    /** 收藏类型：景点 */
    private static final int TYPE_SCENIC = 1;
    /** 收藏类型：路线 */
    private static final int TYPE_ROUTE = 2;

    @Autowired
    private UserCollectionService collectionService;

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
        if (scenicService.getById(scenicId) == null) {
            return Result.error("景点不存在");
        }
        if (collectionService.checkCollectionExists(userId, scenicId)) {
            return Result.error("已收藏该景点");
        }
        collectionService.save(buildCollection(userId, TYPE_SCENIC, scenicId));
        return Result.success("收藏成功");
    }

    @PostMapping("/route/{routeId}")
    public Result<String> addRouteCollection(@PathVariable Long routeId) {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return Result.error("请先登录");
        }
        if (routeService.getById(routeId) == null) {
            return Result.error("路线不存在");
        }
        // 复用 checkCollectionExists 逻辑（type=2 路线）
        boolean exists = collectionService.lambdaQuery()
                .eq(UserCollection::getUserId, userId)
                .eq(UserCollection::getType, TYPE_ROUTE)
                .eq(UserCollection::getTargetId, routeId)
                .exists();
        if (exists) {
            return Result.error("已收藏该路线");
        }
        collectionService.save(buildCollection(userId, TYPE_ROUTE, routeId));
        return Result.success("收藏成功");
    }

    @DeleteMapping("/scenic/{scenicId}")
    public Result<String> deleteScenicCollection(@PathVariable Long scenicId) {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return Result.error("请先登录");
        }
        collectionService.remove(new LambdaQueryWrapper<UserCollection>()
                .eq(UserCollection::getUserId, userId)
                .eq(UserCollection::getType, TYPE_SCENIC)
                .eq(UserCollection::getTargetId, scenicId));
        return Result.success("取消收藏成功");
    }

    @DeleteMapping("/route/{routeId}")
    public Result<String> deleteRouteCollection(@PathVariable Long routeId) {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return Result.error("请先登录");
        }
        collectionService.remove(new LambdaQueryWrapper<UserCollection>()
                .eq(UserCollection::getUserId, userId)
                .eq(UserCollection::getType, TYPE_ROUTE)
                .eq(UserCollection::getTargetId, routeId));
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

        Map<String, Object> result = new HashMap<>(4);
        result.put("scenic", scenicList != null ? scenicList : Collections.emptyList());
        result.put("route", routeList != null ? routeList : Collections.emptyList());
        return Result.success(result);
    }

    @GetMapping("/user/{userId}")
    public Result<List<UserCollection>> getUserCollections(@PathVariable Long userId) {
        List<UserCollection> collections = collectionService.lambdaQuery()
                .eq(UserCollection::getUserId, userId)
                .orderByDesc(UserCollection::getCreateTime)
                .list();
        return Result.success(collections);
    }

    private UserCollection buildCollection(Long userId, int type, Long targetId) {
        UserCollection collection = new UserCollection();
        collection.setUserId(userId);
        collection.setType(type);
        collection.setTargetId(targetId);
        return collection;
    }
}
