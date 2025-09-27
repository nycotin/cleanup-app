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

  public List<Task> getAllTasks() {
    return taskRepository.findAll();
  }
}
