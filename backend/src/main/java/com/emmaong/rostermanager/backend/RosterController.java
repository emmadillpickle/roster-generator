package com.emmaong.rostermanager.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RosterController {

    @GetMapping("/healthcheck")
    public String hello() {
        return "healthy :)";
    }
}