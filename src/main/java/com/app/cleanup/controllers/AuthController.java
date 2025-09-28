package com.app.cleanup.controllers;

import com.app.cleanup.dto.AuthRequest;
import com.app.cleanup.dto.AuthResponse;
import com.app.cleanup.services.AuthService;
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
  public ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthRequest authRequest) {
    try {
      String apiKey = authService.authenticate(authRequest);
      return new ResponseEntity<>(new AuthResponse(apiKey), HttpStatus.OK);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  @PostMapping("/register")
  public ResponseEntity<AuthResponse> createUser(@RequestBody AuthRequest authRequest) {
    try {
      String apiKey = authService.authenticate(authRequest);
      return new ResponseEntity<>(new AuthResponse(apiKey), HttpStatus.OK);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }
}
