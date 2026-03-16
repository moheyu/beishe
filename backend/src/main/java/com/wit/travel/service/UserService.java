package com.wit.travel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wit.travel.entity.User;
import java.util.List;

/**
 * 用户Service接口
 */
public interface UserService extends IService<User> {
    List<User> listAllUsers();
    User getUserById(Long id);
    User getUserByUsername(String username);
    boolean updateUser(User user);
    boolean deleteUser(Long id);
}
