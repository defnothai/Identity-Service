package com.haidev.identityservice.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
@AllArgsConstructor
public enum ErrorCode {

    USER_EXISTED(1001, "User already exists"),
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized exception"),
    USERNAME_INVALID(1002, "Username must be at least 3 characters long"),
    PASSWORD_INVALID(1003, "Password must be at least 8 characters long"),
    FIRSTNAME_INVALID(1004, "First name must be at least 3 characters long"),
    LASTNAME_INVALID(1005, "Last name must be at least 3 characters long"),
    DOB_INVALID(1006, "Date of birth must be in the past"),
    DOB_NOT_NULL(1007, "Date of birth is required"),
    USER_NOT_EXISTED(1009, "User not existed");

    int code;
    String message;


}
