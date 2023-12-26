package com.gramos22.bookingmaster.services;

import com.gramos22.bookingmaster.dtos.UserRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    UserRepository userRepository;

    public void addUser(UserRequest userRequest) {
        
    }
}
