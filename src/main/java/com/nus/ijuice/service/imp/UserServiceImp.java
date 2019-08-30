package com.nus.ijuice.service.imp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nus.ijuice.Validator.PasswordValidator;
import com.nus.ijuice.dto.UserDto;
import com.nus.ijuice.test.UserRepository;
import com.nus.ijuice.service.UserService;
import com.nus.ijuice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.util.Date;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private PasswordValidator passwordValidator;

    @Override
    public boolean existsByEmail(String emailid) {
        return userRepository.existsByEmail(emailid);
    }

    @Override
    public UserDto save(UserDto userDto) {
        User user = mapper.convertValue(userDto, User.class);

        User exist=this.userRepository.findUserByEmail(user.getEmail());
        if (exist!=null && StringUtils.isEmpty(user.getPassword())) {
            user.setPassword(passwordEncoder
                    .encode(user.getPassword()));

            Date date = new Date();
            user.setLastPasswordChangedOn(date);
            user.setCreatedOn(date);

        } else {

            user.setUserId(exist.getUserId());

        }
        user = userRepository.save(user);

        return mapper.convertValue(user, UserDto.class);

    }
}
