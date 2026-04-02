package com.wit.travel.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wit.travel.entity.ScenicCategory;
import com.wit.travel.service.ScenicCategoryService;
import com.wit.travel.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 景点分类控制器（管理端）
 */
@Slf4j
@RestController
@RequestMapping("/admin/scenic/category")
@PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
public class ScenicCategoryController {

    @Autowired
    private ScenicCategoryService scenicCategoryService;

    /**
     * 分页获取分类列表
     */
    @GetMapping("/list")
    public Result<IPage<ScenicCategory>> getCategoryList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name) {

        Page<ScenicCategory> page = new Page<>(pageNum, pageSize);
        QueryWrapper<ScenicCategory> queryWrapper = new QueryWrapper<>();

        if (name != null && !name.trim().isEmpty()) {
            queryWrapper.like("name", name);
        }

        IPage<ScenicCategory> pageResult = scenicCategoryService.page(page, queryWrapper);
        return Result.success(pageResult);
    }

    /**
     * 获取所有分类（不分页）
     */
    @GetMapping("/all")
    public Result<List<ScenicCategory>> getAllCategories() {
        List<ScenicCategory> list = scenicCategoryService.list();
        return Result.success(list);
    }

    /**
     * 根据ID获取分类
     */
    @GetMapping("/{id}")
    public Result<ScenicCategory> getCategoryById(@PathVariable Long id) {
        ScenicCategory category = scenicCategoryService.getById(id);
        if (category == null) {
            return Result.error("分类不存在");
        }
        return Result.success(category);
    }

    /**
     * 新增分类
     */
    @PostMapping
    public Result<String> addCategory(@Valid @RequestBody ScenicCategory category) {
        try {
            // 检查名称是否已存在
            QueryWrapper<ScenicCategory> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", category.getName());
            long count = scenicCategoryService.count(queryWrapper);
            if (count > 0) {
                return Result.error("分类名称已存在");
            }

            boolean saved = scenicCategoryService.save(category);
            return saved ? Result.success("添加成功") : Result.error("添加失败");
        } catch (Exception e) {
            log.error("添加景点分类异常，参数：{}，异常：", category, e);
            return Result.error("服务器内部错误");
        }
    }

    /**
     * 更新分类
     */
    @PutMapping("/{id}")
    public Result<String> updateCategory(@PathVariable Long id, @Valid @RequestBody ScenicCategory category) {
        try {
            // 检查分类是否存在
            ScenicCategory existing = scenicCategoryService.getById(id);
            if (existing == null) {
                return Result.error("分类不存在");
            }

            // 如果名称变更，检查新名称是否已存在
            if (!existing.getName().equals(category.getName())) {
                QueryWrapper<ScenicCategory> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("name", category.getName());
                long count = scenicCategoryService.count(queryWrapper);
                if (count > 0) {
                    return Result.error("分类名称已存在");
                }
            }

            category.setId(id);
            boolean updated = scenicCategoryService.updateById(category);
            return updated ? Result.success("更新成功") : Result.error("更新失败");
        } catch (Exception e) {
            log.error("更新景点分类异常，ID：{}，参数：{}，异常：", id, category, e);
            return Result.error("服务器内部错误");
        }
    }

    /**
     * 删除分类
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteCategory(@PathVariable Long id) {
        try {
            ScenicCategory existing = scenicCategoryService.getById(id);
            if (existing == null) {
                return Result.error("分类不存在");
            }

            boolean removed = scenicCategoryService.removeById(id);
            return removed ? Result.success("删除成功") : Result.error("删除失败");
        } catch (Exception e) {
            log.error("删除景点分类异常，ID：{}，异常：", id, e);
            return Result.error("服务器内部错误");
        }
    }
}
