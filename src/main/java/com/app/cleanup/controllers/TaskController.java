package com.app.cleanup.controllers;

import com.app.cleanup.entities.Task;
import com.app.cleanup.services.TaskService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @GetMapping()
  public List<Task> getTasks() {
    return taskService.getAllTasks();
  }
}
