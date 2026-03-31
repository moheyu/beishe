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

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private UserService userService;

    @GetMapping("/info")
    public Result<UserVO> getUserInfo() {
        Long userId = SecurityUtil.getCurrentUserId();
        User user = userService.getById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        return Result.success(toUserVO(user));
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
        List<UserVO> voList = userService.listAllUsers().stream()
                .map(this::toUserVO)
                .collect(Collectors.toList());
        return Result.success(voList);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    @GetMapping("/{id}")
    public Result<UserVO> getUserById(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        return Result.success(toUserVO(user));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    @PutMapping("/{id}")
    public Result<String> updateUserById(@PathVariable Long id, @RequestBody User user) {
        if (userService.getById(id) == null) {
            return Result.error("用户不存在");
        }
        user.setId(id);
        userService.updateById(user);
        return Result.success("更新成功");
    }

    @PreAuthorize("hasRole('ROOT')")
    @DeleteMapping("/{id}")
    public Result<String> deleteUser(@PathVariable Long id) {
        if (userService.getById(id) == null) {
            return Result.error("用户不存在");
        }
        userService.removeById(id);
        return Result.success("删除成功");
    }

    /**
     * 将 User 实体转换为 UserVO，统一处理时间格式和空头像。
     */
    private UserVO toUserVO(User user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        if (user.getCreateTime() != null) {
            vo.setCreateTime(user.getCreateTime().format(DATE_FORMATTER));
        }
        if (vo.getAvatar() == null) {
            vo.setAvatar("");
        }
        return vo;
    }
}
