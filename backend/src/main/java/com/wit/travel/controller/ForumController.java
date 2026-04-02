package com.wit.travel.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wit.travel.dto.ForumPostAddDTO;
import com.wit.travel.dto.ForumPostQueryDTO;
import com.wit.travel.dto.ForumReplyAddDTO;
import com.wit.travel.dto.ForumReplyQueryDTO;
import com.wit.travel.entity.ForumPost;
import com.wit.travel.entity.ForumReply;
import com.wit.travel.service.ForumPostService;
import com.wit.travel.service.ForumReplyService;
import com.wit.travel.util.SecurityUtil;
import com.wit.travel.vo.ForumPostVO;
import com.wit.travel.vo.ForumReplyVO;
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
@RequestMapping("/forum")
public class ForumController {

    @Autowired
    private ForumPostService forumPostService;

    @Autowired
    private ForumReplyService forumReplyService;

    @GetMapping("/list")
    public Result<IPage<ForumPostVO>> getForumPostList(ForumPostQueryDTO queryDTO) {
        Page<ForumPostVO> page = new Page<>(queryDTO.getPageNum() != null ? queryDTO.getPageNum() : 1,
                queryDTO.getPageSize() != null ? queryDTO.getPageSize() : 10);
        IPage<ForumPostVO> voPage = forumPostService.getForumPostVOList(page, queryDTO);
        return Result.success(voPage);
    }

    @GetMapping("/{id}")
    public Result<ForumPostVO> getForumPostById(@PathVariable Long id) {
        ForumPostVO vo = forumPostService.getForumPostVOById(id);
        if (vo == null) {
            return Result.error("帖子不存在");
        }
        return Result.success(vo);
    }

    @GetMapping("/recommend")
    public Result<List<ForumPostVO>> getRecommendForumPost(@RequestParam(defaultValue = "10") Integer limit) {
        List<ForumPostVO> voList = forumPostService.getRecommendForumPostVOList(limit);
        return Result.success(voList);
    }

    @PostMapping
    public Result<Long> addForumPost(@Valid @RequestBody ForumPostAddDTO addDTO) {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return Result.error("请先登录");
        }

        ForumPost post = new ForumPost();
        post.setUserId(userId);
        post.setTitle(addDTO.getTitle());
        post.setContent(addDTO.getContent());
        post.setScenicId(addDTO.getScenicId());
        post.setRouteId(addDTO.getRouteId());
        post.setViewCount(0);
        post.setReplyCount(0);
        post.setIsTop(0);
        post.setIsEssence(0);
        post.setIsAudit(1);
        forumPostService.save(post);

