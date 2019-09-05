package com.nus.ijuice.controller;

import com.nus.ijuice.dto.EmailDto;
import com.nus.ijuice.dto.PasswordDto;
import com.nus.ijuice.dto.PasswordResponseDto;
import com.nus.ijuice.dto.UserDto;
import com.nus.ijuice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.EmailAppSettingConstant;
import util.PasswordChangeConstants;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/fv/v1.0")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;


    @PostMapping(path = "/register", produces = "application/json")
    public ResponseEntity<UserDto> createUser(
            @Valid @RequestBody UserDto userDto) throws ParseException {
        try {
            UserDto userODto = userService.save(userDto);
            logger.info("'"+userDto.getUsername()+"' user created successfully");
            return new ResponseEntity<UserDto>(userODto, HttpStatus.OK);
        } catch (Exception e) {
            throw e;
        }
    }

    @PostMapping(path = "/forgotpassword", consumes = "application/json")
    public ResponseEntity<PasswordResponseDto> forgotPassword(@RequestBody EmailDto emailDto) {

        String response = userService.forgotPassword(emailDto.getEmail());
        PasswordResponseDto responseDto = new PasswordResponseDto();
        if (response.equals(EmailAppSettingConstant.PASSWORD_SENT)) {
            responseDto.setStatus("0");
            responseDto.setMessage(EmailAppSettingConstant.PASSWORD_SENT);
            logger.info("Password sent successfully to '"+emailDto.getEmail()+"'");

        } else {
            responseDto.setStatus("1");
            responseDto.setMessage(EmailAppSettingConstant.INVALID_EMAILID);
            logger.info("Invalid email '"+emailDto.getEmail()+"'");
        }
        return new ResponseEntity<PasswordResponseDto>(responseDto, HttpStatus.OK);
    }


    @PostMapping(path = "/changepassword", consumes = "application/json")
    // @PreAuthorize("hasPermission('UserController', 'CREATE')")
    public ResponseEntity<Object> changePassword(@RequestBody PasswordDto passwordDto) {
        String response = userService.changePassword(passwordDto);
        PasswordResponseDto responseDto = new PasswordResponseDto();

        if (response.equals(PasswordChangeConstants.PASSWORD_CHANGE_SUCCESS)) {
            responseDto.setStatus("0");
            responseDto.setMessage(PasswordChangeConstants.PASSWORD_CHANGE_SUCCESS);
            logger.info("Password Changed successfully");

        } else if (response.equals(PasswordChangeConstants.PASSWORD_CHANGE_FAILURE)) {
            responseDto.setStatus("1");
            responseDto.setMessage(PasswordChangeConstants.PASSWORD_CHANGE_FAILURE);

        } else {
            responseDto.setStatus("1");
            responseDto.setMessage(PasswordChangeConstants.PASSWORD_POLICY_CORRUPT);
            logger.info("Wrong password");
        }

        return new ResponseEntity<Object>(responseDto, HttpStatus.OK);
    }
}
