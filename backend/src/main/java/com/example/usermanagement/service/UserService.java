package com.example.usermanagement.service;

import com.example.usermanagement.model.User;
import com.example.usermanagement.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        logger.info("获取所有用户列表");
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        logger.info("获取用户信息，ID: {}", id);
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        logger.info("创建新用户: {}", user.getName());
        return userRepository.save(user);
    }

    public Optional<User> updateUser(Long id, User userDetails) {
        logger.info("更新用户信息，ID: {}", id);
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setName(userDetails.getName());
            user.setAge(userDetails.getAge());
            user.setEmail(userDetails.getEmail());
            return Optional.of(userRepository.save(user));
        }
        logger.warn("未找到用户，ID: {}", id);
        return Optional.empty();
    }

    public boolean deleteUser(Long id) {
        logger.info("删除用户，ID: {}", id);
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
            return true;
        }
        logger.warn("未找到要删除的用户，ID: {}", id);
        return false;
    }
}
