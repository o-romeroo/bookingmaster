package com.gramos22.bookingmaster.services;

import com.gramos22.bookingmaster.dtos.UserRequest;
import com.gramos22.bookingmaster.dtos.UserResponse;
import com.gramos22.bookingmaster.entities.User;

import java.util.List;
import com.gramos22.bookingmaster.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void addUser(UserRequest userRequest) {
        User user = new User();
        user.setFirst_name(userRequest.getFirst_name());
        user.setLast_name(userRequest.getLast_name());
        user.setEmail(userRequest.getEmail());
        user.setDocument(userRequest.getDocument());
        userRepository.save(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> {
                    UserResponse response = new UserResponse();
                    response.setId(user.getId());
                    response.setFirst_name(user.getFirst_name());
                    response.setLast_name(user.getLast_name());
                    response.setEmail(user.getEmail());
                    response.setDocument(user.getDocument());
                    return response;
                })
                .toList();
    }

    @Override
    public UserResponse getUserById(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFirst_name(user.getFirst_name());
        response.setLast_name(user.getLast_name());
        response.setEmail(user.getEmail());
        response.setDocument(user.getDocument());
        return response;
    }

    @Override
    public UserResponse deleteUserById(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFirst_name(user.getFirst_name());
        response.setLast_name(user.getLast_name());
        response.setEmail(user.getEmail());
        response.setDocument(user.getDocument());
        return response;
    }
}
