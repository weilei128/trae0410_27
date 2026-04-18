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

@Component
public class JsonFileUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(JsonFileUtil.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private File jsonFile;

    private File getJsonFile() throws IOException {
        if (jsonFile == null || !jsonFile.exists()) {
            ClassPathResource resource = new ClassPathResource("users.json");
            InputStream inputStream = resource.getInputStream();
            
            File tempFile = File.createTempFile("users", ".json");
            Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            inputStream.close();
            
            this.jsonFile = tempFile;
            logger.info("初始化JSON文件: {}", tempFile.getAbsolutePath());
        }
        return jsonFile;
    }

    public List<User> readUsers() {
        try {
            File file = getJsonFile();
            if (file.length() == 0) {
                logger.info("JSON文件为空，返回空列表");
                return new ArrayList<>();
            }
            List<User> users = objectMapper.readValue(file, new TypeReference<List<User>>() {});
            logger.debug("读取用户数据: {} 条记录", users.size());
            return users;
        } catch (IOException e) {
            logger.error("读取用户数据失败", e);
            return new ArrayList<>();
        }
    }

    public void writeUsers(List<User> users) {
        try {
            File file = getJsonFile();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, users);
            logger.debug("写入用户数据: {} 条记录", users.size());
        } catch (IOException e) {
            logger.error("写入用户数据失败", e);
        }
    }
}
