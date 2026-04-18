package com.example.usermanagement.service.impl;

import com.example.usermanagement.exception.BusinessException;
import com.example.usermanagement.model.User;
import com.example.usermanagement.repository.UserRepository;
import com.example.usermanagement.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        logger.info("查询所有用户");
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        logger.info("查询用户: id={}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("用户不存在"));
    }

    @Override
    public User createUser(User user) {
        logger.info("创建用户: name={}", user.getName());
        validateUser(user);
        user.setId(null);
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User user) {
        logger.info("更新用户: id={}", id);
        userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        validateUser(user);
        user.setId(id);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        logger.info("删除用户: id={}", id);
        userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        userRepository.deleteById(id);
    }

    private void validateUser(User user) {
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new BusinessException("用户姓名不能为空");
        }
        if (user.getAge() == null || user.getAge() < 0 || user.getAge() > 150) {
            throw new BusinessException("用户年龄不合法");
        }
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new BusinessException("邮箱格式不正确");
        }
    }
}