        return Result.success(post.getId());
    }

    @PutMapping("/{id}")
    public Result<String> updateForumPost(@PathVariable Long id, @RequestBody ForumPostAddDTO addDTO) {
        try {
            ForumPost post = forumPostService.getById(id);
            if (post == null) {
                return Result.error("帖子不存在");
            }
            // 只更新非空字段
            if (addDTO.getTitle() != null) {
                post.setTitle(addDTO.getTitle());
            }
            if (addDTO.getContent() != null) {
                post.setContent(addDTO.getContent());
            }
            if (addDTO.getScenicId() != null) {
                post.setScenicId(addDTO.getScenicId());
            }
            if (addDTO.getRouteId() != null) {
                post.setRouteId(addDTO.getRouteId());
            }
            boolean updated = forumPostService.updateById(post);
            return updated ? Result.success("更新成功") : Result.error("更新失败");
        } catch (Exception e) {
            log.error("更新帖子异常，ID：{}，异常：", id, e);
            return Result.error("服务器内部错误");
        }
    }

    @PutMapping("/{id}/audit")
    public Result<String> auditForumPost(@PathVariable Long id, @RequestParam Integer isAudit, @RequestParam(required = false) String auditRemark) {
        try {
            ForumPost post = forumPostService.getById(id);
            if (post == null) {
                return Result.error("帖子不存在");
            }
            post.setIsAudit(isAudit);
            post.setAuditTime(LocalDateTime.now());
            post.setAuditRemark(auditRemark);
            boolean updated = forumPostService.updateById(post);
            return updated ? Result.success("审核成功") : Result.error("审核失败");
        } catch (Exception e) {
            log.error("审核帖子异常，ID：{}，异常：", id, e);
            return Result.error("服务器内部错误");
        }
    }

    @PutMapping("/{id}/top")
    public Result<String> setTop(@PathVariable Long id, @RequestParam Object isTop) {
        try {
            ForumPost post = forumPostService.getById(id);
            if (post == null) {
                return Result.error("帖子不存在");
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
            post.setIsTop(topValue);
            boolean updated = forumPostService.updateById(post);
            return updated ? Result.success("设置成功") : Result.error("设置失败");
        } catch (Exception e) {
            log.error("设置置顶异常，ID：{}，异常：", id, e);
            return Result.error("服务器内部错误");
        }
    }

    @PutMapping("/{id}/essence")
    public Result<String> setEssence(@PathVariable Long id, @RequestParam Object isEssence) {
        try {
            ForumPost post = forumPostService.getById(id);
            if (post == null) {
                return Result.error("帖子不存在");
            }
            // 处理各种类型的输入
            Integer essenceValue = 0;
            if (isEssence instanceof Number) {
                essenceValue = ((Number) isEssence).intValue();
            } else if (isEssence instanceof Boolean) {
                essenceValue = (Boolean) isEssence ? 1 : 0;
            } else if (isEssence instanceof String) {
                essenceValue = "true".equalsIgnoreCase((String) isEssence) ? 1 : 0;
            }
            post.setIsEssence(essenceValue);
            boolean updated = forumPostService.updateById(post);
            return updated ? Result.success("设置成功") : Result.error("设置失败");
        } catch (Exception e) {
            log.error("设置精华异常，ID：{}，异常：", id, e);
            return Result.error("服务器内部错误");
        }
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteForumPost(@PathVariable Long id) {
        try {
            ForumPost post = forumPostService.getById(id);
            if (post == null) {
                return Result.error("帖子不存在");
            }
            forumPostService.removeById(id);
            log.info("删除帖子成功，ID：{}", id);
            return Result.success("删除成功");
        } catch (Exception e) {
            log.error("删除帖子异常，ID：{}，异常：", id, e);
            return Result.error("服务器内部错误");
        }
    }

    @GetMapping("/{postId}/replies")
    public Result<IPage<ForumReplyVO>> getForumReplyList(@PathVariable Long postId, ForumReplyQueryDTO queryDTO) {
        Page<ForumReplyVO> page = new Page<>(queryDTO.getPageNum() != null ? queryDTO.getPageNum() : 1,
                queryDTO.getPageSize() != null ? queryDTO.getPageSize() : 10);
        IPage<ForumReplyVO> voPage = forumReplyService.getForumReplyVOList(page, queryDTO);
        return Result.success(voPage);
    }

    @PostMapping("/{postId}/reply")
    public Result<String> addForumReply(@PathVariable Long postId, @Valid @RequestBody ForumReplyAddDTO addDTO) {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return Result.error("请先登录");
        }

        ForumReply reply = new ForumReply();
        reply.setPostId(postId);
        reply.setUserId(userId);
        // 处理parentId：只有非空且非0的值才设置（避免前端传入0导致外键约束失败）
        if (addDTO.getParentId() != null && addDTO.getParentId() != 0) {
            reply.setParentId(addDTO.getParentId());
        }
        reply.setContent(addDTO.getContent());
        reply.setIsAudit(1);
        forumReplyService.save(reply);

        ForumPost post = forumPostService.getById(postId);
        if (post != null) {
            post.setReplyCount(post.getReplyCount() + 1);
            forumPostService.updateById(post);
        }

        return Result.success("回复成功");
    }

    @PutMapping("/reply/{id}/audit")
    public Result<String> auditForumReply(@PathVariable Long id, @RequestParam Integer isAudit, @RequestParam(required = false) String auditRemark) {
        try {
            ForumReply reply = forumReplyService.getById(id);
            if (reply == null) {
                return Result.error("回复不存在");
            }
            reply.setIsAudit(isAudit);
            reply.setAuditTime(LocalDateTime.now());
            reply.setAuditRemark(auditRemark);
            boolean updated = forumReplyService.updateById(reply);
            return updated ? Result.success("审核成功") : Result.error("审核失败");
        } catch (Exception e) {
            log.error("审核回复异常，ID：{}，异常：", id, e);
            return Result.error("服务器内部错误");
        }
    }

    @DeleteMapping("/reply/{id}")
    public Result<String> deleteForumReply(@PathVariable Long id) {
        try {
            ForumReply reply = forumReplyService.getById(id);
            if (reply == null) {
                return Result.error("回复不存在");
            }
            forumReplyService.removeById(id);
            log.info("删除回复成功，ID：{}", id);
            return Result.success("删除成功");
        } catch (Exception e) {
            log.error("删除回复异常，ID：{}，异常：", id, e);
            return Result.error("服务器内部错误");
        }
    }
}
