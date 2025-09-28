package com.app.cleanup.controllers;

import com.app.cleanup.entities.Task;
import com.app.cleanup.services.TaskService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @GetMapping()
  public ResponseEntity<List<Task>> getAllTasks() {
    List<Task> taskList = taskService.listTasks();
    return new ResponseEntity<>(taskList, HttpStatus.OK);
  }

  @GetMapping("/{taskId}")
  public ResponseEntity<Task> getTask(@PathVariable Long taskId) {
    Task task = taskService.getTaskById(taskId);
    return new ResponseEntity<>(task, HttpStatus.OK);
  }

  @PostMapping()
  public ResponseEntity<Task> createTask(@RequestBody Task task) {
    Task newTask = taskService.insertTask(task);
    return new ResponseEntity<>(newTask, HttpStatus.CREATED);
  }

  @PutMapping("/{taskId}")
  public ResponseEntity<Task> updateTask(
      @PathVariable("taskId") Long taskId, @RequestBody Task task) {
    Task updatedTask = taskService.editTask(taskId, task);
    return new ResponseEntity<>(updatedTask, HttpStatus.OK);
  }

  @DeleteMapping("/{taskId}")
  public ResponseEntity<Task> updateTask(@PathVariable("taskId") Long taskId) {
    taskService.deleteTask(taskId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
