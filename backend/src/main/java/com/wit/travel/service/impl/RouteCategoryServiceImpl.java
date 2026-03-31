package com.wit.travel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wit.travel.entity.RouteCategory;
import com.wit.travel.mapper.RouteCategoryMapper;
import com.wit.travel.service.RouteCategoryService;
import org.springframework.stereotype.Service;

/**
 * 路线分类 Service 实现
 */
@Service
public class RouteCategoryServiceImpl extends ServiceImpl<RouteCategoryMapper, RouteCategory>
        implements RouteCategoryService {
}
