package com.haidev.identityservice.mapper;

import com.haidev.identityservice.dto.request.user.UserCreationRequest;
import com.haidev.identityservice.dto.request.user.UserUpdateRequest;
import com.haidev.identityservice.dto.response.UserResponse;
import com.haidev.identityservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserCreationRequest request);

    void updateUser(UserUpdateRequest request, @MappingTarget User user);

    // @Mapping(target = "firstName", ignore = true) // không map field lastname
    @Mapping(source = "firstName", target = "lastName")
    UserResponse toUserResponse(User user);



}
