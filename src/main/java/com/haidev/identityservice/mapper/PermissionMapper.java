package com.haidev.identityservice.mapper;

import com.haidev.identityservice.dto.request.PermissionCreateRequest;
import com.haidev.identityservice.dto.response.PermissionResponse;
import com.haidev.identityservice.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionCreateRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
