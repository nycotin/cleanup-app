package com.app.cleanup.services;

import com.app.cleanup.dto.AuthRequest;
import com.app.cleanup.entities.User;
import com.app.cleanup.repositories.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  // Only for DataLoader
  public void createListOfUsers(List<User> users) {

    for (User user : users) {
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      userRepository.save(user);
    }
  }

  public String authenticate(AuthRequest authRequest) {
    if (isValidCredentials(authRequest.getUsername(), authRequest.getPassword())) {
      return generateApiKey();
    }
    throw new RuntimeException("Invalid credentials");
  }

  private boolean isValidCredentials(String username, String plainPassword) {
    Optional<User> userOptional = userRepository.findByUsername(username);

    if (userOptional.isEmpty()) {
      return false;
    }

    User user = userOptional.get();
    String hashedPasswordFromDb = user.getPassword();

    return passwordEncoder.matches(plainPassword, hashedPasswordFromDb);
  }

  private String generateApiKey() {
    return UUID.randomUUID().toString().replace("-", "");
  }
}
