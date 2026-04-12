package com.wit.travel.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wit.travel.dto.ScenicAddDTO;
import com.wit.travel.dto.ScenicUpdateDTO;
import com.wit.travel.dto.ScenicQueryDTO;
import com.wit.travel.entity.Scenic;
import com.wit.travel.entity.ScenicCategory;
import com.wit.travel.service.ScenicCategoryService;
import com.wit.travel.service.ScenicService;
import com.wit.travel.util.FileUploadUtil;
import com.wit.travel.vo.Result;
import com.wit.travel.vo.ScenicVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 景点控制器（管理端）
 */
@Slf4j
@RestController
@RequestMapping("/admin/scenic")
@PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
public class ScenicController {

    @Autowired
    private ScenicService scenicService;

    // 引入JSON工具（Jackson）
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ScenicCategoryService scenicCategoryService;

    @Autowired
    private FileUploadUtil fileUploadUtil;

    @GetMapping("/list")
    public Result<IPage<ScenicVO>> getScenicList(ScenicQueryDTO queryDTO) {
        Page<ScenicVO> page = new Page<>(queryDTO.getPageNum() != null ? queryDTO.getPageNum() : 1,
                queryDTO.getPageSize() != null ? queryDTO.getPageSize() : 10);
        IPage<ScenicVO> voPage = scenicService.getScenicVOList(page, queryDTO);
        return Result.success(voPage);
    }

    // 分页查询接口 - 与/list功能相同，为兼容不同前端调用方式
    @GetMapping("/page")
    public Result<IPage<ScenicVO>> getScenicPage(ScenicQueryDTO queryDTO) {
        Page<ScenicVO> page = new Page<>(queryDTO.getPageNum() != null ? queryDTO.getPageNum() : 1,
                queryDTO.getPageSize() != null ? queryDTO.getPageSize() : 10);
        IPage<ScenicVO> voPage = scenicService.getScenicVOList(page, queryDTO);
        return Result.success(voPage);
    }

