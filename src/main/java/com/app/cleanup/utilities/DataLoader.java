package com.app.cleanup.utilities;

import com.app.cleanup.entities.Task;
import com.app.cleanup.entities.User;
import com.app.cleanup.services.TaskService;
import com.app.cleanup.services.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

  private final UserService userService;
  private final TaskService taskService;

  @Value("${app.data.initialization.enabled:true}")
  private boolean initializationEnabled;

  // Constructor-based injection (Spring will automatically inject these)
  public DataLoader(UserService userService, TaskService taskService) {
    this.userService = userService;
    this.taskService = taskService;
  }

  @EventListener(ApplicationReadyEvent.class)
  private void loadDataFromJson() {
    if (!initializationEnabled) {
      System.out.println("Data initialization is disabled");
      return;
    }

    try {
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.registerModule(new JavaTimeModule());

      loadUsers(objectMapper);
      loadTasks(objectMapper);

      System.out.println("Data initialization completed successfully!");

    } catch (Exception e) {
      System.err.println("Error during data initialization: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private void loadUsers(ObjectMapper objectMapper) throws IOException {
    ClassPathResource usersResource = new ClassPathResource("static/sample-users.json");

    if (!usersResource.exists()) {
      System.err.println("User data file not found: static/sample-users.json");
      return;
    }

    List<User> userList =
        objectMapper.readValue(usersResource.getInputStream(), new TypeReference<List<User>>() {});

    if (userList != null && !userList.isEmpty()) {
      userService.addListOfUsers(userList);
      System.out.println("Loaded " + userList.size() + " users successfully");
    }
  }

  private void loadTasks(ObjectMapper objectMapper) throws IOException {
    ClassPathResource tasksResource = new ClassPathResource("static/sample-tasks.json");

    if (!tasksResource.exists()) {
      System.err.println("Task data file not found: static/sample-tasks.json");
      return;
    }

    List<Task> taskList =
        objectMapper.readValue(tasksResource.getInputStream(), new TypeReference<List<Task>>() {});

    if (taskList != null && !taskList.isEmpty()) {
      taskService.addListOfTasks(taskList);
      System.out.println("Loaded " + taskList.size() + " tasks successfully");
    }
  }
}
