package com.main_project.movie_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "user-service", url = "http://localhost:8090")
public interface UserServiceClient {

    @GetMapping("/users/test")
    String test();
}
