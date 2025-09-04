package com.haidev.identityservice.dto.request.role;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleCreateRequest {
    String name;
    String description;
    Set<String> permissions; // tên các permission
}
