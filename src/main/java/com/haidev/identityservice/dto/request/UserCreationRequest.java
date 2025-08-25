package com.haidev.identityservice.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class UserCreationRequest {

    @Size(min = 3, message = "Username must be at least 3 characters long")
    String username;
    @Size(min = 8, message = "Username must be at least 8 characters long")
    String password;
    @Size(min = 3, message = "First name must be at least 3 characters long")
    String firstName;
    @Size(min = 3, message = "Last name must be at least 3 characters long")
    String lastName;
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    LocalDate dob;

}
