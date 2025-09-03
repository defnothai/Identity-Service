package com.haidev.identityservice.service;

import com.haidev.identityservice.dto.request.PermissionCreateRequest;
import com.haidev.identityservice.dto.response.PermissionResponse;
import com.haidev.identityservice.mapper.PermissionMapper;
import com.haidev.identityservice.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PermissionService {

    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionCreateRequest request) {
        return permissionMapper.toPermissionResponse(permissionRepository.save(permissionMapper.toPermission(request)));
    }

    public List<PermissionResponse> getAll() {
        return permissionRepository.findAll().stream().map(permissionMapper::toPermissionResponse).toList();
    }

    public PermissionResponse getById(String id) {
        return permissionMapper.toPermissionResponse(permissionRepository.findById(id).orElseThrow());
    }


}
