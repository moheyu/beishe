package com.wit.travel.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wit.travel.dto.RouteQueryDTO;
import com.wit.travel.entity.TravelRoute;
import com.wit.travel.mapper.RouteMapper;
import com.wit.travel.service.RouteService;
import com.wit.travel.vo.RouteDetailVO;
import com.wit.travel.vo.RouteVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteServiceImpl extends ServiceImpl<RouteMapper, TravelRoute> implements RouteService {

    @Override
    public RouteVO getRouteVOById(Long id) {
        return baseMapper.selectRouteVOById(id);
    }

    @Override
    public RouteDetailVO getRouteDetailVOById(Long id) {
        return baseMapper.selectRouteDetailVOById(id);
    }

    @Override
    public IPage<RouteVO> getRouteVOList(Page<RouteVO> page, RouteQueryDTO queryDTO) {
        return baseMapper.selectRouteVOList(page, queryDTO);
    }

    @Override
    public List<RouteVO> getRouteVOByCategoryId(Long categoryId) {
        return baseMapper.selectRouteVOByCategoryId(categoryId);
    }

    @Override
    public List<RouteVO> getRecommendRouteVOList(Integer limit) {
        return baseMapper.selectRecommendRouteVOList(limit);
    }

    @Override
    public boolean save(TravelRoute route) {
        return super.save(route);
    }

    @Override
    public boolean updateById(TravelRoute route) {
        return super.updateById(route);
    }

    @Override
    public boolean removeById(Long id) {
        return super.removeById(id);
    }
}
