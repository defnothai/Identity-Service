package com.haidev.identityservice.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserUpdateRequest {

    String password;
    String firstName;
    String lastName;
    LocalDate dob;

}
