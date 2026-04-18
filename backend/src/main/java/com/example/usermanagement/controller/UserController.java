package com.example.usermanagement.controller;

import com.example.usermanagement.common.Result;
import com.example.usermanagement.model.User;
import com.example.usermanagement.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Result<List<User>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return Result.success(users);
        } catch (Exception e) {
            logger.error("获取用户列表失败", e);
            return Result.error("获取用户列表失败");
        }
    }

    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable Long id) {
        try {
            Optional<User> user = userService.getUserById(id);
            return user.map(Result::success)
                    .orElse(Result.error(404, "用户不存在"));
        } catch (Exception e) {
            logger.error("获取用户信息失败，ID: {}", id, e);
            return Result.error("获取用户信息失败");
        }
    }

    @PostMapping
    public Result<User> createUser(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return Result.success("用户创建成功", createdUser);
        } catch (Exception e) {
            logger.error("创建用户失败", e);
            return Result.error("创建用户失败");
        }
    }

    @PutMapping("/{id}")
    public Result<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        try {
            Optional<User> updatedUser = userService.updateUser(id, userDetails);
            return updatedUser.map(user -> Result.success("用户更新成功", user))
                    .orElse(Result.error(404, "用户不存在"));
        } catch (Exception e) {
            logger.error("更新用户失败，ID: {}", id, e);
            return Result.error("更新用户失败");
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        try {
            boolean deleted = userService.deleteUser(id);
            if (deleted) {
                return Result.success("用户删除成功", null);
            } else {
                return Result.error(404, "用户不存在");
            }
        } catch (Exception e) {
            logger.error("删除用户失败，ID: {}", id, e);
            return Result.error("删除用户失败");
        }
    }
}
