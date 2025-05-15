package com.main_project.payment_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth-service", url = "http://localhost:8099")
public interface AuthServiceClient {

    @PostMapping("/auth/validate")
    ResponseEntity<Boolean> validate(@RequestBody String token);
}
