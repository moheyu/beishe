package com.wit.travel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wit.travel.entity.UserCollection;
import com.wit.travel.vo.RouteVO;
import com.wit.travel.vo.ScenicVO;

import java.util.List;

/**
 * 用户收藏Service接口
 */
public interface UserCollectionService extends IService<UserCollection> {

    List<ScenicVO> getScenicVOByUserId(Long userId);

    List<RouteVO> getRouteVOByUserId(Long userId);

    Boolean checkCollectionExists(Long userId, Long scenicId);
}
