package com.app.cleanup.controllers;

import com.app.cleanup.entities.Task;
import com.app.cleanup.security.UserContext;
import com.app.cleanup.services.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  private Long getCurrentUserId(HttpServletRequest request) {
    Long userId = UserContext.getCurrentUserId();
    if (userId == null) {
      userId = (Long) request.getAttribute("currentUserId");
    }
    if (userId == null) {
      throw new IllegalStateException("User ID not found in context or request");
    }
    return userId;
  }

  @GetMapping()
  public ResponseEntity<List<Task>> getAllTasks() {
    List<Task> taskList = taskService.listTasks();
    return ResponseEntity.ok(taskList);
  }

  @GetMapping("/{taskId}")
  public ResponseEntity<Task> getTask(@PathVariable Long taskId) {
    Task task = taskService.getTaskById(taskId);
    return ResponseEntity.ok(task);
  }

  @PostMapping()
  public ResponseEntity<Task> createTask(@RequestBody Task task, HttpServletRequest request) {
    Long currentUserId = getCurrentUserId(request);
    task.setAuthorId(currentUserId);

    Task newTask = taskService.insertTask(task);

    URI location =
            ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(newTask.getId())
                    .toUri();

    return ResponseEntity.created(location).body(newTask);

  }

  @PutMapping("/{taskId}")
  public ResponseEntity<Task> updateTask(
      @PathVariable("taskId") Long taskId, @RequestBody Task task, HttpServletRequest request) {
    Long currentUserId = getCurrentUserId(request);

    Task existingTask = taskService.getTaskById(taskId);
    if (!existingTask.getAuthorId().equals(currentUserId)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    Task updatedTask = taskService.editTask(taskId, task);
    return ResponseEntity.ok(updatedTask);
  }

  @DeleteMapping("/{taskId}")
  public ResponseEntity<Void> deleteTask(
      @PathVariable("taskId") Long taskId, HttpServletRequest request) {
    Long currentUserId = getCurrentUserId(request);

    Task existingTask = taskService.getTaskById(taskId);
    if (!existingTask.getAuthorId().equals(currentUserId)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    taskService.removeTask(taskId);
    return ResponseEntity.noContent().build();
  }
}
