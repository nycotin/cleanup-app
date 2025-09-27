package com.app.cleanup.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
@Setter
@Getter
@Table(name = "tasks")
public class Task {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @Column(name = "author_id")
  private Long author_id;

  @Column(name = "assignee_id")
  private Long assignee_id;

  public Task() {}

  public Task(String name, Long author_id) {
    this.name = name;
    this.author_id = author_id;
  }

  public void setAuthorId(Long authorId) {
    this.author_id = authorId;
  }

  public void setassigneeId(Long assigneeId) {
    this.assignee_id = assigneeId;
  }
}
