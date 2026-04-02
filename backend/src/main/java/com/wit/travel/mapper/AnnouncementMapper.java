package com.wit.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wit.travel.entity.Announcement;
import com.wit.travel.vo.AnnouncementVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AnnouncementMapper extends BaseMapper<Announcement> {

    AnnouncementVO selectAnnouncementVOById(@Param("id") Long id);

    List<AnnouncementVO> selectAnnouncementVOList();
}
