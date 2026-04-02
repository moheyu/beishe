package com.wit.travel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wit.travel.entity.RouteScenicRelation;
import com.wit.travel.mapper.RouteScenicRelationMapper;
import com.wit.travel.service.RouteScenicRelationService;
import com.wit.travel.vo.ScenicVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteScenicRelationServiceImpl extends ServiceImpl<RouteScenicRelationMapper, RouteScenicRelation> implements RouteScenicRelationService {

    @Override
    public List<ScenicVO> getScenicVOByRouteId(Long routeId) {
        return baseMapper.selectScenicVOByRouteId(routeId);
    }
}
