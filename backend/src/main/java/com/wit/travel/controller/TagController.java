package com.wit.travel.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wit.travel.entity.ScenicTag;
import com.wit.travel.service.ScenicTagService;
import com.wit.travel.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 标签控制器
 */
@Slf4j
@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private ScenicTagService scenicTagService;

    /**
     * 分页获取标签列表
     */
    @GetMapping("/list")
    public Result<IPage<ScenicTag>> getTagList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name) {

        Page<ScenicTag> page = new Page<>(pageNum, pageSize);
        QueryWrapper<ScenicTag> queryWrapper = new QueryWrapper<>();

        if (name != null && !name.trim().isEmpty()) {
            queryWrapper.like("name", name);
        }

        // 按排序字段排序
        queryWrapper.orderByAsc("sort");

        IPage<ScenicTag> pageResult = scenicTagService.page(page, queryWrapper);
        return Result.success(pageResult);
    }

    /**
     * 获取所有标签（不分页）
     */
    @GetMapping("/all")
    public Result<List<ScenicTag>> getAllTags() {
        List<ScenicTag> list = scenicTagService.list();
        return Result.success(list);
    }

    /**
     * 根据ID获取标签
     */
    @GetMapping("/{id}")
    public Result<ScenicTag> getTagById(@PathVariable Long id) {
        ScenicTag tag = scenicTagService.getById(id);
        if (tag == null) {
            return Result.error("标签不存在");
        }
        return Result.success(tag);
    }

    /**
     * 新增标签
     */
    @PostMapping
    public Result<String> addTag(@Valid @RequestBody ScenicTag tag) {
        try {
            // 检查名称是否已存在
            QueryWrapper<ScenicTag> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", tag.getName());
            long count = scenicTagService.count(queryWrapper);
            if (count > 0) {
                return Result.error("标签名称已存在");
            }

            boolean saved = scenicTagService.save(tag);
            return saved ? Result.success("添加成功") : Result.error("添加失败");
        } catch (Exception e) {
            log.error("添加标签异常，参数：{}，异常：", tag, e);
            return Result.error("服务器内部错误");
        }
    }

    /**
     * 更新标签
     */
    @PutMapping("/{id}")
    public Result<String> updateTag(@PathVariable Long id, @Valid @RequestBody ScenicTag tag) {
        try {
            // 检查标签是否存在
            ScenicTag existing = scenicTagService.getById(id);
            if (existing == null) {
                return Result.error("标签不存在");
            }

            // 如果名称变更，检查新名称是否已存在
            if (!existing.getName().equals(tag.getName())) {
                QueryWrapper<ScenicTag> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("name", tag.getName());
                long count = scenicTagService.count(queryWrapper);
                if (count > 0) {
                    return Result.error("标签名称已存在");
                }
            }

            tag.setId(id);
            boolean updated = scenicTagService.updateById(tag);
            return updated ? Result.success("更新成功") : Result.error("更新失败");
        } catch (Exception e) {
            log.error("更新标签异常，ID：{}，参数：{}，异常：", id, tag, e);
            return Result.error("服务器内部错误");
        }
    }

    /**
     * 删除标签
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteTag(@PathVariable Long id) {
        try {
            ScenicTag existing = scenicTagService.getById(id);
            if (existing == null) {
                return Result.error("标签不存在");
            }

            boolean removed = scenicTagService.removeById(id);
            return removed ? Result.success("删除成功") : Result.error("删除失败");
        } catch (Exception e) {
            log.error("删除标签异常，ID：{}，异常：", id, e);
            return Result.error("服务器内部错误");
        }
    }
}
