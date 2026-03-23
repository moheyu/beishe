package com.wit.travel.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wit.travel.dto.PreferenceAddDTO;
import com.wit.travel.entity.UserPreference;
import com.wit.travel.mapper.UserPreferenceMapper;
import com.wit.travel.service.UserPreferenceService;
import com.wit.travel.util.SecurityUtil;
import com.wit.travel.vo.PreferenceDetailVO;
import com.wit.travel.vo.Result;
import com.wit.travel.vo.ScenicVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 偏好控制器
 */
@RestController
@RequestMapping("/preference")
public class PreferenceController {

    @Autowired
    private UserPreferenceService preferenceService;

    @Autowired
    private UserPreferenceMapper preferenceMapper;

    @PostMapping
    public Result<String> addPreference(@RequestBody PreferenceAddDTO addDTO) {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return Result.error("请先登录");
        }

        QueryWrapper<UserPreference> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        if (addDTO.getCategoryId() != null) {
            queryWrapper.eq("category_id", addDTO.getCategoryId());
        }
        if (addDTO.getTagId() != null) {
            queryWrapper.eq("tag_id", addDTO.getTagId());
        }
        UserPreference existPreference = preferenceMapper.selectOne(queryWrapper);
        
        if (existPreference != null) {
            return Result.error("该偏好已存在");
        }

        UserPreference preference = new UserPreference();
        preference.setUserId(userId);
        preference.setCategoryId(addDTO.getCategoryId());
        preference.setTagId(addDTO.getTagId());
        preferenceService.save(preference);

        return Result.success("添加成功");
    }

    @DeleteMapping("/{id}")
    public Result<String> deletePreference(@PathVariable Long id) {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return Result.error("请先登录");
        }

        UserPreference preference = preferenceService.getById(id);
        if (preference == null) {
            return Result.error("偏好不存在");
        }

        if (!preference.getUserId().equals(userId)) {
            return Result.error("无权删除");
        }

        preferenceService.removeById(id);
        return Result.success("删除成功");
    }

    @GetMapping("/list")
    public Result<List<PreferenceDetailVO>> getPreferenceList() {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return Result.error("请先登录");
        }

        List<PreferenceDetailVO> preferences = preferenceService.getPreferenceDetailByUserId(userId);
        return Result.success(preferences);
    }

    @GetMapping("/recommend")
    public Result<List<ScenicVO>> getRecommendScenic() {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return Result.error("请先登录");
        }

        List<ScenicVO> voList = preferenceService.getRecommendScenicByUserId(userId);
        return Result.success(voList);
    }

    @GetMapping("/recommend/hybrid")
    public Result<List<ScenicVO>> getHybridRecommend() {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return Result.error("请先登录");
        }

        List<ScenicVO> voList = preferenceService.getHybridRecommend(userId);
        return Result.success(voList);
    }

    @GetMapping("/user/{userId}")
    public Result<List<PreferenceDetailVO>> getUserPreferences(@PathVariable Long userId) {
        if (userId == null) {
            return Result.error("用户ID不能为空");
        }
        
        List<PreferenceDetailVO> preferences = preferenceService.getPreferenceDetailByUserId(userId);
        return Result.success(preferences);
    }

    @GetMapping("/statistics")
    public Result<List<Object>> getPreferenceStatistics() {
        List<Object> statistics = preferenceService.getPreferenceStatistics();
        return Result.success(statistics);
    }

    @PutMapping
    public Result<String> updateUserPreference(@RequestBody PreferenceAddDTO dto) {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return Result.error("请先登录");
        }

        // 先删除用户所有偏好，再添加新的
        QueryWrapper<UserPreference> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.eq("user_id", userId);
        preferenceMapper.delete(deleteWrapper);

        // 添加新的偏好
        if (dto.getCategoryId() != null) {
            UserPreference preference = new UserPreference();
            preference.setUserId(userId);
            preference.setCategoryId(dto.getCategoryId());
            if (dto.getTagId() != null) {
                preference.setTagId(dto.getTagId());
            }
            preferenceService.save(preference);
        }

        return Result.success("保存成功");
    }
}
