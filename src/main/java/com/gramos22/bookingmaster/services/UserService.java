package com.gramos22.bookingmaster.services;

import com.gramos22.bookingmaster.dtos.UserRequest;
import com.gramos22.bookingmaster.dtos.UserResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    void addUser(UserRequest userRequest);

    UserResponse getUserById(int id);

    UserResponse deleteUserById(int id);
}
