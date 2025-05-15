package com.main_project.user_service.controller;

import com.main_project.user_service.entity.User;
import com.main_project.user_service.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @GetMapping("/abc")
    String test() {
        return "abc";
    }

    @PostMapping("/create")
    ResponseEntity<User> create(@RequestBody User user) {
        return ResponseEntity.ok(userService.create(user));
    }

    // For auth service when login user
    @PostMapping("/valid")
    ResponseEntity<User> valid(@RequestBody User user) {
        return ResponseEntity.ok(userService.valid(user));
    }

    // For auth service get user info and add to claimset int jwt token
    @GetMapping("/{username}")
    ResponseEntity<User> getByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getByUsername(username));
    }
}
