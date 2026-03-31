package com.wit.travel.util;

import com.wit.travel.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 安全工具类，用于从 SecurityContext 中获取当前登录用户信息。
 *
 * <p>userId 从 JWT 过滤器写入 Authentication details 的 Long 值中读取，
 * 无需额外数据库查询。
 */
@Slf4j
@Component
public class SecurityUtil {

    private SecurityUtil() {
    }

    /**
     * 获取当前登录用户 ID。
     * <p>JWT 过滤器在验证 Token 后将 userId 写入 Authentication details，
     * 此处直接读取，避免每次请求查库。
     *
     * @return 用户 ID，未登录时返回 null
     */
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        // JWT 过滤器将 userId 写入 details
        Object details = authentication.getDetails();
        if (details instanceof Long) {
            return (Long) details;
        }

        // 兼容：principal 直接是 User 实体（非 JWT 场景）
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            return ((User) principal).getId();
        }

        log.warn("无法从 SecurityContext 中提取 userId，principal 类型：{}",
                principal == null ? "null" : principal.getClass().getName());
        return null;
    }

    /**
     * 获取当前登录用户名。
     *
     * @return 用户名，未登录时返回 null
     */
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        return authentication.getName();
    }

    /**
     * 获取当前登录用户角色值。
     *
     * @return 1-管理员或 ROOT，0-普通用户，null-未登录
     */
    public static Integer getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities == null || authorities.isEmpty()) {
            return 0;
        }
        String role = authorities.iterator().next().getAuthority();
        return ("ROLE_ROOT".equals(role) || "ROLE_ADMIN".equals(role)) ? 1 : 0;
    }
}
