package com.wit.travel.controller;

import com.wit.travel.dto.AnnouncementAddDTO;
import com.wit.travel.entity.Announcement;
import com.wit.travel.service.AnnouncementService;
import com.wit.travel.util.SecurityUtil;
import com.wit.travel.vo.AnnouncementVO;
import com.wit.travel.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/announcement")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @GetMapping("/list")
    public Result<List<AnnouncementVO>> getAnnouncementList() {
        List<AnnouncementVO> voList = announcementService.getAnnouncementVOList();
        return Result.success(voList);
    }

    @GetMapping("/{id}")
    public Result<AnnouncementVO> getAnnouncementById(@PathVariable Long id) {
        AnnouncementVO vo = announcementService.getAnnouncementVOById(id);
        if (vo == null) {
            return Result.error("公告不存在");
        }
        return Result.success(vo);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    public Result<Long> addAnnouncement(@Valid @RequestBody AnnouncementAddDTO addDTO) {
        try {
            Announcement announcement = new Announcement();
            announcement.setTitle(addDTO.getTitle());
            announcement.setContent(addDTO.getContent());
            announcement.setType(addDTO.getType());
            announcement.setIsTop(0);
            announcement.setStatus(1);
            announcement.setPublishTime(LocalDateTime.now());
            announcementService.save(announcement);
            log.info("新增公告成功，标题：{}", addDTO.getTitle());
            return Result.success(announcement.getId());
        } catch (Exception e) {
            log.error("新增公告异常，参数：{}，异常信息：", addDTO, e);
            return Result.error("服务器内部错误");
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    public Result<String> updateAnnouncement(@PathVariable Long id, @Valid @RequestBody AnnouncementAddDTO addDTO) {
        try {
            Announcement existing = announcementService.getById(id);
            if (existing == null) {
                log.error("公告不存在，ID：{}", id);
                return Result.error("公告不存在");
            }

            existing.setTitle(addDTO.getTitle());
            existing.setContent(addDTO.getContent());
            existing.setType(addDTO.getType());

            boolean updated = announcementService.updateById(existing);
            if (updated) {
                log.info("更新公告成功，ID：{}", id);
                return Result.success("更新成功");
            } else {
                return Result.error("更新失败");
            }
        } catch (Exception e) {
            log.error("更新公告异常，ID：{}，异常：", id, e);
            return Result.error("服务器内部错误");
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    public Result<String> deleteAnnouncement(@PathVariable Long id) {
        try {
            Announcement existing = announcementService.getById(id);
            if (existing == null) {
                return Result.error("公告不存在");
            }

            announcementService.removeById(id);
            log.info("删除公告成功，ID：{}", id);
            return Result.success("删除成功");
        } catch (Exception e) {
            log.error("删除公告异常，ID：{}，异常：", id, e);
            return Result.error("服务器内部错误");
        }
    }

    @PutMapping("/{id}/top")
    public Result<String> setTop(@PathVariable Long id, @RequestParam Object isTop) {
        try {
            Announcement announcement = announcementService.getById(id);
            if (announcement == null) {
                return Result.error("公告不存在");
            }
            // 处理各种类型的输入
            Integer topValue = 0;
            if (isTop instanceof Number) {
                topValue = ((Number) isTop).intValue();
            } else if (isTop instanceof Boolean) {
                topValue = (Boolean) isTop ? 1 : 0;
            } else if (isTop instanceof String) {
                topValue = "true".equalsIgnoreCase((String) isTop) ? 1 : 0;
            }
            announcement.setIsTop(topValue);
            boolean updated = announcementService.updateById(announcement);
            return updated ? Result.success("设置成功") : Result.error("设置失败");
        } catch (Exception e) {
            log.error("设置置顶异常，ID：{}，异常：", id, e);
            return Result.error("服务器内部错误");
        }
    }

    @PutMapping("/{id}/publish")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    public Result<String> publishAnnouncement(@PathVariable Long id) {
        try {
            Announcement announcement = announcementService.getById(id);
            if (announcement == null) {
                return Result.error("公告不存在");
            }
            announcement.setStatus(1);
            announcement.setPublishTime(LocalDateTime.now());
            boolean updated = announcementService.updateById(announcement);
            return updated ? Result.success("发布成功") : Result.error("发布失败");
        } catch (Exception e) {
            log.error("发布公告异常，ID：{}，异常：", id, e);
            return Result.error("服务器内部错误");
        }
    }
}
