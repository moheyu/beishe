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

        // 保存分类偏好
        if (addDTO.getCategoryIds() != null && !addDTO.getCategoryIds().isEmpty()) {
            for (Long categoryId : addDTO.getCategoryIds()) {
                QueryWrapper<UserPreference> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("user_id", userId).eq("category_id", categoryId);
                UserPreference existPreference = preferenceMapper.selectOne(queryWrapper);
                
                if (existPreference != null) {
                    continue;
                }

                UserPreference preference = new UserPreference();
                preference.setUserId(userId);
                preference.setCategoryId(categoryId);
                if (addDTO.getTagId() != null) {
                    preference.setTagId(addDTO.getTagId());
                }
                preferenceService.save(preference);
            }
        }

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
    public Result<List<ScenicVO>> getRecommendScenic(@RequestParam(defaultValue = "10") Integer limit) {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return Result.error("请先登录");
        }

        List<ScenicVO> voList = preferenceService.getRecommendScenicByUserId(userId);
        if (limit != null && voList.size() > limit) {
            voList = voList.subList(0, limit);
        }
        return Result.success(voList);
    }

    @GetMapping("/recommend/hybrid")
    public Result<List<ScenicVO>> getHybridRecommend(@RequestParam(defaultValue = "10") Integer limit) {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return Result.error("请先登录");
        }

        List<ScenicVO> voList = preferenceService.getHybridRecommend(userId, limit);
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

        // 保存预算和出行季节（通用偏好，只保存一条）
        UserPreference commonPref = new UserPreference();
        commonPref.setUserId(userId);
        commonPref.setBudget(dto.getBudget());
        commonPref.setSeason(dto.getSeason());
        preferenceService.save(commonPref);

        // 保存分类偏好（支持多选）
        if (dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()) {
            for (Long categoryId : dto.getCategoryIds()) {
                UserPreference preference = new UserPreference();
                preference.setUserId(userId);
                preference.setCategoryId(categoryId);
                if (dto.getTagId() != null) {
                    preference.setTagId(dto.getTagId());
                }
                preferenceService.save(preference);
            }
        }

        return Result.success("保存成功");
    }
}