    // 审核景点（支持GET和PUT两种方法）
    @RequestMapping(value = "/{id}/audit", method = {RequestMethod.PUT, RequestMethod.GET})
    public Result<String> auditScenic(@PathVariable Long id, @RequestBody(required = false) Map<String, Integer> request, @RequestParam(required = false) Integer status) {
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

            Scenic scenic = scenicService.getById(id);
            if (scenic == null) {
                return Result.error("景点不存在");
            }
            scenic.setStatus(status);
            boolean updated = scenicService.updateById(scenic);
            return updated ? Result.success("审核成功") : Result.error("审核失败");
        } catch (Exception e) {
            log.error("审核景点异常，ID：{}，异常：", id, e);
            return Result.error("服务器内部错误");
        }
    }

    // 设置景点推荐等级（支持GET和PUT两种方法）
    @RequestMapping(value = "/{id}/recommend-level", method = {RequestMethod.PUT, RequestMethod.GET})
    public Result<String> setRecommendLevel(@PathVariable Long id, @RequestBody(required = false) Map<String, Integer> request, @RequestParam(required = false) Integer recommendLevel) {
        try {
            // 优先使用RequestParam（GET请求），然后使用RequestBody（PUT请求）
            if (recommendLevel != null) {
                // GET请求：recommendLevel通过RequestParam传递
            } else if (request != null && request.get("recommendLevel") != null) {
                recommendLevel = request.get("recommendLevel");
                // PUT请求：recommendLevel通过RequestBody传递
            } else {
                return Result.error("推荐等级不能为空");
            }

            Scenic scenic = scenicService.getById(id);
            if (scenic == null) {
                return Result.error("景点不存在");
            }
            scenic.setRecommendLevel(recommendLevel);
            boolean updated = scenicService.updateById(scenic);
            return updated ? Result.success("推荐等级设置成功") : Result.error("设置失败");
        } catch (Exception e) {
            log.error("设置推荐等级异常，ID：{}，异常：", id, e);
            return Result.error("服务器内部错误");
        }
    }

    @GetMapping("/{id}")
    public Result<ScenicVO> getScenicById(@PathVariable Long id) {
        ScenicVO vo = scenicService.getScenicVOById(id);
        if (vo == null) {
            return Result.error("景点不存在");
        }
        return Result.success(vo);
    }

    @GetMapping("/recommend")
    public Result<List<ScenicVO>> getRecommendScenic(@RequestParam(defaultValue = "10") Integer limit) {
        List<ScenicVO> voList = scenicService.getRecommendScenicVOList(limit);
        return Result.success(voList);
    }

    @GetMapping("/category/{categoryId}")
    public Result<List<ScenicVO>> getScenicByCategory(@PathVariable Long categoryId) {
        List<ScenicVO> voList = scenicService.getScenicVOByCategoryId(categoryId);
        return Result.success(voList);
    }

    @GetMapping("/tag/{tagId}")
    public Result<List<ScenicVO>> getScenicByTag(@PathVariable Long tagId) {
        List<ScenicVO> voList = scenicService.getScenicVOByTagId(tagId);
        return Result.success(voList);
    }



    @DeleteMapping("/{id}")
    public Result<String> deleteScenic(@PathVariable Long id) {
        try {
            log.info("开始删除景点，ID：{}", id);

            // 先检查景点是否存在
            Scenic existing = scenicService.getById(id);
            if (existing == null) {
                log.warn("删除景点失败，景点不存在，ID：{}", id);
                return Result.error("景点不存在");
            }

            // 删除景点关联的图片文件
            try {
                String imagesJson = existing.getImages();
                log.info("景点【{}】的images字段原始值：{}", existing.getName(), imagesJson);

                if (imagesJson != null && !imagesJson.trim().isEmpty() && !imagesJson.equals("[]")) {
                    // 解析JSON字符串获取图片URL数组
                    try {
                        String[] imageUrls = objectMapper.readValue(imagesJson, String[].class);
                        if (imageUrls != null && imageUrls.length > 0) {
                            log.info("解析出图片URL数组，数量：{}", imageUrls.length);
                            for (int i = 0; i < imageUrls.length; i++) {
                                log.info("  图片{}: {}", i + 1, imageUrls[i]);
                            }

                            // 删除文件
                            fileUploadUtil.deleteFiles(imageUrls);
                            log.info("成功删除景点关联的图片文件，数量：{}", imageUrls.length);
                        } else {
                            log.info("图片URL数组为空，无需删除文件");
                        }
                    } catch (Exception parseException) {
                        log.error("解析images字段JSON失败，ID：{}，images值：{}，异常：", id, imagesJson, parseException);
                        // JSON解析失败不阻止景点删除
                    }
                } else {
                    log.info("景点没有关联图片或images字段为空");
                }
            } catch (Exception e) {
                log.error("删除景点图片文件异常，ID：{}，异常：", id, e);
                // 图片删除失败不阻止景点删除，继续执行数据库删除
            }

            // 执行删除（逻辑删除或物理删除，取决于您的业务）
            boolean removed = scenicService.removeById(id);
            if (removed) {
                log.info("删除景点成功，ID：{}", id);
                return Result.success("删除成功");
            } else {
                log.error("删除景点失败，ID：{}", id);
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除景点异常，ID：{}，异常：", id, e);
            return Result.error("服务器内部错误");
        }
    }



    @PostMapping
    public Result<Long> addScenic(@Valid @RequestBody ScenicAddDTO dto) {
        try {
            log.info("开始新增景点，接收参数：{}", dto);
            
            // 直接使用前端传来的分类ID
            Long categoryId = dto.getCategoryId();
            // 验证分类是否存在（如果需要）
            ScenicCategory category = scenicCategoryService.getById(categoryId);
            if (category == null) {
                log.error("景点分类不存在，ID：{}", categoryId);
                return Result.error("景点分类不存在");
            }

            // DTO转实体
            Scenic scenic = new Scenic();
            scenic.setName(dto.getName());
            scenic.setDescription(dto.getDescription());
            scenic.setPrice(dto.getPrice());
            scenic.setLocation(dto.getLocation());
            scenic.setCategoryId(categoryId);  // 直接设置ID
            scenic.setViewCount(0);
            scenic.setStatus(1);

            // 将图片列表序列化为JSON字符串
            if (dto.getImages() != null && !dto.getImages().isEmpty()) {
                try {
                    String imagesJson = objectMapper.writeValueAsString(dto.getImages());
                    scenic.setImages(imagesJson); // 实体类images为String，正常赋值
                    log.info("序列化后的图片JSON：{}", imagesJson);
                } catch (Exception e) {
                    log.error("图片列表序列化失败：{}", e.getMessage());
                    return Result.error("图片列表序列化失败");
                }
            } else {
                scenic.setImages("[]"); // 空列表存空JSON数组
            }

            // 4. 保存到数据库
            scenicService.save(scenic);
            log.info("新增景点成功，景点名称：{}，主键 ID：{}", dto.getName(), scenic.getId());
            return Result.success(scenic.getId());

        } catch (Exception e) {
            log.error("新增景点异常，参数：{}，异常信息：", dto, e);
            return Result.error("服务器内部错误，请联系管理员");
        }
    }

    @PutMapping("/{id}")
    public Result<String> updateScenic(@PathVariable Long id, @RequestBody ScenicUpdateDTO dto) {
        try {
            log.info("开始更新景点，ID：{}，参数：{}", id, dto);
            
            // 1. 检查景点是否存在
            Scenic existing = scenicService.getById(id);
            if (existing == null) {
                log.error("景点不存在，ID：{}", id);
                return Result.error("景点不存在");
            }

            // 2. 如果传了分类ID，验证分类是否存在
            Long categoryId = dto.getCategoryId();
            if (categoryId != null) {
                ScenicCategory category = scenicCategoryService.getById(categoryId);
                if (category == null) {
                    log.error("景点分类不存在，ID：{}", categoryId);
                    return Result.error("景点分类不存在");
                }
                existing.setCategoryId(categoryId);
            }

            // 3. 更新景点信息（仅更新非空字段）
            if (dto.getName() != null) {
                existing.setName(dto.getName());
            }
            if (dto.getDescription() != null) {
                existing.setDescription(dto.getDescription());
            }
            if (dto.getPrice() != null) {
                existing.setPrice(dto.getPrice());
            }
            if (dto.getLocation() != null) {
                existing.setLocation(dto.getLocation());
            }
            if (dto.getOpeningHours() != null) {
                existing.setOpeningHours(dto.getOpeningHours());
            }
            if (dto.getImages() != null && !dto.getImages().isEmpty()) {
                existing.setImages(dto.getImages());
            }
            if (dto.getStatus() != null) {
                existing.setStatus(dto.getStatus());
            }
            if (dto.getRecommendLevel() != null) {
                existing.setRecommendLevel(dto.getRecommendLevel());
            }

            // 4. 处理图片
            if (dto.getImages() != null && !dto.getImages().isEmpty()) {
                try {
                    String imagesJson = objectMapper.writeValueAsString(dto.getImages());
                    existing.setImages(imagesJson);
                    log.info("更新景点图片JSON：{}", imagesJson);
                } catch (Exception e) {
                    log.error("图片列表序列化失败：{}", e.getMessage());
                    return Result.error("图片列表序列化失败");
                }
            }

            // 5. 保存更新
            boolean updated = scenicService.updateById(existing);
            if (updated) {
                log.info("更新景点成功，ID：{}", id);
                return Result.success("更新成功");
            } else {
                return Result.error("更新失败");
            }
        } catch (Exception e) {
            log.error("更新景点异常，ID：{}，异常：", id, e);
            return Result.error("服务器内部错误");
        }
    }
}
