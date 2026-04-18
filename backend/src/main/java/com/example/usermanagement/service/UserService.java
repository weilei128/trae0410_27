package com.example.usermanagement.service;

import com.example.usermanagement.dto.UserDTO;
import com.example.usermanagement.entity.User;

import java.util.List;

public interface UserService {
    
    List<User> getAllUsers();
    
    User getUserById(Long id);
    
    User createUser(UserDTO userDTO);
    
    User updateUser(Long id, UserDTO userDTO);
    
    void deleteUser(Long id);
}
