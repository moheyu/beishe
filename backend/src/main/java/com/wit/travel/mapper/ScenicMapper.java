package com.wit.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wit.travel.dto.ScenicQueryDTO;
import com.wit.travel.entity.Scenic;
import com.wit.travel.vo.ScenicDetailVO;
import com.wit.travel.vo.ScenicVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 景点Mapper接口
 */
@Mapper
public interface ScenicMapper extends BaseMapper<Scenic> {

    ScenicVO selectScenicVOById(@Param("id") Long id);

    ScenicDetailVO selectScenicDetailVOById(@Param("id") Long id);

    IPage<ScenicVO> selectScenicVOList(Page<ScenicVO> page, @Param("query") ScenicQueryDTO queryDTO);

    List<ScenicVO> selectScenicVOByTagId(@Param("tagId") Long tagId);

    List<ScenicVO> selectScenicVOByCategoryId(@Param("categoryId") Long categoryId);

    List<ScenicVO> selectRecommendScenicVOList(@Param("limit") Integer limit);

    List<ScenicVO> selectHotScenicList(@Param("limit") Integer limit);
}
