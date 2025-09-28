package com.app.cleanup.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.app.cleanup.controllers.AuthController;
import com.app.cleanup.dto.AuthRequest;
import com.app.cleanup.services.AuthService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc
public class AuthControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private AuthService authService;

  @Test
  @DisplayName("POST /api/auth/login - Should authenticate user and return API key")
  void shouldAuthenticateUserAndReturnApiKey() throws Exception {

    String expectedApiKey = "abc123-def456-ghi789";

    given(authService.authenticate(any(AuthRequest.class))).willReturn(expectedApiKey);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"john.doe\",\"password\":\"J0hnD03#Pass2025\"}"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.apiKey", Matchers.is(expectedApiKey)));

    verify(authService).authenticate(any(AuthRequest.class));
  }

  @Test
  @DisplayName("POST /api/auth/login - Should return 401 for invalid credentials")
  void shouldReturnUnauthorizedForInvalidCredentials() throws Exception {
    given(authService.authenticate(any(AuthRequest.class)))
        .willThrow(new RuntimeException("Invalid credentials"));

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"john.doe\",\"password\":\"wrongpass\"}"))
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());

    verify(authService).authenticate(any(AuthRequest.class));
  }
}
