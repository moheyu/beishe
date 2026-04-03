package com.wit.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wit.travel.entity.ViewRecord;
import com.wit.travel.vo.ScenicVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 浏览记录Mapper接口
 */
@Mapper
public interface ViewRecordMapper extends BaseMapper<ViewRecord> {

    List<ScenicVO> selectScenicVOByUserId(@Param("userId") Long userId);

    Long selectTotalViews();

    Long selectTotalViewsByUserId(@Param("userId") Long userId);

    List<Long> selectOldestRecordIds(@Param("userId") Long userId, @Param("limit") int limit);
}
