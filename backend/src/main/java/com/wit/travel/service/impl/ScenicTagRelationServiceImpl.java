package com.wit.travel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wit.travel.entity.ScenicTagRelation;
import com.wit.travel.mapper.ScenicTagRelationMapper;
import com.wit.travel.service.ScenicTagRelationService;
import org.springframework.stereotype.Service;

/**
 * 景点-标签关联Service实现类
 */
@Service
public class ScenicTagRelationServiceImpl extends ServiceImpl<ScenicTagRelationMapper, ScenicTagRelation> implements ScenicTagRelationService {
}
