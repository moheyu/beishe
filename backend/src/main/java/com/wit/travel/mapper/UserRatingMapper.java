package com.wit.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wit.travel.entity.UserRating;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

@Mapper
public interface UserRatingMapper extends BaseMapper<UserRating> {

    BigDecimal selectAverageScoreByScenicId(@Param("scenicId") Long scenicId);

    Integer selectRatingCountByScenicId(@Param("scenicId") Long scenicId);
}
