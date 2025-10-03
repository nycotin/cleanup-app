package com.app.cleanup.services;

import com.app.cleanup.entities.Task;
import com.app.cleanup.exceptions.TaskNotFoundException;
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
    return taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
  }

  public Task insertTask(Task task) {
    return taskRepository.save(task);
  }

  public Task editTask(Long taskId, Task task) {
    Task currentTask = getTaskById(taskId);

    currentTask.setName(task.getName());
    currentTask.setAssigneeId(task.getAssigneeId());

    return taskRepository.save(currentTask);
  }

  public void removeTask(Long taskId) {
    taskRepository.deleteById(taskId);
  }
}
