package com.wit.travel.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wit.travel.dto.ScenicQueryDTO;
import com.wit.travel.entity.Scenic;
import com.wit.travel.mapper.ScenicMapper;
import com.wit.travel.service.ScenicService;
import com.wit.travel.service.UserRatingService;
import com.wit.travel.vo.ScenicDetailVO;
import com.wit.travel.vo.ScenicVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 景点Service实现类
 */
@Service
public class ScenicServiceImpl extends ServiceImpl<ScenicMapper, Scenic> implements ScenicService {

    @Autowired
    private UserRatingService userRatingService;

    @Override
    public ScenicVO getScenicVOById(Long id) {
        return baseMapper.selectScenicVOById(id);
    }

    @Override
    public ScenicDetailVO getScenicDetailVOById(Long id) {
        ScenicDetailVO detailVO = baseMapper.selectScenicDetailVOById(id);
        
        if (detailVO != null && detailVO.getAverageScore() != null) {
            // 评分保留一位小数
            detailVO.setAverageScore(Math.round(detailVO.getAverageScore() * 10) / 10.0);
        }
        
        return detailVO;
    }

    @Override
    public IPage<ScenicVO> getScenicVOList(Page<ScenicVO> page, ScenicQueryDTO queryDTO) {
        IPage<ScenicVO> voPage = baseMapper.selectScenicVOList(page, queryDTO);
        // 评分保留一位小数
        if (voPage != null && voPage.getRecords() != null) {
            for (ScenicVO vo : voPage.getRecords()) {
                if (vo.getAverageScore() != null) {
                    vo.setAverageScore(Math.round(vo.getAverageScore() * 10) / 10.0);
                }
            }
        }
        return voPage;
    }

    @Override
    public List<ScenicVO> getScenicVOByTagId(Long tagId) {
        List<ScenicVO> list = baseMapper.selectScenicVOByTagId(tagId);
        return roundScore(list);
    }

    @Override
    public List<ScenicVO> getScenicVOByCategoryId(Long categoryId) {
        List<ScenicVO> list = baseMapper.selectScenicVOByCategoryId(categoryId);
        return roundScore(list);
    }

    @Override
    public List<ScenicVO> getRecommendScenicVOList(Integer limit) {
        List<ScenicVO> list = baseMapper.selectRecommendScenicVOList(limit);
        return roundScore(list);
    }
    
    // 评分保留一位小数
    private List<ScenicVO> roundScore(List<ScenicVO> list) {
        if (list != null) {
            for (ScenicVO vo : list) {
                if (vo.getAverageScore() != null) {
                    vo.setAverageScore(Math.round(vo.getAverageScore() * 10) / 10.0);
                }
            }
        }
        return list;
    }

    @Override
    public boolean save(Scenic scenic) {
        return super.save(scenic);
    }

    @Override
    public boolean updateById(Scenic scenic) {
        return super.updateById(scenic);
    }

    @Override
    public boolean removeById(Long id) {
        return super.removeById(id);
    }
}
