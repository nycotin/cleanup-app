package com.app.cleanup.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.app.cleanup.controllers.TaskController;
import com.app.cleanup.entities.Task;
import com.app.cleanup.services.TaskService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc
public class TaskControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private TaskService taskService;

  @Test
  @DisplayName("GET /api/tasks - Should return all users with correct structure")
  void shouldFindAllTasks() throws Exception {

    List<Task> mockTasks =
        Arrays.asList(
            createMockTask(1L, "Setup registration and check-in station", 1L, 2L),
            createMockTask(2L, "Distribute cleanup supplies and safety equipment", 1L, 3L));

    given(taskService.getAllTasks()).willReturn(mockTasks);

    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/tasks"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id", Matchers.is(1)))
        .andExpect(
            MockMvcResultMatchers.jsonPath(
                "$.[0].name", Matchers.is("Setup registration and check-in station")))
        .andExpect(MockMvcResultMatchers.jsonPath("$.[0].author_id", Matchers.is(1)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.[0].assignee_id", Matchers.is(2)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.[1].id", Matchers.is(2)))
        .andExpect(
            MockMvcResultMatchers.jsonPath(
                "$.[1].name", Matchers.is("Distribute cleanup supplies and safety equipment")))
        .andExpect(MockMvcResultMatchers.jsonPath("$.[1].author_id", Matchers.is(1)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.[1].assignee_id", Matchers.is(3)));

    verify(taskService).getAllTasks();
  }

  @Test
  @DisplayName("GET /api/tasks/1 - Should return user 1 with correct structure")
  void shouldFindTasksById() throws Exception {
    Task mockTask = createMockTask(1L, "Setup registration and check-in station", 1L, 2L);

    given(taskService.getTaskById(1L)).willReturn(Optional.of(mockTask));

    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/tasks/1"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
        .andExpect(
            MockMvcResultMatchers.jsonPath(
                "$.name", Matchers.is("Setup registration and check-in station")))
        .andExpect(MockMvcResultMatchers.jsonPath("$.author_id", Matchers.is(1)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.assignee_id", Matchers.is(2)));

    verify(taskService).getTaskById(1L);
  }

  private Task createMockTask(Long id, String name, Long authorId, Long assigneeId) {
    Task task = new Task();
    task.setId(id);
    task.setName(name);
    task.setAuthorId(authorId);
    task.setassigneeId(assigneeId);
    return task;
  }
}
