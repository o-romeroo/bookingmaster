package com.gramos22.bookingmaster.services;

import com.gramos22.bookingmaster.dtos.UserRequest;
import com.gramos22.bookingmaster.dtos.UserResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    void addUser(UserRequest userRequest);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(int id);

    UserResponse deleteUserById(int id);
}
