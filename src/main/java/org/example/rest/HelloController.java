package org.example.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.Map;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public Map<String, String> greeting() {
        return Map.of("message", "Hello World!");
    }
}
