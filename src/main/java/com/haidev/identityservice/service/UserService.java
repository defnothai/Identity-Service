package com.haidev.identityservice.service;

import com.haidev.identityservice.dto.request.UserCreationRequest;
import com.haidev.identityservice.dto.request.UserUpdateRequest;
import com.haidev.identityservice.dto.response.UserResponse;
import com.haidev.identityservice.entity.User;
import com.haidev.identityservice.exception.AppException;
import com.haidev.identityservice.exception.ErrorCode;
import com.haidev.identityservice.mapper.UserMapper;
import com.haidev.identityservice.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserService {

    UserRepository userRepository;
    UserMapper userMapper;

    public User createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        return userRepository.save(userMapper.toUser(request));
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserResponse updateUser(String id, UserUpdateRequest request) {
        User user = getUserById(id);
        userMapper.updateUser(request, user);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

}
