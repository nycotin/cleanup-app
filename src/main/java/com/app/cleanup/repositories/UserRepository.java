package com.app.cleanup.repositories;

import com.app.cleanup.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  User findByUsername(User username);

  boolean existsByUsername(String username);
}
