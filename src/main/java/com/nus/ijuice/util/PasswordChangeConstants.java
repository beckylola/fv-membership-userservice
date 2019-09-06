package com.nus.ijuice.util;

public interface PasswordChangeConstants {
    public static final String PASSWORD_CHANGE_SUCCESS = "Password updated successfully";
    public static final String PASSWORD_CHANGE_FAILURE = "Entered invalid current password";
    public static final String PASSWORD_POLICY_CORRUPT = "Your password must be have at least:"
            + " 8 characters long, "
            + " 1 uppercase & 1 lowercase character, "
            + " 1 number, "
            + " 1 special charecter[ @ # $ % ! . ]";
    public static final String PASSWORD_POLICY = "passwordpolicy";
    public static final String PASSWORD_PATTERN = "passwordPattern";
}