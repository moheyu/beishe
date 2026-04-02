package com.wit.travel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wit.travel.entity.RouteScenicRelation;
import com.wit.travel.vo.ScenicVO;

import java.util.List;

public interface RouteScenicRelationService extends IService<RouteScenicRelation> {

    List<ScenicVO> getScenicVOByRouteId(Long routeId);
}
