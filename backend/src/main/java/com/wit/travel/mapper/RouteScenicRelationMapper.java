package com.wit.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wit.travel.entity.RouteScenicRelation;
import com.wit.travel.vo.ScenicVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RouteScenicRelationMapper extends BaseMapper<RouteScenicRelation> {

    List<ScenicVO> selectScenicVOByRouteId(@Param("routeId") Long routeId);
}
