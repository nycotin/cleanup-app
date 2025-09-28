package com.app.cleanup.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
@Table(name = "tasks")
public class Task {
  @Getter
  @Setter
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter
  @Getter
  @Column(nullable = false, length = 100, name = "name")
  private String name;

  @Setter
  @Getter
  @Column(nullable = false, name = "author_id")
  private Long authorId;

  @Setter
  @Getter
  @Column(name = "assignee_id")
  private Long assigneeId;

  public Task() {}

  public Task(String name, Long authorId) {
    this.name = name;
    this.authorId = authorId;
  }
}
