package com.wit.travel.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wit.travel.entity.Scenic;
import com.wit.travel.entity.UserCollection;
import com.wit.travel.mapper.UserCollectionMapper;
import com.wit.travel.service.ScenicService;
import com.wit.travel.service.UserCollectionService;
import com.wit.travel.util.SecurityUtil;
import com.wit.travel.vo.Result;
import com.wit.travel.vo.ScenicVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/{scenicId}")
    public Result<String> addCollection(@PathVariable Long scenicId) {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return Result.error("请先登录");
        }

        Scenic scenic = scenicService.getById(scenicId);
        if (scenic == null) {
            return Result.error("景点不存在");
        }

        if (collectionService.checkCollectionExists(userId, scenicId)) {
            return Result.error("已收藏该景点");
        }

        UserCollection collection = new UserCollection();
        collection.setUserId(userId);
        collection.setType(1);
        collection.setTargetId(scenicId);
        collectionService.save(collection);

        return Result.success("收藏成功");
    }

    @DeleteMapping("/{scenicId}")
    public Result<String> deleteCollection(@PathVariable Long scenicId) {
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

    @GetMapping("/list")
    public Result<List<ScenicVO>> getCollectionList() {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return Result.error("请先登录");
        }

        List<ScenicVO> voList = collectionService.getScenicVOByUserId(userId);
        return Result.success(voList);
    }
}
