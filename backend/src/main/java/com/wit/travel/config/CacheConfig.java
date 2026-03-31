package com.wit.travel.config;

import org.springframework.context.annotation.Configuration;

/**
 * 缓存配置。
 * <p>项目使用 Redis（{@code RedisTemplate}）作为唯一缓存层，
 * 不再注册 {@code ConcurrentMapCacheManager}，避免两套缓存并存导致数据不一致。
 */
@Configuration
public class CacheConfig {
}