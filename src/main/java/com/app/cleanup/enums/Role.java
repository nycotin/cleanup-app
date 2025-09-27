package com.app.cleanup.enums;

public enum Role {
  ADMIN("Administrator"),
  USER("Regular User"),
  MANAGER("Manager");

  private final String displayName;

  Role(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}
