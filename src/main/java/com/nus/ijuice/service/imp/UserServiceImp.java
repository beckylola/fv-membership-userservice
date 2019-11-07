package com.nus.ijuice.service.imp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nus.ijuice.Validator.PasswordValidator;
import com.nus.ijuice.dto.*;
import com.nus.ijuice.model.EmailConfiguration;
import com.nus.ijuice.model.SystemConfig;
import com.nus.ijuice.model.Template;
import com.nus.ijuice.repository.SystemConfigRepository;
import com.nus.ijuice.repository.UserRepository;
import com.nus.ijuice.service.UserService;
import com.nus.ijuice.model.User;
import org.jasypt.util.text.BasicTextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.nus.ijuice.util.EmailAppSettingConstant;
import com.nus.ijuice.util.PasswordChangeConstants;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Calendar;
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

//    @Autowired
//    private BCryptPasswordEncoder passwordEncoder;

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
        UserDto responseDto = new UserDto();

        User user = mapper.convertValue(userDto, User.class);
        String passwordPattern = "";

        User exist = this.userRepository.findUserByEmail(user.getEmail());
        if (exist != null) {

          responseDto.setStatus("0");
          responseDto.setMessage("Sorry, your email already been registered.");


        } else {
        	 SystemConfig appSetting = systemConfigRepository.findSystemConfigByConfigname(PasswordChangeConstants.PASSWORD_PATTERN);
        
        	 if (appSetting != null) {
                 if (appSetting.getConfigname().equals(PasswordChangeConstants.PASSWORD_PATTERN)) {
                     passwordPattern = appSetting.getConfigsetting();
                 }
             }
             if (passwordValidator.validate(userDto.getPassword(), passwordPattern)) {
            	 try {
                     byte[] salt = getSalt();
                     user.setSalt(salt);
                     user.setPassword(getHashPassword(user.getPassword(),salt));
                 } catch (NoSuchAlgorithmException e) {
                     e.printStackTrace();
                 }

                 Date date = new Date();
                 user.setLastPasswordChangedOn(date);
                 user.setCreatedOn(date);
                 if (user.getAccount_source() == null)
                     user.setAccount_source("FvMembership");
                 user = userRepository.save(user);
                 user.setPassword(userDto.getPassword());
                 responseDto=mapper.convertValue(user, UserDto.class);
                 responseDto.setStatus("1");
                 responseDto.setMessage("User register success."); 
             } 
             else {
            	 responseDto.setStatus("0");
                 responseDto.setMessage(PasswordChangeConstants.PASSWORD_POLICY_CORRUPT); 
             }

        }


        return responseDto;

    }

    @Override
    public BaseResponseDto login(VerifyUserDto dto) throws Exception {
        BaseResponseDto responseDto = new BaseResponseDto();
        User exist = this.userRepository.findUserByUsernameOrEmail(dto.getUsername(), dto.getUsername());

        if (exist == null) {
            responseDto.setStatus("0");
            responseDto.setMessage("User name or email not found");
        } else {
            boolean isPasswordMatches=false;
            if(getHashPassword(dto.getPassword(),exist.getSalt()).equals(exist.getPassword())){
                isPasswordMatches=true;
            }
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
        User user = userRepository.findUserByEmail(email);
        if (user != null) {
            String generatedPassword = generateRandomPassword();
            try {
                byte[] salt = getSalt();
                user.setPassword(getHashPassword(user.getPassword(),salt));
                user.setSalt(salt);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            Date date = new Date();
            user.setLastPasswordChangedOn(date);
            user = userRepository.save(user);
            templateOptObj = emailUtil.loadTemplate(2);
            if (templateOptObj != null) {
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
        String response = "";
        String passwordPattern = "";
        User user = userRepository.findUserByEmail(passwordDto.getEmail());

        if (user != null) {
            if(!passwordDto.getToken().equalsIgnoreCase(user.getToken())) {
                response="Token not match.";
            }
            else {
                boolean isPasswordMatches = false;
                if (getHashPassword(passwordDto.getCurrentPassword(), user.getSalt()).equals(user.getPassword())) {
                    isPasswordMatches = true;
                }
                logger.info("New password::::" + passwordDto.getNewPassword());
                SystemConfig appSetting = systemConfigRepository.findSystemConfigByConfigname(PasswordChangeConstants.PASSWORD_PATTERN);
                if (appSetting != null) {
                    if (appSetting.getConfigname().equals(PasswordChangeConstants.PASSWORD_PATTERN)) {
                        passwordPattern = appSetting.getConfigsetting();
                    }
                }
                if (passwordValidator.validate(passwordDto.getNewPassword(), passwordPattern)) {
                    if (isPasswordMatches) {
                        try {
                            byte[] salt = getSalt();
                            user.setPassword(getHashPassword(passwordDto.getNewPassword(), salt));
                            user.setSalt(salt);
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }

                        Date date = new Date();
                        user.setLastPasswordChangedOn(date);
                        user.setResetPassword(false);
                        userRepository.save(user);
                        response = PasswordChangeConstants.PASSWORD_CHANGE_SUCCESS;
                        logger.info("User password changed succesfully");
                        // userRepository.updateResetPassword(0, passwordDto.getEmail());

                    } else {
                        response = PasswordChangeConstants.PASSWORD_CHANGE_FAILURE;
                        logger.info("Current password not matched");
                    }
                } else {
                    response = PasswordChangeConstants.PASSWORD_POLICY_CORRUPT;
                }
            }
        } else {
            response = "Email not found";
        }
        return response;
    }

    @Override
    public OTPDto SendOTPToUserEmail(EmailDto dto) {
        OTPDto otpDto = new OTPDto();
        User user = userRepository.findUserByEmail(dto.getEmail());

        if (user != null) {
            String otp = this.getRandomNumberString();

            templateOptObj = emailUtil.loadTemplate(2);
            if (templateOptObj != null) {
                templateObj = templateOptObj;
                String emailContent = emailUtil.replaceEmailContentPlaceholder(
                        user, templateObj.getEmailContent(), otp);
                emailConfig = emailUtil.findByKey(emailUtil.getEmailKeyList());
                emailConfig=this.getDecryptEmailConfig(emailConfig);
                emailUtil.sendSimpleMessage(user.getEmail(),
                        templateObj.getSubject(), emailContent, emailConfig);
                user.setToken(otp);
                this.userRepository.save(user);

                otpDto.setEmail(dto.getEmail());
                otpDto.setOtp(otp);
                otpDto.setExpAt(getAfterAdding(2));
                otpDto.setStatus(1);
                otpDto.setMessage("Otp send successfully");
            }
            else{
                otpDto.setStatus(0);
                otpDto.setMessage("Template not found");
            }


        } else {
            otpDto.setStatus(0);
            otpDto.setMessage("Email not found");
        }
        return otpDto;
    }

    private EmailConfiguration getDecryptEmailConfig(EmailConfiguration dto){
        EmailConfiguration emailConfiguration=dto;
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPasswordCharArray("19poiqweXJ".toCharArray());
        String myEncryptedText=emailConfiguration.getUserName();
        String plainText = textEncryptor.decrypt(myEncryptedText);
        emailConfiguration.setUserName(plainText);
        String myEncryptedText2=emailConfiguration.getPassword();
        String plainText2 = textEncryptor.decrypt(myEncryptedText2);
        emailConfiguration.setPassword(plainText2);
        return emailConfiguration;
    }
    private String getHashPassword(String passwordToHash,byte[] salt){
        String  securePassword="";

        securePassword = get_SHA_512_SecurePassword(passwordToHash, salt);

        return securePassword;

    }
    private static byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    private static String get_SHA_512_SecurePassword(String passwordToHash, byte[] salt)
    {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
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


    public static String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }

    public Date getAfterAdding(int times) {
        final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs

        Calendar date = Calendar.getInstance();
        long t = date.getTimeInMillis();
        Date afterAddingTwoMins = new Date(t + (times * ONE_MINUTE_IN_MILLIS));
        return afterAddingTwoMins;
    }

}
