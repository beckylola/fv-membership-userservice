package com.nus.ijuice.controller;

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

import javax.validation.Valid;
import java.text.ParseException;

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
}
