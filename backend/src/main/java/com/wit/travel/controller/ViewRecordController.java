package com.wit.travel.controller;

import com.wit.travel.entity.ViewRecord;
import com.wit.travel.service.ScenicService;
import com.wit.travel.service.ViewRecordService;
import com.wit.travel.util.SecurityUtil;
import com.wit.travel.vo.Result;
import com.wit.travel.vo.ScenicVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 浏览记录控制器
 */
@Slf4j
@RestController
@RequestMapping("/view-record")
public class ViewRecordController {

    @Autowired
    private ViewRecordService viewRecordService;

    @Autowired
    private ScenicService scenicService;

    /**
     * 记录用户浏览景点，并原子递增景点浏览量。
     */
    @PostMapping("/{scenicId}")
    public Result<String> addViewRecord(@PathVariable Long scenicId) {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return Result.error("请先登录");
        }
        if (scenicService.getById(scenicId) == null) {
            return Result.error("景点不存在");
        }

        ViewRecord record = new ViewRecord();
        record.setUserId(userId);
        record.setScenicId(scenicId);
        viewRecordService.save(record);

        // 使用 SQL 原子递增，避免并发场景下的计数丢失
        scenicService.incrementViewCount(scenicId);

        return Result.success("记录成功");
    }

    @GetMapping("/list")
    public Result<List<ScenicVO>> getViewRecordList() {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return Result.error("请先登录");
        }
        return Result.success(viewRecordService.getScenicVOByUserId(userId));
    }

    @DeleteMapping("/clear")
    public Result<String> clearViewRecord() {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return Result.error("请先登录");
        }
        viewRecordService.clearByUserId(userId);
        return Result.success("清空成功");
    }
}
