package com.haidev.identityservice.controller;

import com.haidev.identityservice.dto.request.UserCreationRequest;
import com.haidev.identityservice.entity.User;
import com.haidev.identityservice.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    UserService userService;

    @PostMapping
    public User createUser(@RequestBody UserCreationRequest request) {
        return userService.createUser(request);
    }

}
