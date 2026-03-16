package com.wit.travel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wit.travel.entity.UserPreference;
import com.wit.travel.vo.PreferenceDetailVO;
import com.wit.travel.vo.ScenicVO;

import java.util.List;

/**
 * 用户偏好Service接口
 */
public interface UserPreferenceService extends IService<UserPreference> {

    List<PreferenceDetailVO> getPreferenceDetailByUserId(Long userId);

    List<ScenicVO> getRecommendScenicByUserId(Long userId);
}
