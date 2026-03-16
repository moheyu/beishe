package com.wit.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wit.travel.entity.UserCollection;
import com.wit.travel.vo.ScenicVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户收藏Mapper接口
 */
@Mapper
public interface UserCollectionMapper extends BaseMapper<UserCollection> {

    List<ScenicVO> selectScenicVOByUserId(@Param("userId") Long userId);

    Integer checkCollectionExists(@Param("userId") Long userId, @Param("scenicId") Long scenicId);
}
