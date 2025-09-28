package com.app.cleanup.services;

import com.app.cleanup.entities.Task;
import com.app.cleanup.repositories.TaskRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
  private final TaskRepository taskRepository;

  public TaskService(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  // Only for DataLoader
  public void addListOfTasks(List<Task> tasks) {
    taskRepository.saveAll(tasks);
  }

  public List<Task> listTasks() {
    return taskRepository.findAll();
  }

  public Task getTaskById(Long taskId) {
    return taskRepository
        .findById(taskId)
        .orElseThrow(() -> new RuntimeException("Task with id " + taskId + " not found"));
  }

  public Task insertTask(Task task) {
    return taskRepository.save(task);
  }

  public Task editTask(Long taskId, Task task) {
    Task currentTask =
        taskRepository
            .findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task with id " + taskId + " not found"));

    currentTask.setName(task.getName());
    currentTask.setAuthorId(task.getAuthorId());
    currentTask.setAssigneeId(task.getAssigneeId());
    taskRepository.save(currentTask);
    return currentTask;
  }
}
