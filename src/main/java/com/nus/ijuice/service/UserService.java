package com.nus.ijuice.service;

import com.nus.ijuice.dto.*;

import java.text.ParseException;

public interface UserService {

    boolean existsByEmail(String emailid);

    UserDto save(UserDto userDto) throws ParseException;

    BaseResponseDto login(VerifyUserDto dto) throws Exception;

    String forgotPassword(String email);

    String changePassword(PasswordDto passwordDto);

    OTPDto SendOTPToUserEmail(EmailDto dto);
}
