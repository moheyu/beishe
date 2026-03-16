package com.wit.travel.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

/**
 * 安全工具类
 * 用于获取当前登录用户信息
 */
public class SecurityUtil {

    /**
     * 获取当前登录用户ID
     */
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof Long) {
                return (Long) principal;
            } else if (principal instanceof org.springframework.security.core.userdetails.User) {
                // 登录时返回的是User对象，需要通过用户名查询用户ID
                String username = ((org.springframework.security.core.userdetails.User) principal).getUsername();
                // 这里可以添加缓存或直接查询数据库
                // 为了避免循环依赖，暂时返回null，建议在需要的地方直接查询
                return null;
            }
        }
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
