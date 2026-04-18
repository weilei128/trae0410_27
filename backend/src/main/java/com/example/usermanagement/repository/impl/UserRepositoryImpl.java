package com.example.usermanagement.repository.impl;

import com.example.usermanagement.model.User;
import com.example.usermanagement.repository.UserRepository;
import com.example.usermanagement.util.JsonFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    
    @Autowired
    private JsonFileUtil jsonFileUtil;

    @Override
    public List<User> findAll() {
        return jsonFileUtil.readUsers();
    }

    @Override
    public Optional<User> findById(Long id) {
        return jsonFileUtil.readUsers().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    @Override
    public User save(User user) {
        List<User> users = jsonFileUtil.readUsers();
        if (user.getId() == null) {
            user.setId(getNextId());
            users.add(user);
        } else {
            users.removeIf(u -> u.getId().equals(user.getId()));
            users.add(user);
        }
        jsonFileUtil.writeUsers(users);
        return user;
    }

    @Override
    public void deleteById(Long id) {
        List<User> users = jsonFileUtil.readUsers();
        users.removeIf(user -> user.getId().equals(id));
        jsonFileUtil.writeUsers(users);
    }

    @Override
    public Long getNextId() {
        return jsonFileUtil.readUsers().stream()
                .mapToLong(User::getId)
                .max()
                .orElse(0L) + 1;
    }
}
