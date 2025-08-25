package com.haidev.identityservice.dto.request;

import com.haidev.identityservice.entity.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class UserCreationRequest {

    String username;
    String password;
    String firstName;
    String lastName;
    LocalDate dob;

}
