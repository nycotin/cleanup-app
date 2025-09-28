package com.app.cleanup.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
  private String apiKey;

  public AuthResponse(String apiKey) {
    this.apiKey = apiKey;
  }
}
