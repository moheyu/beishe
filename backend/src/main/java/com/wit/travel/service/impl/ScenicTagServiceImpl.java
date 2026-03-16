package com.wit.travel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wit.travel.entity.ScenicTag;
import com.wit.travel.mapper.ScenicTagMapper;
import com.wit.travel.service.ScenicTagService;
import org.springframework.stereotype.Service;

/**
 * 景点标签Service实现类
 */
@Service
public class ScenicTagServiceImpl extends ServiceImpl<ScenicTagMapper, ScenicTag> implements ScenicTagService {
}
