package com.example.usermanagement.repository;

import com.example.usermanagement.model.User;
import com.example.usermanagement.util.JsonFileUtil;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JsonFileUtil jsonFileUtil;

    public UserRepository(JsonFileUtil jsonFileUtil) {
        this.jsonFileUtil = jsonFileUtil;
    }

    public List<User> findAll() {
        return jsonFileUtil.readUsers();
    }

    public Optional<User> findById(Long id) {
        return jsonFileUtil.readUsers().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    public User save(User user) {
        List<User> users = jsonFileUtil.readUsers();
        if (user.getId() == null) {
            Long maxId = users.stream()
                    .mapToLong(User::getId)
                    .max()
                    .orElse(0L);
            user.setId(maxId + 1);
            users.add(user);
        } else {
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getId().equals(user.getId())) {
                    users.set(i, user);
                    break;
                }
            }
        }
        jsonFileUtil.writeUsers(users);
        return user;
    }

    public void deleteById(Long id) {
        List<User> users = jsonFileUtil.readUsers();
        users.removeIf(user -> user.getId().equals(id));
        jsonFileUtil.writeUsers(users);
    }
}
