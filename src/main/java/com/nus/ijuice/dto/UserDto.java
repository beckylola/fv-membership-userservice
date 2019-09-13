package com.nus.ijuice.dto;

import com.nus.ijuice.Validator.Unique;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

import static com.nus.ijuice.util.MessageConstants.USER_EMAIL_EXIST;
import static com.nus.ijuice.util.MessageConstants.USER_EMAIL_REQUIRED;
import static com.nus.ijuice.util.MessageConstants.USER_NAME_REQUIRED;

public class UserDto {

    @NotEmpty(message = USER_NAME_REQUIRED)
    private String username;

    @NotEmpty(message = "{user.password.error.required}")
    private String password;

    @NotEmpty(message = USER_EMAIL_REQUIRED)
    private String email;

    private Date createdOn;
    private String account_source;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getAccount_source() {
        return account_source;
    }

    public void setAccount_source(String account_source) {
        this.account_source = account_source;
    }
}
