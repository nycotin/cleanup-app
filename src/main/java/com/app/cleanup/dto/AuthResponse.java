package com.app.cleanup.dto;

import com.app.cleanup.enums.Role;
import java.util.Set;
import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
  private String apiKey;
  private String username;
  private Set<Role> roles;
}
