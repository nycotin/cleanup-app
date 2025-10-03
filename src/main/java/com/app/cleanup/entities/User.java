package com.app.cleanup.entities;

import com.app.cleanup.enums.Role;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 50)
  private String username;

  @Column(nullable = false, length = 100)
  private String password;

  @Column(unique = true)
  private String apiKey;

  @Enumerated(EnumType.STRING)
  @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
  @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
  @Column(name = "roles")
  private Set<Role> roles = new HashSet<>();

  public User() {}

  public User(String username, String password) {
    this.username = username;
    this.password = password;
  }

  // Generate API key on user creation
  @PrePersist
  public void generateApiKey() {
    if (this.apiKey == null) {
      this.apiKey = UUID.randomUUID().toString();
    }
  }
}
