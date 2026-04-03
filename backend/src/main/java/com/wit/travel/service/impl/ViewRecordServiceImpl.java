package com.wit.travel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wit.travel.entity.ViewRecord;
import com.wit.travel.mapper.ViewRecordMapper;
import com.wit.travel.service.ViewRecordService;
import com.wit.travel.vo.ScenicVO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 浏览记录Service实现类
 */
@Service
public class ViewRecordServiceImpl extends ServiceImpl<ViewRecordMapper, ViewRecord> implements ViewRecordService {

    @Override
    public List<ScenicVO> getScenicVOByUserId(Long userId) {
        return baseMapper.selectScenicVOByUserId(userId);
    }

    @Override
    public void clearByUserId(Long userId) {
        QueryWrapper<ViewRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        baseMapper.delete(wrapper);
    }

    @Override
    public void upsertViewRecord(Long userId, Long scenicId) {
        // 查询是否已存在该用户对该景点的浏览记录
        QueryWrapper<ViewRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("scenic_id", scenicId);
        ViewRecord existing = baseMapper.selectOne(wrapper);

        if (existing != null) {
            // 存在则更新浏览时间
            existing.setViewTime(LocalDateTime.now());
            baseMapper.updateById(existing);
        } else {
            // 不存在则插入新记录
            ViewRecord record = new ViewRecord();
            record.setUserId(userId);
            record.setScenicId(scenicId);
            record.setViewTime(LocalDateTime.now());
            baseMapper.insert(record);
        }

        // 检查并清理超过18条的浏览记录（保留最新的18条）
        cleanupOldRecords(userId);
    }

    /**
     * 清理超过18条的浏览记录，只保留最新的18条
     */
    private void cleanupOldRecords(Long userId) {
        // 先查询该用户的浏览记录总数
        Long total = baseMapper.selectTotalViewsByUserId(userId);
        if (total != null && total > 18) {
            // 需要删除的条数
            int deleteCount = total.intValue() - 18;

            // 查询最旧的deleteCount条记录
            List<Long> oldIds = baseMapper.selectOldestRecordIds(userId, deleteCount);
            if (oldIds != null && !oldIds.isEmpty()) {
                // 批量删除
                baseMapper.deleteBatchIds(oldIds);
            }
        }
    }
}
