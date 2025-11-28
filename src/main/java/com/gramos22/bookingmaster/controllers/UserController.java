package com.gramos22.bookingmaster.controllers;

import com.gramos22.bookingmaster.dtos.UserRequest;
import com.gramos22.bookingmaster.dtos.UserResponse;
import com.gramos22.bookingmaster.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public void addUser(@RequestBody UserRequest userRequest) {
        userService.addUser(userRequest);
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public UserResponse deleteUserById(@PathVariable int id) {
        return userService.deleteUserById(id);
    }
}
