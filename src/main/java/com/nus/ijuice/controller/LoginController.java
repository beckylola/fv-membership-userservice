package com.nus.ijuice.controller;

import com.nus.ijuice.dto.*;
import com.nus.ijuice.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.nus.ijuice.util.EmailAppSettingConstant;
import com.nus.ijuice.util.PasswordChangeConstants;

import javax.validation.Valid;
import java.text.ParseException;


@CrossOrigin
@Api(value="IJOOZE - User Management Service API ", description="User controllers")
@RestController
@RequestMapping(path = "/fv/v1.0")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @ApiOperation(value = "User Register", response = UserDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully create user"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
    @PostMapping(path = "/register", produces = "application/json")
    public ResponseEntity<UserDto> createUser(
            @Valid @RequestBody UserDto userDto) throws ParseException {
        try {
            UserDto userODto = userService.save(userDto);
            logger.info("'" + userDto.getUsername() + "' user created successfully");
            return new ResponseEntity<UserDto>(userODto, HttpStatus.OK);
        } catch (Exception e) {
            throw e;
        }
    }


    @ApiOperation(value = "User Login", response = BaseResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully verify user"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
    @PostMapping(path = "/login", produces = "application/json")
    public ResponseEntity<BaseResponseDto> verifyUser(
            @Valid @RequestBody VerifyUserDto dto) throws Exception {
        try {
            BaseResponseDto userdto = userService.login(dto);
           // logger.info(" user login successfully");
            return new ResponseEntity<BaseResponseDto>(userdto, HttpStatus.OK);
        } catch (Exception e) {
            throw e;
        }
    }

    @PostMapping(path = "/forgotpassword", consumes = "application/json")
    public ResponseEntity<BaseResponseDto> forgotPassword(@Valid @RequestBody EmailDto emailDto) {

        String response = userService.forgotPassword(emailDto.getEmail());
        BaseResponseDto responseDto = new BaseResponseDto();
        if (response.equals(EmailAppSettingConstant.PASSWORD_SENT)) {
            responseDto.setStatus("1");
            responseDto.setMessage(EmailAppSettingConstant.PASSWORD_SENT);
            logger.info("Password sent successfully to '" + emailDto.getEmail() + "'");

        } else {
            responseDto.setStatus("0");
            responseDto.setMessage(EmailAppSettingConstant.INVALID_EMAILID);
            logger.info("Invalid email '" + emailDto.getEmail() + "'");
        }
        return new ResponseEntity<BaseResponseDto>(responseDto, HttpStatus.OK);
    }


    @ApiOperation(value = "Reset password", response = BaseResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully reset password"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
    @PostMapping(path = "/changepassword", consumes = "application/json")
    // @PreAuthorize("hasPermission('UserController', 'CREATE')")
    public ResponseEntity<Object> changePassword(@Valid @RequestBody PasswordDto passwordDto) {
        String response = userService.changePassword(passwordDto);
        BaseResponseDto responseDto = new BaseResponseDto();

        if (response.equals(PasswordChangeConstants.PASSWORD_CHANGE_SUCCESS)) {
            responseDto.setStatus("1");
            responseDto.setMessage(PasswordChangeConstants.PASSWORD_CHANGE_SUCCESS);
            logger.info("Password Changed successfully");

        } else if (response.equals(PasswordChangeConstants.PASSWORD_CHANGE_FAILURE)) {
            responseDto.setStatus("0");
            responseDto.setMessage(PasswordChangeConstants.PASSWORD_CHANGE_FAILURE);

        } else {
            responseDto.setStatus("0");
            responseDto.setMessage(PasswordChangeConstants.PASSWORD_POLICY_CORRUPT);
            logger.info("Wrong password");
        }

        return new ResponseEntity<Object>(responseDto, HttpStatus.OK);
    }



    @ApiOperation(value = "Send otp to user's email as MFA code", response = OTPDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully send otp"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
    @PostMapping(path = "/sendOtp", consumes = "application/json")
    public ResponseEntity<OTPDto> sendOtp(@Valid @RequestBody EmailDto emailDto) {

        OTPDto response = userService.SendOTPToUserEmail(emailDto);

        return new ResponseEntity<OTPDto>(response, HttpStatus.OK);
    }

}
