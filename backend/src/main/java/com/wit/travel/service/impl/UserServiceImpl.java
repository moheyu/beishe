package com.wit.travel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wit.travel.constant.CacheKeyConstants;
import com.wit.travel.entity.User;
import com.wit.travel.mapper.UserMapper;
import com.wit.travel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 用户Service实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;






        @PostConstruct
        public void testRedis() {
            try {
                RedisConnection conn = redisTemplate.getConnectionFactory().getConnection();
                System.out.println("Redis 连接测试：" + conn.ping());
                conn.close();
            } catch (Exception e) {
                System.out.println("======== Redis 连接失败详情 ========");
                e.printStackTrace(); // 这里会打印具体错误：是超时？还是拒绝？
                System.out.println("====================================");
            }
        }

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
        // 1. 构造缓存 Key
        String cacheKey = String.format(CacheKeyConstants.USER_INFO_KEY, username);
        
        // 2. 先查 Redis 缓存
        User cachedUser = (User) redisTemplate.opsForValue().get(cacheKey);
        if (cachedUser != null) {
            // 缓存命中：直接返回，无需查数据库
            System.out.println("【缓存命中】从 Redis 获取用户：" + username);
            return cachedUser;
        }

        // 3. 缓存未命中：查数据库
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User dbUser = baseMapper.selectOne(queryWrapper);
        
        if (dbUser != null) {
            // 4. 将数据库查询结果存入 Redis，设置过期时间（30分钟，避免缓存永久存在）
            redisTemplate.opsForValue().set(
                cacheKey,
                dbUser,
                30,  // 过期时间（数值）
                TimeUnit.MINUTES  // 时间单位
            );
            System.out.println("【缓存未命中】从数据库查询并缓存用户：" + username);
        }

        return dbUser;
    }
}
