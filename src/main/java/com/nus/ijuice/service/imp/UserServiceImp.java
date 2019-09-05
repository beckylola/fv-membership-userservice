package com.nus.ijuice.service.imp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nus.ijuice.Validator.PasswordValidator;
import com.nus.ijuice.dto.PasswordDto;
import com.nus.ijuice.dto.PasswordResponseDto;
import com.nus.ijuice.dto.UserDto;
import com.nus.ijuice.dto.VerifyUserDto;
import com.nus.ijuice.model.EmailConfiguration;
import com.nus.ijuice.model.SystemConfig;
import com.nus.ijuice.model.Template;
import com.nus.ijuice.repository.SystemConfigRepository;
import com.nus.ijuice.repository.UserRepository;
import com.nus.ijuice.service.UserService;
import com.nus.ijuice.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.nus.ijuice.util.EmailAppSettingConstant;
import com.nus.ijuice.util.PasswordChangeConstants;


import java.util.Date;
import java.util.Random;

@Service
public class UserServiceImp implements UserService {

    final Logger logger = LoggerFactory.getLogger(UserServiceImp.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SystemConfigRepository systemConfigRepository;

    @Autowired
    EmailUtil emailUtil;
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private PasswordValidator passwordValidator;

    Template templateOptObj;
    Template templateObj;
    EmailConfiguration emailConfig;

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

    @Override
    public PasswordResponseDto login(VerifyUserDto dto) throws Exception {
        PasswordResponseDto responseDto=new PasswordResponseDto();
        User exist=this.userRepository.findUserByUsernameOrEmail(dto.getUsername(),dto.getUsername());

        if(exist==null){
            responseDto.setStatus("0");
            responseDto.setMessage("User name or email not found");
        }
        else {
            boolean isPasswordMatches = this.passwordEncoder.matches(
                    dto.getPassword(), exist.getPassword());
            logger.info("user key in password::::" + dto.getPassword());
            if (isPasswordMatches) {
                responseDto.setStatus("1");
                responseDto.setMessage("Valid password, Login success");
            } else {
                responseDto.setStatus("0");
                responseDto.setMessage("Password not match");

            }
        }

        return responseDto;
    }

    @Override
    public String forgotPassword(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            String generatedPassword = generateRandomPassword();
            user.setPassword(new BCryptPasswordEncoder()
                    .encode(generatedPassword));
            Date date = new Date();
            user.setLastPasswordChangedOn(date);
            user = userRepository.save(user);
            templateOptObj = emailUtil.loadTemplate(2);
            if (templateOptObj!=null) {
                templateObj = templateOptObj;
                String emailContent = emailUtil.replaceEmailContentPlaceholder(
                        user, templateObj.getEmailContent(), generatedPassword);
                emailConfig = emailUtil.findByKey(emailUtil.getEmailKeyList());
                emailUtil.sendSimpleMessage(user.getEmail(),
                        templateObj.getSubject(), emailContent, emailConfig);
                userRepository.updateResetPassword(1, user.getEmail());
                return EmailAppSettingConstant.PASSWORD_SENT;
            }
        }
        return EmailAppSettingConstant.INVALID_EMAILID;
    }

    @Override
    public String changePassword(PasswordDto passwordDto) {
        // boolean isPasswordChanged = false;
        String response = "";
        String passwordPattern = "";
        // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //String email = auth.getName();
        String email="xujiao7078@gmail.com";
        User user = userRepository.findByEmail(email);
        boolean isPasswordMatches = this.passwordEncoder.matches(
                passwordDto.getCurrentPassword(), user.getPassword());
        logger.info("New password::::" + passwordDto.getNewPassword());
        SystemConfig appSetting = systemConfigRepository.findSystemConfigByConfigname(PasswordChangeConstants.PASSWORD_PATTERN);
        if( appSetting.getConfigname().equals(PasswordChangeConstants.PASSWORD_PATTERN))
        {
            passwordPattern = appSetting.getConfigsetting();
        }
        if (passwordValidator.validate(passwordDto.getNewPassword(),passwordPattern)) {
            if (isPasswordMatches) {
                user.setPassword(new BCryptPasswordEncoder().encode(passwordDto
                        .getNewPassword()));
                Date date = new Date();
                user.setLastPasswordChangedOn(date);
                userRepository.save(user);
                response = PasswordChangeConstants.PASSWORD_CHANGE_SUCCESS;
                logger.info("User password changed succesfully");
                userRepository.updateResetPassword(0, email);
            } else {
                response = PasswordChangeConstants.PASSWORD_CHANGE_FAILURE;
                logger.info("Current password not matched");
            }
        } else {
            response = PasswordChangeConstants.PASSWORD_POLICY_CORRUPT;
        }
        return response;
    }

    private String generateRandomPassword() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit
                    + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedPassword = buffer.toString();
        return generatedPassword;
    }
}
