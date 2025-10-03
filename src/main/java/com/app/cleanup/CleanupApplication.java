package com.app.cleanup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class CleanupApplication extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(CleanupApplication.class, args);
  }
}
