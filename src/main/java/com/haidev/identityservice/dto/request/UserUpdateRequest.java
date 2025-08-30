package com.haidev.identityservice.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserUpdateRequest {

    @Size(min = 8, message = "PASSWORD_INVALID")
    String password;
    @Size(min = 3, message = "FIRSTNAME_INVALID")
    String firstName;
    @Size(min = 3, message = "LASTNAME_INVALID")
    String lastName;
    @NotNull(message = "DOB_NOT_NULL")
    @Past(message = "DOB_INVALID")
    LocalDate dob;

}
