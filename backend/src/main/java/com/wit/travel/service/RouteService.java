package com.wit.travel.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wit.travel.dto.RouteQueryDTO;
import com.wit.travel.entity.TravelRoute;
import com.wit.travel.vo.RouteDetailVO;
import com.wit.travel.vo.RouteVO;

import java.util.List;

/**
 * 旅游路线 Service 接口
 */
public interface RouteService extends IService<TravelRoute> {

    RouteVO getRouteVOById(Long id);

    RouteDetailVO getRouteDetailVOById(Long id);

    IPage<RouteVO> getRouteVOList(Page<RouteVO> page, RouteQueryDTO queryDTO);

    List<RouteVO> getRecommendRouteVOList(Integer limit);

    List<RouteVO> getRouteVOByCategoryId(Long categoryId);
}
