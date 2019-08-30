package com.nus.ijuice.Validator;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PasswordValidator {

    private Pattern pattern;
    private Matcher matcher;

    public boolean validate(final String password,String passwordPattern) {
        pattern = Pattern.compile(passwordPattern);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }



}