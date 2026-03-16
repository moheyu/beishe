package com.wit.travel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wit.travel.entity.ScenicCategory;
import com.wit.travel.mapper.ScenicCategoryMapper;
import com.wit.travel.service.ScenicCategoryService;
import org.springframework.stereotype.Service;

/**
 * 景点分类Service实现类
 */
@Service
public class ScenicCategoryServiceImpl extends ServiceImpl<ScenicCategoryMapper, ScenicCategory> implements ScenicCategoryService {
}
