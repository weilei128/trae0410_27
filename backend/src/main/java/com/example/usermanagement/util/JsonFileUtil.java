package com.example.usermanagement.util;

import com.example.usermanagement.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
public class JsonFileUtil {

    private static final Logger logger = LoggerFactory.getLogger(JsonFileUtil.class);

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private File jsonFile;

    private File getJsonFile() throws IOException {
        if (jsonFile == null || !jsonFile.exists()) {
            ClassPathResource resource = new ClassPathResource("users.json");
            try (InputStream inputStream = resource.getInputStream()) {
                File tempFile = File.createTempFile("users", ".json");
                tempFile.deleteOnExit();
                Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                this.jsonFile = tempFile;
                logger.info("JSON 文件已初始化: {}", tempFile.getAbsolutePath());
            }
        }
        return jsonFile;
    }

    public List<User> readUsers() {
        lock.readLock().lock();
        try {
            File file = getJsonFile();
            if (file.length() == 0) {
                logger.debug("JSON 文件为空，返回空列表");
                return new ArrayList<>();
            }
            List<User> users = objectMapper.readValue(file, new TypeReference<List<User>>() {});
            logger.debug("从 JSON 文件读取了 {} 个用户", users.size());
            return users;
        } catch (IOException e) {
            logger.error("读取 JSON 文件失败", e);
            return new ArrayList<>();
        } finally {
            lock.readLock().unlock();
        }
    }

    public void writeUsers(List<User> users) {
        lock.writeLock().lock();
        try {
            File file = getJsonFile();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, users);
            logger.debug("已将 {} 个用户写入 JSON 文件", users.size());
        } catch (IOException e) {
            logger.error("写入 JSON 文件失败", e);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
