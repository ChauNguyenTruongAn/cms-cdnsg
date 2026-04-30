package com.github.chaunguyentruongan.warehouse_cdnsg.modules.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.chaunguyentruongan.warehouse_cdnsg.modules.user.dto.UserCreateDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
// @RequestMapping("/api/v1/users")
@RequestMapping("/auth/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserCreateDTO request) {
        return ResponseEntity.ok(userService.createUser(request));
    }
}
