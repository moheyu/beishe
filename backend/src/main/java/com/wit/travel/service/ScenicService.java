package com.wit.travel.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wit.travel.dto.ScenicQueryDTO;
import com.wit.travel.entity.Scenic;
import com.wit.travel.vo.ScenicDetailVO;
import com.wit.travel.vo.ScenicVO;

import java.util.List;

/**
 * 景点Service接口
 */
public interface ScenicService extends IService<Scenic> {

    ScenicVO getScenicVOById(Long id);

    ScenicDetailVO getScenicDetailVOById(Long id);

    IPage<ScenicVO> getScenicVOList(Page<ScenicVO> page, ScenicQueryDTO queryDTO);

    List<ScenicVO> getScenicVOByTagId(Long tagId);

    List<ScenicVO> getScenicVOByCategoryId(Long categoryId);

    List<ScenicVO> getRecommendScenicVOList(Integer limit);

    boolean save(Scenic scenic);

    boolean updateById(Scenic scenic);

    boolean removeById(Long id);
}
