package com.nus.ijuice.service;

import com.nus.ijuice.dto.PasswordDto;
import com.nus.ijuice.dto.PasswordResponseDto;
import com.nus.ijuice.dto.UserDto;
import com.nus.ijuice.dto.VerifyUserDto;

import java.text.ParseException;

public interface UserService {
    boolean existsByEmail(String emailid);
    UserDto save(UserDto userDto) throws ParseException;
    PasswordResponseDto login(VerifyUserDto dto) throws Exception;
    String forgotPassword(String email);
    String changePassword(PasswordDto passwordDto);
}
