package com.authService.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class TestController {
    @GetMapping("/api/test")
    public String test(Principal principal) {
        if (principal != null) {
            return "認證成功！用戶名: " + principal.getName();
        }
        return "未認證";
    }
}
