package com.wit.travel.constant;

/**
 * 缓存 Key 常量
 * 统一管理 Redis 缓存键的命名规范，格式：模块:业务:标识
 */
public final class CacheKeyConstants {

    private CacheKeyConstants() {
    }

    /** 用户信息缓存，参数：username */
    public static final String USER_INFO_KEY = "travel:user:info:%s";

    /** 景点推荐列表缓存，参数：limit */
    public static final String SCENIC_RECOMMEND_KEY = "travel:scenic:recommend:%d";

    /** 用户偏好推荐缓存，参数：userId */
    public static final String USER_PREFERENCE_RECOMMEND_KEY = "travel:preference:recommend:%d";
}
