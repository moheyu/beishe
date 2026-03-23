package com.wit.travel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wit.travel.entity.ViewRecord;
import com.wit.travel.mapper.ViewRecordMapper;
import com.wit.travel.service.ViewRecordService;
import com.wit.travel.vo.ScenicVO;
import org.springframework.stereotype.Service;

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
}
