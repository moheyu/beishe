package com.wit.travel.controller;

import com.wit.travel.dto.UserUpdateDTO;
import com.wit.travel.entity.User;
import com.wit.travel.service.UserService;
import com.wit.travel.util.SecurityUtil;
import com.wit.travel.vo.Result;
import com.wit.travel.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/info")
    public Result<UserVO> getUserInfo() {
        Long userId = SecurityUtil.getCurrentUserId();
        User user = userService.getById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        if (user.getCreateTime() != null) {
            userVO.setCreateTime(user.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        if (userVO.getAvatar() == null) {
            userVO.setAvatar("");
        }
        return Result.success(userVO);
    }

    @PutMapping("/update")
    public Result<String> updateUser(@RequestBody UserUpdateDTO userUpdateDTO) {
        Long userId = SecurityUtil.getCurrentUserId();
        User user = userService.getById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }

        if (userUpdateDTO.getNickname() != null) {
            user.setNickname(userUpdateDTO.getNickname());
        }
        if (userUpdateDTO.getAvatar() != null) {
            user.setAvatar(userUpdateDTO.getAvatar());
        }
        userService.updateById(user);

        return Result.success("更新成功");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    @GetMapping("/list")
    public Result<List<UserVO>> listUsers() {
        List<User> userList = userService.listAllUsers();
        List<UserVO> userVOList = userList.stream().map(user -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            if (user.getCreateTime() != null) {
                userVO.setCreateTime(user.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
            if (userVO.getAvatar() == null) {
                userVO.setAvatar("");
            }
            return userVO;
        }).collect(Collectors.toList());
        return Result.success(userVOList);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    @GetMapping("/{id}")
    public Result<UserVO> getUserById(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        if (user.getCreateTime() != null) {
            userVO.setCreateTime(user.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        if (userVO.getAvatar() == null) {
            userVO.setAvatar("");
        }
        return Result.success(userVO);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    @PutMapping("/{id}")
    public Result<String> updateUserById(@PathVariable Long id, @RequestBody User user) {
        User existingUser = userService.getById(id);
        if (existingUser == null) {
            return Result.error("用户不存在");
        }
        user.setId(id);
        userService.updateById(user);
        return Result.success("更新成功");
    }

    @PreAuthorize("hasRole('ROOT')")
    @DeleteMapping("/{id}")
    public Result<String> deleteUser(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        userService.removeById(id);
        return Result.success("删除成功");
    }
}
