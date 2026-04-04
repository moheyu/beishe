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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        
        // OPTIONS 预检请求直接放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }
        
        log.info("JWT 过滤器拦截请求：{}", requestURI);
        
        // 排除登录、注册和公开接口请求
        // 注意：/admin/ 路径需要认证，不能放在这里
        if (requestURI != null && (
                requestURI.contains("/auth/login") ||
                requestURI.contains("/auth/register") ||
                // 公开接口
                (requestURI.startsWith("/scenic/") && !requestURI.startsWith("/admin/scenic")) ||
                (requestURI.startsWith("/route/") && !requestURI.startsWith("/admin/route")) ||
                // 评分接口公开（GET），POST需要登录由Controller控制
                requestURI.startsWith("/rating/") ||
                // 评论接口需要认证，不能放行
                requestURI.contains("/uploads/") ||
                requestURI.contains("/announcement/") ||
                // 论坛接口：只放行 GET 请求的查询接口
                (requestURI.startsWith("/forum/") && 
                    ("GET".equalsIgnoreCase(request.getMethod()) || 
                     requestURI.contains("/replies"))) ||
                requestURI.contains("/tag/"))) {
            log.info("放行公开接口：{}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }
        
        String authHeader = request.getHeader("Authorization");
        log.info("请求头 Authorization: {}", authHeader != null ? "存在" : "不存在");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            log.info("Token 前 20 位：{}", token.substring(0, Math.min(20, token.length())) + "...");
            String username = jwtUtil.extractUsername(token);
            log.info("从 Token 中提取的用户名：{}", username);
        
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                try {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    log.info("加载 UserDetails 成功：{}", userDetails.getUsername());
        
                    boolean isValid = jwtUtil.validateToken(token, userDetails.getUsername());
                    log.info("Token 验证结果：{}", isValid);
        
                    if (isValid) {
                        Long userId = jwtUtil.extractUserId(token);
                        log.info("提取用户 ID: {}, 设置认证到 SecurityContext", userId);
                        // 第一个参数必须是 UserDetails 对象，确保 authorities 正确传递
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    } else {
                        log.warn("Token 验证失败，用户名：{}", username);
                    }
                } catch (Exception e) {
                    // JWT 验证失败，清除认证信息
                    log.warn("JWT 验证失败：{}, token: {}", e.getMessage(), token.substring(0, Math.min(20, token.length())) + "...");
                    SecurityContextHolder.clearContext();
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
