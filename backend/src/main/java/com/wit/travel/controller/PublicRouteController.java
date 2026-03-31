package com.wit.travel.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wit.travel.dto.RouteQueryDTO;
import com.wit.travel.entity.RouteCategory;
import com.wit.travel.service.RouteCategoryService;
import com.wit.travel.service.RouteService;
import com.wit.travel.vo.Result;
import com.wit.travel.vo.RouteDetailVO;
import com.wit.travel.vo.RouteVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 旅游路线控制器（公开接口，无需登录）
 */
@Slf4j
@RestController
@RequestMapping("/route")
public class PublicRouteController {

    @Autowired
    private RouteService routeService;

    @Autowired
    private RouteCategoryService routeCategoryService;

    /**
     * 获取路线列表（公开接口，支持分页和搜索）
     * @param queryDTO 查询参数
     * @return 路线列表
     */
    @GetMapping("/list")
    public Result<IPage<RouteVO>> getRouteList(RouteQueryDTO queryDTO) {
        Page<RouteVO> page = new Page<>(queryDTO.getPageNum() != null ? queryDTO.getPageNum() : 1,
                queryDTO.getPageSize() != null ? queryDTO.getPageSize() : 10);
        IPage<RouteVO> voPage = routeService.getRouteVOList(page, queryDTO);
        return Result.success(voPage);
    }

    /**
     * 分页查询路线（公开接口）
     * @param queryDTO 查询参数
     * @return 路线列表
     */
    @GetMapping("/page")
    public Result<IPage<RouteVO>> getRoutePage(RouteQueryDTO queryDTO) {
        Page<RouteVO> page = new Page<>(queryDTO.getPageNum() != null ? queryDTO.getPageNum() : 1,
                queryDTO.getPageSize() != null ? queryDTO.getPageSize() : 10);
        IPage<RouteVO> voPage = routeService.getRouteVOList(page, queryDTO);
        return Result.success(voPage);
    }

    /**
     * 获取推荐路线列表（公开接口）
     * @param limit 数量限制
     * @return 推荐路线列表
     */
    @GetMapping("/recommend")
    public Result<List<RouteVO>> getRecommendRoute(@RequestParam(defaultValue = "10") Integer limit) {
        List<RouteVO> voList = routeService.getRecommendRouteVOList(limit);
        return Result.success(voList);
    }

    /**
     * 根据分类 ID 获取路线列表（公开接口）
     * @param categoryId 分类 ID
     * @return 路线列表
     */
    @GetMapping("/category/{categoryId}")
    public Result<List<RouteVO>> getRouteByCategory(@PathVariable Long categoryId) {
        List<RouteVO> voList = routeService.getRouteVOByCategoryId(categoryId);
        return Result.success(voList);
    }

    /**
     * 获取路线详情（公开接口）
     * @param id 路线 ID
     * @return 路线详情
     */
    @GetMapping("/{id}")
    public Result<RouteVO> getRouteById(@PathVariable Long id) {
        RouteVO vo = routeService.getRouteVOById(id);
        if (vo == null) {
            return Result.error("路线不存在");
        }
        return Result.success(vo);
    }

    /**
     * 获取路线详细信息（公开接口）
     * @param id 路线 ID
     * @return 路线详细信息
     */
    @GetMapping("/detail/{id}")
    public Result<RouteDetailVO> getRouteDetailById(@PathVariable Long id) {
        RouteDetailVO vo = routeService.getRouteDetailVOById(id);
        if (vo == null) {
            return Result.error("路线不存在");
        }
        return Result.success(vo);
    }

    /**
     * 获取所有路线分类（公开接口）
     * @return 路线分类列表
     */
    @GetMapping("/categories")
    public Result<List<RouteCategory>> getAllCategories() {
        QueryWrapper<RouteCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");
        List<RouteCategory> categories = routeCategoryService.list(queryWrapper);
        return Result.success(categories);
    }
}
