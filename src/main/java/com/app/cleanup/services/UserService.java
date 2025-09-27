package com.app.cleanup.services;

import com.app.cleanup.entities.User;
import com.app.cleanup.repositories.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  // Only for DataLoader
  public void addListOfUsers(List<User> users) {
    userRepository.saveAll(users);
  }

  public List<User> getAllUsers() {
    return userRepository.findAll();
  }
}
