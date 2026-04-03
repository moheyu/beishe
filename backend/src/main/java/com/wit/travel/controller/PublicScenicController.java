package com.wit.travel.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wit.travel.dto.ScenicQueryDTO;
import com.wit.travel.entity.ScenicCategory;
import com.wit.travel.service.ScenicCategoryService;
import com.wit.travel.service.ScenicService;
import com.wit.travel.vo.Result;
import com.wit.travel.vo.ScenicVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 景点控制器（公开接口，无需登录）
 */
@Slf4j
@RestController
@RequestMapping("/scenic")
public class PublicScenicController {

    @Autowired
    private ScenicService scenicService;

    @Autowired
    private ScenicCategoryService scenicCategoryService;

    /**
     * 获取景点列表（公开接口，支持分页和搜索）
     * @param queryDTO 查询参数
     * @return 景点列表
     */
    @GetMapping("/list")
    public Result<IPage<ScenicVO>> getScenicList(ScenicQueryDTO queryDTO) {
        Page<ScenicVO> page = new Page<>(queryDTO.getPageNum() != null ? queryDTO.getPageNum() : 1,
                queryDTO.getPageSize() != null ? queryDTO.getPageSize() : 12);
        IPage<ScenicVO> voPage = scenicService.getScenicVOList(page, queryDTO);
        return Result.success(voPage);
    }

    /**
     * 分页查询景点（公开接口）
     * @param queryDTO 查询参数
     * @return 景点列表
     */
    @GetMapping("/page")
    public Result<IPage<ScenicVO>> getScenicPage(ScenicQueryDTO queryDTO) {
        Page<ScenicVO> page = new Page<>(queryDTO.getPageNum() != null ? queryDTO.getPageNum() : 1,
                queryDTO.getPageSize() != null ? queryDTO.getPageSize() : 12);
        IPage<ScenicVO> voPage = scenicService.getScenicVOList(page, queryDTO);
        return Result.success(voPage);
    }

    /**
     * 获取推荐景点列表（公开接口）
     * @param limit 数量限制
     * @return 推荐景点列表
     */
    @GetMapping("/recommend")
    public Result<List<ScenicVO>> getRecommendScenic(@RequestParam(defaultValue = "10") Integer limit) {
        List<ScenicVO> voList = scenicService.getRecommendScenicVOList(limit);
        return Result.success(voList);
    }

    /**
     * 根据分类 ID 获取景点列表（公开接口）
     * @param categoryId 分类 ID
     * @return 景点列表
     */
    @GetMapping("/category/{categoryId}")
    public Result<List<ScenicVO>> getScenicByCategory(@PathVariable Long categoryId) {
        List<ScenicVO> voList = scenicService.getScenicVOByCategoryId(categoryId);
        return Result.success(voList);
    }

    /**
     * 根据标签 ID 获取景点列表（公开接口）
     * @param tagId 标签 ID
     * @return 景点列表
     */
    @GetMapping("/tag/{tagId}")
    public Result<List<ScenicVO>> getScenicByTag(@PathVariable Long tagId) {
        List<ScenicVO> voList = scenicService.getScenicVOByTagId(tagId);
        return Result.success(voList);
    }

    /**
     * 获取景点详情（公开接口）
     * @param id 景点 ID
     * @return 景点详情
     */
    @GetMapping("/{id}")
    public Result<ScenicVO> getScenicById(@PathVariable Long id) {
        ScenicVO vo = scenicService.getScenicVOById(id);
        if (vo == null) {
            return Result.error("景点不存在");
        }
        return Result.success(vo);
    }

    /**
     * 获取所有景点分类（公开接口）
     * @return 景点分类列表
     */
    @GetMapping("/categories")
    public Result<List<ScenicCategory>> getAllCategories() {
        QueryWrapper<ScenicCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");
        List<ScenicCategory> categories = scenicCategoryService.list(queryWrapper);
        return Result.success(categories);
    }
}
