package com.wit.travel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wit.travel.entity.Announcement;
import com.wit.travel.mapper.AnnouncementMapper;
import com.wit.travel.service.AnnouncementService;
import com.wit.travel.vo.AnnouncementVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements AnnouncementService {
    @Override
    public AnnouncementVO getAnnouncementVOById(Long id) {
        return baseMapper.selectAnnouncementVOById(id);
    }

    @Override
    public List<AnnouncementVO> getAnnouncementVOList() {
        return baseMapper.selectAnnouncementVOList();
    }

    @Override
    public boolean save(Announcement announcement) {
        return super.save(announcement);
    }

    @Override
    public boolean updateById(Announcement announcement) {
        return super.updateById(announcement);
    }

    @Override
    public boolean removeById(Long id) {
        return super.removeById(id);
    }
}
