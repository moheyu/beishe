package com.wit.travel.controller;

import com.wit.travel.dto.LoginDTO;
import com.wit.travel.dto.RegisterDTO;
import com.wit.travel.entity.User;
import com.wit.travel.service.UserService;
import com.wit.travel.util.JwtUtil;
import com.wit.travel.vo.LoginVO;
import com.wit.travel.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * 认证控制器
 */
@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<String> register(@Valid @RequestBody RegisterDTO registerDTO) {
        // 1. 校验用户名/密码非空
        if (!StringUtils.hasText(registerDTO.getUsername()) || !StringUtils.hasText(registerDTO.getPassword())) {
            return Result.error("用户名和密码不能为空");
        }

        // 2. 检查用户名是否已存在
        User existUser = userService.getUserByUsername(registerDTO.getUsername());
        if (existUser != null) {
            return Result.error("用户名已存在");
        }

        // 3. 构建用户信息并加密密码
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword())); // BCrypt加密不会返回空（已校验密码非空）
        user.setNickname(StringUtils.hasText(registerDTO.getNickname()) ? registerDTO.getNickname() : registerDTO.getUsername());
        user.setAvatar("");
        user.setRole(0);
        user.setStatus(1);

        // 3. 保存用户
        userService.save(user);
        return Result.success("注册成功");
    }

    /**
     * 用户登录（优化版：复用Security认证后的UserDetails，减少查库次数）
     */
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO loginDTO) {
        if (!StringUtils.hasText(loginDTO.getUsername()) || !StringUtils.hasText(loginDTO.getPassword())) {
            log.warn("登录参数为空，用户名：{}", loginDTO.getUsername());
            return Result.error("用户名或密码不能为空");
        }

        try {
            // 1. Spring Security 认证（用户名+密码），UserDetailsService已查询过用户
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
            );

            // 2. 从Authentication中获取UserDetails，无需再次查库验证用户名密码
            org.springframework.security.core.userdetails.User authUser =
                    (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            String username = authUser.getUsername();

            // 3. 仅查询一次用户信息（获取ID、昵称、头像、角色、状态等非认证字段）
            User user = userService.getUserByUsername(username);
            if (user == null) {
                log.warn("登录用户不存在，用户名：{}", username);
                throw new UsernameNotFoundException("用户不存在");
            }

            // 4. 校验用户状态
            if (user.getStatus() == 0) {
                log.warn("账号被禁用，用户名：{}", username);
                return Result.error("账号已被禁用");
            }

            // 5. 生成JWT Token，构建返回VO
            String avatar = StringUtils.hasText(user.getAvatar()) ? user.getAvatar() : "";
            String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());

            LoginVO.UserInfo userInfo = new LoginVO.UserInfo(
                    user.getId(),
                    username,
                    user.getNickname(),
                    avatar,
                    user.getRole()
            );
            LoginVO loginVO = new LoginVO(token, userInfo);
            log.info("用户登录成功，用户名：{}", username);
            return Result.success(loginVO);

        } catch (BadCredentialsException e) {
            // 仅捕获密码错误异常
            log.error("密码验证失败，用户名：{}", loginDTO.getUsername(), e);
            return Result.error("用户名或密码错误");
        } catch (UsernameNotFoundException e) {
            // 捕获用户不存在异常
            log.error("用户不存在，用户名：{}", loginDTO.getUsername(), e);
            return Result.error("用户名或密码错误");
        } catch (Exception e) {
            // 其他异常（数据库、JWT等）统一处理
            log.error("登录异常，用户名：{}", loginDTO.getUsername(), e);
            return Result.error("登录失败，请稍后重试");
        }
    }

    /**
     * 用户登出（增加JWT黑名单逻辑）
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        // 清空SecurityContext
        SecurityContextHolder.clearContext();
        // 清空HttpSession
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession(false);
        if (session != null) {
            session.invalidate();
        }
        // 可以在这里添加JWT黑名单逻辑（如将token存入redis并设置过期时间）
        return Result.success("退出成功");
    }


}