package com.wit.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wit.travel.entity.UserPreference;
import com.wit.travel.vo.PreferenceDetailVO;
import com.wit.travel.vo.ScenicVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户偏好Mapper接口
 */
@Mapper
public interface UserPreferenceMapper extends BaseMapper<UserPreference> {

    List<PreferenceDetailVO> selectPreferenceDetailByUserId(@Param("userId") Long userId);

    List<ScenicVO> selectScenicVOByUserId(@Param("userId") Long userId);

    List<Object> selectPreferenceStatistics();

    List<ScenicVO> selectRecommendByBrowseHistory(@Param("userId") Long userId);
}
