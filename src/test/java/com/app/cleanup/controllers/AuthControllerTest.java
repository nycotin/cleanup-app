package com.app.cleanup.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.app.cleanup.dto.AuthRequest;
import com.app.cleanup.entities.User;
import com.app.cleanup.enums.Role;
import com.app.cleanup.repositories.UserRepository;
import com.app.cleanup.security.ApiKeyService;
import com.app.cleanup.services.AuthService;
import java.util.Set;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockitoBean private AuthService authService;
  @MockitoBean private UserRepository userRepository;
  @MockitoBean private ApiKeyService apiKeyService;
  @MockitoBean private PasswordEncoder passwordEncoder;

  @Test
  @DisplayName("POST /api/auth/login - Should authenticate user and return API key")
  void shouldAuthenticateUserAndReturnApiKey() throws Exception {

    User mockUser = createMockUser(1L, "john.doe", "abc123-def456-ghi789");

    given(authService.authenticate(any(AuthRequest.class))).willReturn(mockUser);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"john.doe\",\"password\":\"J0hnD03#Pass2025\"}"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.apiKey", Matchers.is("abc123-def456-ghi789")))
        .andExpect(MockMvcResultMatchers.jsonPath("$.username", Matchers.is("john.doe")))
        .andExpect(MockMvcResultMatchers.jsonPath("$.roles", Matchers.hasSize(1)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.roles[0]", Matchers.is("USER")));

    verify(authService).authenticate(any(AuthRequest.class));
  }

  @Test
  @DisplayName("POST /api/auth/login - Should return 401 for invalid credentials")
  void shouldReturnBasRequestForInvalidCredentials() throws Exception {
    given(authService.authenticate(any(AuthRequest.class)))
        .willThrow(new RuntimeException("Invalid username or password"));

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"john.doe\",\"password\":\"wrongpass\"}"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Invalid username or password")));

    verify(authService).authenticate(any(AuthRequest.class));
  }

  private User createMockUser(Long id, String username, String apikey) {
    User user = new User();
    user.setId(id);
    user.setUsername(username);
    user.setApiKey(apikey);
    user.setRoles(Set.of(Role.USER));
    return user;
  }
}
