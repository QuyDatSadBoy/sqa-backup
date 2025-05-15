package com.main_project.api_gateway.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GatewayController {

    @GetMapping("/test")
    String test() {
        return "test";
    }

    @RequestMapping("/**")
    public ResponseEntity<String> fallback() {
        return ResponseEntity
                .status(404)
                .body("Path not found in API Gateway routes");
    }
}
