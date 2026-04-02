package com.wit.travel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wit.travel.entity.Announcement;
import com.wit.travel.vo.AnnouncementVO;

import java.util.List;

public interface AnnouncementService extends IService<Announcement> {

    AnnouncementVO getAnnouncementVOById(Long id);

    List<AnnouncementVO> getAnnouncementVOList();

    boolean save(Announcement announcement);

    boolean updateById(Announcement announcement);

    boolean removeById(Long id);
}
