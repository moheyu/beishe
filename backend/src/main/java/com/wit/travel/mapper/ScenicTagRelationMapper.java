package com.wit.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wit.travel.entity.ScenicTagRelation;
import org.apache.ibatis.annotations.Mapper;

/**
 * 景点-标签关联Mapper接口
 */
@Mapper
public interface ScenicTagRelationMapper extends BaseMapper<ScenicTagRelation> {
}
