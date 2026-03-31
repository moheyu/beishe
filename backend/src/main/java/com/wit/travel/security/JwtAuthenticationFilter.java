package com.wit.travel.security;

import com.wit.travel.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT认证过滤器
 * 每次请求都会执行，用于验证JWT token
 */
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // OPTIONS 预检请求直接放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String requestURI = request.getRequestURI();

        // 公开接口无需 Token 验证，直接放行
        if (isPublicUri(requestURI, request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                String username = jwtUtil.extractUsername(token);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    if (jwtUtil.validateToken(token, userDetails.getUsername())) {
                        Long userId = jwtUtil.extractUserId(token);
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        // userId 写入 details，供 SecurityUtil.getCurrentUserId() 无查库读取
                        authToken.setDetails(userId);
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    } else {
                        log.warn("Token 验证失败，用户名：{}", username);
                    }
                }
            } catch (Exception e) {
                log.warn("JWT 解析失败：{}", e.getMessage());
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 判断是否为无需认证的公开接口。
     */
    private boolean isPublicUri(String uri, String method) {
        if (uri == null) {
            return false;
        }
        return uri.contains("/auth/login")
                || uri.contains("/auth/register")
                || (uri.startsWith("/scenic/") && !uri.startsWith("/admin/scenic"))
                || (uri.startsWith("/route/") && !uri.startsWith("/admin/route"))
                || uri.contains("/uploads/")
                || uri.contains("/announcement/")
                || uri.contains("/tag/")
                || (uri.startsWith("/forum/") && "GET".equalsIgnoreCase(method));
    }
}
