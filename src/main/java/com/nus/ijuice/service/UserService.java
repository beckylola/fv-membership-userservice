package com.nus.ijuice.service;

import com.nus.ijuice.dto.UserDto;

import java.text.ParseException;

public interface UserService {
    boolean existsByEmail(String emailid);
    UserDto save(UserDto userDto) throws ParseException;
}
