package com.nus.ijuice.dto;

import com.nus.ijuice.Validator.Unique;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

import static util.MessageConstants.USER_EMAIL_EXIST;
import static util.MessageConstants.USER_EMAIL_REQUIRED;
import static util.MessageConstants.USER_FIRST_NAME_REQUIRED;


@Unique.List({
            @Unique(field = "email", dependField = "userId", message = USER_EMAIL_EXIST),

    })

    public class UserDto {

        @NotEmpty(message = USER_FIRST_NAME_REQUIRED )
        private String username;

	    @NotEmpty(message = "{user.password.error.required}")
        private String password;

        @NotEmpty(message = USER_EMAIL_REQUIRED)
        private String email;

        private Date createdOn;


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

}
