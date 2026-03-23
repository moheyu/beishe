package com.wit.travel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wit.travel.entity.UserCollection;
import com.wit.travel.mapper.UserCollectionMapper;
import com.wit.travel.service.UserCollectionService;
import com.wit.travel.vo.RouteVO;
import com.wit.travel.vo.ScenicVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户收藏Service实现类
 */
@Service
public class UserCollectionServiceImpl extends ServiceImpl<UserCollectionMapper, UserCollection> implements UserCollectionService {

    @Override
    public List<ScenicVO> getScenicVOByUserId(Long userId) {
        return baseMapper.selectScenicVOByUserId(userId);
    }

    @Override
    public List<RouteVO> getRouteVOByUserId(Long userId) {
        return baseMapper.selectRouteVOByUserId(userId);
    }

    @Override
    public Boolean checkCollectionExists(Long userId, Long scenicId) {
        Integer count = baseMapper.checkCollectionExists(userId, scenicId);
        return count != null && count > 0;
    }
}
