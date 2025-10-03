package com.app.cleanup.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.app.cleanup.entities.Task;
import com.app.cleanup.entities.User;
import com.app.cleanup.enums.Role;
import com.app.cleanup.interceptor.ApiKeyInterceptor;
import com.app.cleanup.repositories.TaskRepository;
import com.app.cleanup.security.ApiKeyService;
import com.app.cleanup.security.UserContext;
import com.app.cleanup.services.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(
    controllers = TaskController.class,
    excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
public class TaskControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockitoBean private TaskService taskService;
  @MockitoBean private TaskRepository taskRepository;
  @MockitoBean private ApiKeyService apiKeyService;
  @MockitoBean private ApiKeyInterceptor apiKeyInterceptor;

  private User mockUser;

  @BeforeEach
  void setUp() throws Exception {
    // Return true in pre-handle to allow access to taskController
    given(
            apiKeyInterceptor.preHandle(
                any(HttpServletRequest.class), any(HttpServletResponse.class), any(Object.class)))
        .willReturn(true);

    // Create a mock user and set it in UserContext for all tests
    mockUser = new User();
    mockUser.setId(4L);
    mockUser.setUsername("test.user");
    mockUser.setApiKey("test-api-key");
    mockUser.setRoles(Set.of(Role.USER));

    UserContext.setCurrentUser(mockUser);
  }

  @AfterEach
  void tearDown() {
    // Clean up UserContext after each test
    UserContext.clear();
  }

  @Test
  @DisplayName("GET /api/tasks - Should return all tasks with correct structure")
  void shouldFindAllTasks() throws Exception {

    List<Task> mockTasks =
        Arrays.asList(
            createMockTask(1L, "Setup registration and check-in station", 1L, 2L),
            createMockTask(2L, "Distribute cleanup supplies and safety equipment", 1L, 3L));

    given(taskService.listTasks()).willReturn(mockTasks);

    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/tasks").requestAttr("currentUserId", 1L))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
        .andExpect(
            MockMvcResultMatchers.jsonPath(
                "$[0].name", Matchers.is("Setup registration and check-in station")))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].authorId", Matchers.is(1)))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].assigneeId", Matchers.is(2)))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.is(2)))
        .andExpect(
            MockMvcResultMatchers.jsonPath(
                "$[1].name", Matchers.is("Distribute cleanup supplies and safety equipment")))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].authorId", Matchers.is(1)))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].assigneeId", Matchers.is(3)));

    verify(taskService).listTasks();
  }

  @Test
  @DisplayName("GET /api/tasks/1 - Should return task 1 with correct structure")
  void shouldFindTasksById() throws Exception {
    Task mockTask = createMockTask(1L, "Setup registration and check-in station", 4L, 2L);

    given(taskService.getTaskById(1L)).willReturn(mockTask);

    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/tasks/1").requestAttr("currentUserId", 4L))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
        .andExpect(
            MockMvcResultMatchers.jsonPath(
                "$.name", Matchers.is("Setup registration and check-in station")))
        .andExpect(MockMvcResultMatchers.jsonPath("$.authorId", Matchers.is(4)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.assigneeId", Matchers.is(2)));

    verify(taskService).getTaskById(1L);
  }

  @Test
  @DisplayName("POST /api/tasks - Should create new task with current user as author")
  void shouldCreateNewTask() throws Exception {
    Task savedTask = createMockTask(3L, "Coordinate trash collection and sorting stations", 4L, 2L);

    given(taskService.insertTask(any(Task.class))).willReturn(savedTask);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/tasks")
                .requestAttr("currentUserId", 4L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"name\":\"Coordinate trash collection and sorting stations\",\"assigneeId\":2}"))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(3)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.authorId", Matchers.is(4)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.assigneeId", Matchers.is(2)));

    verify(taskService).insertTask(any(Task.class));
  }

  @Test
  @DisplayName("PUT /api/tasks/4 - Should update task when user is author")
  void shouldEditTaskByAssigningNewAssignee() throws Exception {
    Task existingTask = createMockTask(4L, "Document cleanup progress", 4L, null);
    Task updatedTask =
        createMockTask(4L, "Document cleanup progress and take before/after photos", 4L, 2L);

    given(taskService.getTaskById(4L)).willReturn(existingTask);
    given(taskService.editTask(any(Long.class), any(Task.class))).willReturn(updatedTask);

    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/api/tasks/4")
                .requestAttr("currentUserId", 4L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"name\":\"Document cleanup progress and take before/after photos\",\"assigneeId\":2}"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(4)))
        .andExpect(
            MockMvcResultMatchers.jsonPath(
                "$.name", Matchers.is("Document cleanup progress and take before/after photos")))
        .andExpect(MockMvcResultMatchers.jsonPath("$.assigneeId", Matchers.is(2)));

    verify(taskService).getTaskById(4L);
    verify(taskService).editTask(any(Long.class), any(Task.class));
  }

  @Test
  @DisplayName("DELETE /api/tasks/1 - Should delete task when user is author")
  void shouldDeleteTask() throws Exception {

    Task existingTask = createMockTask(1L, "Setup registration and check-in station", 4L, 2L);

    given(taskService.getTaskById(1L)).willReturn(existingTask);
    doNothing().when(taskService).removeTask(1L);

    mockMvc
        .perform(MockMvcRequestBuilders.delete("/api/tasks/1").requestAttr("currentUserId", 4L))
        .andExpect(MockMvcResultMatchers.status().isNoContent());

    verify(taskService).getTaskById(1L);
    verify(taskService).removeTask(1L);
  }

  @Test
  @DisplayName("DELETE /api/tasks/1 - Should return 403 when user is not author")
  void shouldReturnForbiddenWhenDeletingOthersTask() throws Exception {
    Task existingTask = createMockTask(1L, "Someone else's task", 5L, 3L);

    given(taskService.getTaskById(1L)).willReturn(existingTask);

    mockMvc
        .perform(MockMvcRequestBuilders.delete("/api/tasks/1").requestAttr("currentUserId", 4L))
        .andExpect(MockMvcResultMatchers.status().isForbidden());

    verify(taskService).getTaskById(1L);
    // removeTask should NOT be called
  }

  private Task createMockTask(Long id, String name, Long authorId, Long assigneeId) {
    Task task = new Task();
    task.setId(id);
    task.setName(name);
    task.setAuthorId(authorId);
    task.setAssigneeId(assigneeId);
    return task;
  }
}
