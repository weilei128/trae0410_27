package com.example.usermanagement.repository.impl;

import com.example.usermanagement.entity.User;
import com.example.usermanagement.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Repository
public class JsonFileUserRepository implements UserRepository {
    
    private final ObjectMapper objectMapper;
    private final ReadWriteLock lock;
    private final AtomicLong idGenerator;
    
    @Value("${user.data.file:users.json}")
    private String dataFilePath;
    
    private File dataFile;
    private List<User> users;
    
    public JsonFileUserRepository() {
        this.objectMapper = new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT);
        this.lock = new ReentrantReadWriteLock();
        this.idGenerator = new AtomicLong(0);
        this.users = new ArrayList<>();
    }
    
    @PostConstruct
    public void init() {
        try {
            String dataDir = System.getProperty("user.dir") + File.separator + "data";
            File dir = new File(dataDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            this.dataFile = new File(dir, dataFilePath);
            if (!dataFile.exists()) {
                dataFile.createNewFile();
                saveToFile();
            } else {
                loadFromFile();
            }
        } catch (IOException e) {
            throw new RuntimeException("初始化数据文件失败", e);
        }
    }
    
    private void loadFromFile() {
        lock.writeLock().lock();
        try {
            if (dataFile.length() == 0) {
                users = new ArrayList<>();
            } else {
                users = objectMapper.readValue(dataFile, new TypeReference<List<User>>() {});
            }
            long maxId = users.stream()
                    .mapToLong(User::getId)
                    .max()
                    .orElse(0);
            idGenerator.set(maxId);
        } catch (IOException e) {
            throw new RuntimeException("读取用户数据失败", e);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    private void saveToFile() {
        lock.writeLock().lock();
        try {
            objectMapper.writeValue(dataFile, users);
        } catch (IOException e) {
            throw new RuntimeException("保存用户数据失败", e);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public List<User> findAll() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(users);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public Optional<User> findById(Long id) {
        lock.readLock().lock();
        try {
            return users.stream()
                    .filter(u -> u.getId().equals(id))
                    .findFirst();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public User save(User user) {
        lock.writeLock().lock();
        try {
            if (user.getId() == null) {
                user.setId(getNextId());
                users.add(user);
            } else {
                users.removeIf(u -> u.getId().equals(user.getId()));
                users.add(user);
            }
            saveToFile();
            return user;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public void deleteById(Long id) {
        lock.writeLock().lock();
        try {
            users.removeIf(u -> u.getId().equals(id));
            saveToFile();
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public Long getNextId() {
        return idGenerator.incrementAndGet();
    }
}
