package com.wit.travel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wit.travel.entity.ViewRecord;
import com.wit.travel.vo.ScenicVO;

import java.util.List;

/**
 * 浏览记录Service接口
 */
public interface ViewRecordService extends IService<ViewRecord> {

    List<ScenicVO> getScenicVOByUserId(Long userId);

    void clearByUserId(Long userId);

    void upsertViewRecord(Long userId, Long scenicId);
}
