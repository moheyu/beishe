package com.wit.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wit.travel.dto.RouteQueryDTO;
import com.wit.travel.entity.TravelRoute;
import com.wit.travel.vo.RouteDetailVO;
import com.wit.travel.vo.RouteVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 旅游路线 Mapper
 */
@Mapper
public interface RouteMapper extends BaseMapper<TravelRoute> {

    RouteVO selectRouteVOById(@Param("id") Long id);

    RouteDetailVO selectRouteDetailVOById(@Param("id") Long id);

    IPage<RouteVO> selectRouteVOList(Page<RouteVO> page, @Param("query") RouteQueryDTO queryDTO);

    List<RouteVO> selectRecommendRouteVOList(@Param("limit") Integer limit);

    List<RouteVO> selectRouteVOByCategoryId(@Param("categoryId") Long categoryId);
}
