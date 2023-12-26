package com.gramos22.bookingmaster.controllers;

import com.gramos22.bookingmaster.dtos.UserRequest;
import com.gramos22.bookingmaster.dtos.UserResponse;
import com.gramos22.bookingmaster.entities.User;
import com.gramos22.bookingmaster.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("/users")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping // create
    public void addUser(@RequestBody UserRequest userRequest) {userService.addUser(userRequest);}

    @GetMapping("/{id}") // read
    public UserResponse getUserById(int id) {return userService.getUserById(id);}

    @DeleteMapping("/{id}")
    public UserResponse deleteUserById(int id) {return userService.deleteUserById(id);}

}
