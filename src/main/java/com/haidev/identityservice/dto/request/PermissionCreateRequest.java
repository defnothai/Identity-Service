package com.haidev.identityservice.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionCreateRequest {

    String name;
    String description;
}
