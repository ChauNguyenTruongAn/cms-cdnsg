package com.github.chaunguyentruongan.warehouse_cdnsg.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username) throws Exception {
        Map<String, String> response = new HashMap<>();
        response.put("token", jwtService.generateToken(username));
        return ResponseEntity.ok(response);
    }
}