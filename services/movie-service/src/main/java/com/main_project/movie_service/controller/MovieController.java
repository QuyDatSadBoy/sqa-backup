package com.main_project.movie_service.controller;

import com.main_project.movie_service.client.UserServiceClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MovieController {

    UserServiceClient userServiceClient;

    @GetMapping("/test")
    String test() {
        return userServiceClient.test() + " OK";

//        return "test";
    }

}
