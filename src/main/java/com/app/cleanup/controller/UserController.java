package com.app.cleanup.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {
    @RequestMapping("/api/auth")
    public String authenticateUser() {
        return "edrcftvbzuin";
    }
}


// Create user in DB
// Authenticate user + return PAI token