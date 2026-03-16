package com.wit.travel.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wit.travel.dto.ScenicQueryDTO;
import com.wit.travel.entity.Scenic;
import com.wit.travel.mapper.ScenicMapper;
import com.wit.travel.service.ScenicService;
import com.wit.travel.vo.ScenicDetailVO;
import com.wit.travel.vo.ScenicVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 景点Service实现类
 */
@Service
public class ScenicServiceImpl extends ServiceImpl<ScenicMapper, Scenic> implements ScenicService {

    @Override
    public ScenicVO getScenicVOById(Long id) {
        return baseMapper.selectScenicVOById(id);
    }

    @Override
    public ScenicDetailVO getScenicDetailVOById(Long id) {
        return baseMapper.selectScenicDetailVOById(id);
    }

    @Override
    public IPage<ScenicVO> getScenicVOList(Page<ScenicVO> page, ScenicQueryDTO queryDTO) {
        return baseMapper.selectScenicVOList(page, queryDTO);
    }

    @Override
    public List<ScenicVO> getScenicVOByTagId(Long tagId) {
        return baseMapper.selectScenicVOByTagId(tagId);
    }

    @Override
    public List<ScenicVO> getScenicVOByCategoryId(Long categoryId) {
        return baseMapper.selectScenicVOByCategoryId(categoryId);
    }

    @Override
    public List<ScenicVO> getRecommendScenicVOList(Integer limit) {
        return baseMapper.selectRecommendScenicVOList(limit);
    }
}
