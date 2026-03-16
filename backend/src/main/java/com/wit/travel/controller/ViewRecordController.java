package com.wit.travel.controller;

import com.wit.travel.entity.Scenic;
import com.wit.travel.entity.ViewRecord;
import com.wit.travel.service.ScenicService;
import com.wit.travel.service.ViewRecordService;
import com.wit.travel.util.SecurityUtil;
import com.wit.travel.vo.Result;
import com.wit.travel.vo.ScenicVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 浏览记录控制器
 */
@RestController
@RequestMapping("/view-record")
public class ViewRecordController {

    @Autowired
    private ViewRecordService viewRecordService;

    @Autowired
    private ScenicService scenicService;

    @PostMapping("/{scenicId}")
    public Result<String> addViewRecord(@PathVariable Long scenicId) {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return Result.error("请先登录");
        }

        Scenic scenic = scenicService.getById(scenicId);
        if (scenic == null) {
            return Result.error("景点不存在");
        }

        ViewRecord record = new ViewRecord();
        record.setUserId(userId);
        record.setScenicId(scenicId);
        viewRecordService.save(record);

        scenic.setViewCount(scenic.getViewCount() + 1);
        scenicService.updateById(scenic);

        return Result.success("记录成功");
    }

    @GetMapping("/list")
    public Result<List<ScenicVO>> getViewRecordList() {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return Result.error("请先登录");
        }

        List<ScenicVO> voList = viewRecordService.getScenicVOByUserId(userId);
        return Result.success(voList);
    }
}
