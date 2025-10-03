package com.app.cleanup.controllers;

import com.app.cleanup.dto.AuthRequest;
import com.app.cleanup.dto.AuthResponse;
import com.app.cleanup.entities.User;
import com.app.cleanup.services.AuthService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser(@RequestBody AuthRequest authRequest) {
    try {
      User user = authService.authenticate(authRequest);
      return ResponseEntity.ok(
          new AuthResponse(user.getApiKey(), user.getUsername(), user.getRoles()));
    } catch (Exception e) {
      Map<String, String> error = new HashMap<>();
      error.put("error", e.getMessage());
      return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
  }

  // ! TO BE CHNAGE WITH ACTUAL CREATE USER SERVICE
  @PostMapping("/register")
  public ResponseEntity<?> createUser(@RequestBody AuthRequest authRequest) {
    try {
      User user = authService.authenticate(authRequest);
      return ResponseEntity.ok(
          new AuthResponse(user.getApiKey(), user.getUsername(), user.getRoles()));
    } catch (Exception e) {
      Map<String, String> error = new HashMap<>();
      error.put("error", e.getMessage());
      return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
  }
}
