package com.app.cleanup.utilities;

import com.app.cleanup.entities.User;
import com.app.cleanup.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Configuration
@Profile({"dev", "test"})
public class DataLoader {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    @Autowired
    private UserRepository userRepository;

    @Bean
    public CommandLineRunner loadData() {
        return args -> {
            // Only load sample data if the users table is empty
            if (userRepository.count() == 0) {
                logger.info("Loading sample users into the database...");
                loadUsersFromJson();
                logger.info("Sample users loaded successfully!");
            } else {
                logger.info("Users already exist in the database. Skipping sample data load.");
            }
        };
    }

    private void loadUsersFromJson() {
        try {
            // Load the JSON file from resources
            ClassPathResource resource = new ClassPathResource("sample-users.json");
            InputStream inputStream = resource.getInputStream();

            // Parse the JSON
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, List<Map<String, String>>> usersMap = objectMapper.readValue(
                    inputStream,
                    new TypeReference<Map<String, List<Map<String, String>>>>() {}
            );

            List<Map<String, String>> usersList = usersMap.get("users");

            // Convert and save users
            for (Map<String, String> userData : usersList) {
                String username = userData.get("username");
                String password = userData.get("password");

                // Skip if username or password is missing
                if (username == null || password == null) {
                    logger.warn("Skipping user with missing username or password");
                    continue;
                }

                // Create and save user
                User user = new User(username, password);
                userRepository.save(user);
                logger.debug("Saved user: {}", username);
            }

        } catch (IOException e) {
            logger.error("Failed to load sample users from JSON", e);
        }
    }
}