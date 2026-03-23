package com.wit.travel.util;

import com.wit.travel.entity.User;
import com.wit.travel.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

/**
 * 安全工具类
 * 用于获取当前登录用户信息
 */
@Slf4j
public class SecurityUtil {

    private static UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        SecurityUtil.userService = userService;
    }

    /**
     * 获取当前登录用户 ID
     */
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("SecurityUtil.getCurrentUserId() 被调用");
        log.info("Authentication: {}", authentication);
            
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            log.info("Principal 类型: {}", principal.getClass().getName());
            log.info("Principal 内容: {}", principal);
                
            // 情况 1：principal 是 Long 类型（旧逻辑）
            if (principal instanceof Long) {
                return (Long) principal;
            }
                
            // 情况 2：principal 是 User 实体对象
            if (principal instanceof User) {
                return ((User) principal).getId();
            }
                
            // 情况 3：principal 是 UserDetails 对象（JWT 过滤器设置的）
            if (principal instanceof org.springframework.security.core.userdetails.User) {
                String username = ((org.springframework.security.core.userdetails.User) principal).getUsername();
                log.info("从 UserDetails 获取用户名: {}", username);
                // 通过用户名查询用户 ID
                if (userService != null) {
                    User user = userService.getUserByUsername(username);
                    if (user != null) {
                        log.info("查询到用户 ID: {}", user.getId());
                        return user.getId();
                    }
                }
            }
        } else {
            log.warn("SecurityContext 中的 Authentication 为 null！");
        }
        log.warn("无法获取当前用户 ID，返回 null");
        return null;
    }

    /**
     * 获取当前登录用户名
     */
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getName();
        }
        return null;
    }

    /**
     * 获取当前登录用户角色
     * 返回1表示管理员，0表示普通用户
     */
    public static Integer getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();
            if (authorities != null && !authorities.isEmpty()) {
                String role = authorities.get(0).getAuthority();
                if ("ROLE_ADMIN".equals(role)) {
                    return 1;
                }
            }
            Object principal = authentication.getPrincipal();
            if (principal instanceof Long) {
                return 0;
            }
        }
        return null;
    }
}
