package com.wit.travel.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wit.travel.entity.User;
import com.wit.travel.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 优化后的 UserDetailsService 实现（推荐使用这个版本）
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 类型安全的查询（LambdaQueryWrapper 避免硬编码字段）
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username);
        User user = userMapper.selectOne(queryWrapper);

        // 2. 用户不存在抛标准化异常
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在：" + username);
        }

        // 3. 校验密码非空（核心！避免 Empty encoded password 警告）
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            log.error("检测到空密码用户：{}, ID: {}", username, user.getId());
            throw new UsernameNotFoundException("用户密码未设置，请联系管理员");
        }

        // 4. 动态构建角色/权限（复用原代码的角色逻辑）
        String authority;
        if (user.getRole() == 2) {
            authority = "ROLE_ROOT";
        } else if (user.getRole() == 1) {
            authority = "ROLE_ADMIN";
        } else {
            authority = "ROLE_USER";
        }

        // 5. 完整构建 UserDetails（规范设置所有状态属性）
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(), // 必须传入数据库中加密后的密码（核心！）
                user.getStatus() == 1, // 是否启用（对应数据库状态：1=启用，0=禁用）
                true, // 账号是否未过期
                true, // 凭证是否未过期
                true, // 账号是否未锁定
                AuthorityUtils.commaSeparatedStringToAuthorityList(authority)
        );
    }
}