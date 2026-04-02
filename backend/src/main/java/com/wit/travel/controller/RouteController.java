package com.wit.travel.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wit.travel.dto.RouteAddDTO;
import com.wit.travel.dto.RouteQueryDTO;
import com.wit.travel.entity.RouteCategory;
import com.wit.travel.entity.RouteScenicRelation;
import com.wit.travel.entity.TravelRoute;
import com.wit.travel.service.RouteCategoryService;
import com.wit.travel.service.RouteScenicRelationService;
import com.wit.travel.service.RouteService;
import com.wit.travel.vo.Result;
import com.wit.travel.vo.RouteDetailVO;
import com.wit.travel.vo.RouteVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 旅游路线控制器（管理端）
 */
@Slf4j
@RestController
@RequestMapping("/admin/route")
@PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RouteCategoryService routeCategoryService;

    @Autowired
    private RouteScenicRelationService routeScenicRelationService;

    @GetMapping("/list")
    public Result<IPage<RouteVO>> getRouteList(RouteQueryDTO queryDTO) {
        Page<RouteVO> page = new Page<>(queryDTO.getPageNum() != null ? queryDTO.getPageNum() : 1,
                queryDTO.getPageSize() != null ? queryDTO.getPageSize() : 10);
        IPage<RouteVO> voPage = routeService.getRouteVOList(page, queryDTO);
        return Result.success(voPage);
    }

    // 分页查询接口 - 与/list功能相同，为兼容不同前端调用方式
    @GetMapping("/page")
    public Result<IPage<RouteVO>> getRoutePage(RouteQueryDTO queryDTO) {
        Page<RouteVO> page = new Page<>(queryDTO.getPageNum() != null ? queryDTO.getPageNum() : 1,
                queryDTO.getPageSize() != null ? queryDTO.getPageSize() : 10);
        IPage<RouteVO> voPage = routeService.getRouteVOList(page, queryDTO);
        return Result.success(voPage);
    }

    @GetMapping("/{id}")
    public Result<RouteVO> getRouteById(@PathVariable Long id) {
        RouteVO vo = routeService.getRouteVOById(id);
        if (vo == null) {
            return Result.error("路线不存在");
        }
        return Result.success(vo);
    }

    @GetMapping("/detail/{id}")
    public Result<RouteDetailVO> getRouteDetailById(@PathVariable Long id) {
        RouteDetailVO vo = routeService.getRouteDetailVOById(id);
        if (vo == null) {
            return Result.error("路线不存在");
        }
        return Result.success(vo);
    }

    @GetMapping("/recommend")
    public Result<List<RouteVO>> getRecommendRoute(@RequestParam(defaultValue = "10") Integer limit) {
        List<RouteVO> voList = routeService.getRecommendRouteVOList(limit);
        return Result.success(voList);
    }

    @GetMapping("/category/{categoryId}")
    public Result<List<RouteVO>> getRouteByCategory(@PathVariable Long categoryId) {
        List<RouteVO> voList = routeService.getRouteVOByCategoryId(categoryId);
        return Result.success(voList);
    }

    @PostMapping
    public Result<Long> addRoute(@Valid @RequestBody RouteAddDTO dto) {
        try {
            log.info("开始新增路线，接收参数：{}", dto);

            Long categoryId = dto.getCategoryId();
            RouteCategory category = routeCategoryService.getById(categoryId);
            if (category == null) {
                log.error("路线分类不存在，ID：{}", categoryId);
                return Result.error("路线分类不存在");
            }

            TravelRoute route = new TravelRoute();
            route.setName(dto.getName());
            route.setDescription(dto.getDescription());
            route.setBudget(dto.getBudget());
            route.setDurationDays(dto.getDurationDays());
            route.setBestSeason(dto.getBestSeason());
            route.setCategoryId(categoryId);
            route.setViewCount(0);
            route.setStatus(1);

            if (dto.getImages() != null && !dto.getImages().isEmpty()) {
                try {
                    String imagesJson = objectMapper.writeValueAsString(dto.getImages());
                    route.setImages(imagesJson);
                    log.info("序列化后的图片JSON：{}", imagesJson);
                } catch (Exception e) {
                    log.error("图片列表序列化失败：{}", e.getMessage());
                    return Result.error("图片列表序列化失败");
                }
            } else {
                route.setImages("[]");
            }

            routeService.save(route);
            log.info("新增路线成功，路线名称：{}，主键 ID：{}", dto.getName(), route.getId());

            if (dto.getScenicList() != null && !dto.getScenicList().isEmpty()) {
                for (RouteAddDTO.RouteScenicDTO scenicDTO : dto.getScenicList()) {
                    RouteScenicRelation relation = new RouteScenicRelation();
                    relation.setRouteId(route.getId());
                    relation.setScenicId(scenicDTO.getScenicId());
                    relation.setDayNumber(scenicDTO.getDayNumber());
                    relation.setSortOrder(scenicDTO.getSortOrder());
                    relation.setDescription(scenicDTO.getDescription());
                    routeScenicRelationService.save(relation);
                }
            }

            return Result.success(route.getId());

        } catch (Exception e) {
            log.error("新增路线异常，参数：{}，异常信息：", dto, e);
            return Result.error("服务器内部错误，请联系管理员");
        }
    }

    @PutMapping("/{id}")
    public Result<String> updateRoute(@PathVariable Long id, @Valid @RequestBody RouteAddDTO dto) {
        try {
            log.info("开始更新路线，ID：{}，参数：{}", id, dto);

            TravelRoute existing = routeService.getById(id);
            if (existing == null) {
                log.error("路线不存在，ID：{}", id);
                return Result.error("路线不存在");
            }

            Long categoryId = dto.getCategoryId();
            RouteCategory category = routeCategoryService.getById(categoryId);
            if (category == null) {
                log.error("路线分类不存在，ID：{}", categoryId);
                return Result.error("路线分类不存在");
            }

            existing.setName(dto.getName());
            existing.setDescription(dto.getDescription());
            existing.setBudget(dto.getBudget());
            existing.setDurationDays(dto.getDurationDays());
            existing.setBestSeason(dto.getBestSeason());
            existing.setCategoryId(categoryId);

            if (dto.getImages() != null && !dto.getImages().isEmpty()) {
                try {
                    String imagesJson = objectMapper.writeValueAsString(dto.getImages());
                    existing.setImages(imagesJson);
                    log.info("更新路线图片JSON：{}", imagesJson);
                } catch (Exception e) {
                    log.error("图片列表序列化失败：{}", e.getMessage());
                    return Result.error("图片列表序列化失败");
                }
            }

            routeService.updateById(existing);

            QueryWrapper<RouteScenicRelation> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.eq("route_id", id);
            routeScenicRelationService.remove(deleteWrapper);

            if (dto.getScenicList() != null && !dto.getScenicList().isEmpty()) {
                for (RouteAddDTO.RouteScenicDTO scenicDTO : dto.getScenicList()) {
                    RouteScenicRelation relation = new RouteScenicRelation();
                    relation.setRouteId(id);
                    relation.setScenicId(scenicDTO.getScenicId());
                    relation.setDayNumber(scenicDTO.getDayNumber());
                    relation.setSortOrder(scenicDTO.getSortOrder());
                    relation.setDescription(scenicDTO.getDescription());
                    routeScenicRelationService.save(relation);
                }
            }

            log.info("更新路线成功，ID：{}", id);
            return Result.success("更新成功");
        } catch (Exception e) {
            log.error("更新路线异常，ID：{}，异常：", id, e);
            return Result.error("服务器内部错误");
        }
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteRoute(@PathVariable Long id) {
        try {
            TravelRoute existing = routeService.getById(id);
            if (existing == null) {
                return Result.error("路线不存在");
            }

            routeService.removeById(id);
            log.info("删除路线成功，ID：{}", id);
            return Result.success("删除成功");
        } catch (Exception e) {
            log.error("删除路线异常，ID：{}，异常：", id, e);
            return Result.error("服务器内部错误");
        }
    }

    @RequestMapping(value = "/{id}/audit", method = {RequestMethod.PUT, RequestMethod.GET})
    public Result<String> auditRoute(@PathVariable Long id, @RequestBody(required = false) Map<String, Integer> request, @RequestParam(required = false) Integer status) {
        try {
            // 优先使用RequestParam（GET请求），然后使用RequestBody（PUT请求）
            if (status != null) {
                // GET请求：status通过RequestParam传递
            } else if (request != null && request.get("status") != null) {
                status = request.get("status");
                // PUT请求：status通过RequestBody传递
            } else {
                return Result.error("审核状态不能为空");
            }

            TravelRoute route = routeService.getById(id);
            if (route == null) {
                return Result.error("路线不存在");
            }
            route.setStatus(status);
            boolean updated = routeService.updateById(route);
            return updated ? Result.success("审核成功") : Result.error("审核失败");
        } catch (Exception e) {
            log.error("审核路线异常，ID：{}，异常：", id, e);
            return Result.error("服务器内部错误");
        }
    }
}
