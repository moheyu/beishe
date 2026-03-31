package com.wit.travel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wit.travel.constant.CacheKeyConstants;
import com.wit.travel.entity.User;
import com.wit.travel.mapper.UserMapper;
import com.wit.travel.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 用户 Service 实现
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<User> listAllUsers() {
        return baseMapper.selectList(null);
    }

    @Override
    public User getUserById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public boolean updateUser(User user) {
        return baseMapper.updateById(user) > 0;
    }

    @Override
    public boolean deleteUser(Long id) {
        return baseMapper.deleteById(id) > 0;
    }

    @Override
    public User getUserByUsername(String username) {
        String cacheKey = String.format(CacheKeyConstants.USER_INFO_KEY, username);

        User cachedUser = (User) redisTemplate.opsForValue().get(cacheKey);
        if (cachedUser != null) {
            log.debug("缓存命中，用户：{}", username);
            return cachedUser;
        }

        User dbUser = baseMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, username));

        if (dbUser != null) {
            redisTemplate.opsForValue().set(cacheKey, dbUser, 30, TimeUnit.MINUTES);
            log.debug("缓存写入，用户：{}", username);
        }

        return dbUser;
    }
}
