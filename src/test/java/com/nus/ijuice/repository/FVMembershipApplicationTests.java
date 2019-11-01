package com.nus.ijuice.repository;

import com.nus.ijuice.dto.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.apache.commons.lang.StringUtils;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FVMembershipApplicationTests extends AbstractApplicationTest {

    private static Logger logger = LoggerFactory.getLogger(FVMembershipApplicationTests.class);

    private static String USER_REGISTER_URI = "/fv/v1.0/register";
    private static String USER_LOGIN_URI = "/fv/v1.0/login";
    private static String CHANGE_PASSWORD_URI = "/fv/v1.0/changepassword";
    private static String FORGET_PASSWORD_URI = "/fv/v1.0/forgotpassword";
    private static String SEND_OTP_URI = "/fv/v1.0/sendOtp";
    @Autowired
    private UserRepository userRepository;


    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    /**
     * This is a positive repository case for Create CIF And Account API
     *
     * @throws Exception
     */


    @Test
    @Ignore
    public void registerPositiveTest() throws Exception {
        UserDto mockRequest = this.getMockRequestUser();

        // Perform API call
        MvcResult mvcResult = this.performPostRequest(USER_REGISTER_URI, mockRequest, status().isOk());

        String content = mvcResult.getResponse().getContentAsString();
        logger.info("=====Test new user=========");
        logger.info(content);

        assertTrue("Response should not be empty", StringUtils.isNotEmpty(content));
        UserDto response = mapFromJson(content, UserDto.class);
        assertTrue("Name should not be empty", StringUtils.isNotEmpty(response.getUsername()));
        assertTrue("Password should not be empty", StringUtils.isNotEmpty(response.getPassword()));
        assertTrue("Email should not be empty", StringUtils.isNotEmpty(response.getEmail()));
    }

    private UserDto getMockRequestUser() {
        UserDto dto = new UserDto();
        dto.setUsername("april");
        dto.setEmail("al@gmail.com");
        dto.setPassword("123sts4567");

        return dto;
    }

    @Test
    public void loginPositiveTest() throws Exception {
        VerifyUserDto mockRequest = this.getMockRequestlogin();

        // Perform API call
        MvcResult mvcResult = this.performPostRequest(USER_LOGIN_URI, mockRequest, status().isOk());

        String content = mvcResult.getResponse().getContentAsString();
        logger.info("=====Test new user=========");
        logger.info(content);

        assertTrue("Response should not be empty", StringUtils.isNotEmpty(content));
        BaseResponseDto response = mapFromJson(content, BaseResponseDto.class);
        assertTrue("Status should not be empty", StringUtils.isNotEmpty(response.getStatus()));
        assertTrue("Message should not be empty", StringUtils.isNotEmpty(response.getMessage()));

    }

    private VerifyUserDto getMockRequestlogin() {
        VerifyUserDto dto = new VerifyUserDto();
        dto.setUsername("xj");
        dto.setPassword("1234567");
        return dto;
    }

    @Test
    public void ForgetPasswordPositiveTest() throws Exception {
        EmailDto mockRequest = this.getMockRequestForgetPassword();
        // Perform API call
        MvcResult mvcResult = this.performPostRequest(FORGET_PASSWORD_URI, mockRequest, status().isOk());

        String content = mvcResult.getResponse().getContentAsString();
        logger.info("===== Test forget password =========");
        logger.info(content);

        assertTrue("Response should not be empty", StringUtils.isNotEmpty(content));
        BaseResponseDto response = mapFromJson(content, BaseResponseDto.class);
        assertTrue("Message should not be empty", StringUtils.isNotEmpty(response.getMessage()));
        assertTrue("Status should not be empty", StringUtils.isNotEmpty(response.getStatus()));
    }

    private EmailDto getMockRequestForgetPassword() {
        EmailDto dto = new EmailDto();
        dto.setEmail("xujiao7078@gmail.com");
        return dto;
    }

    @Test
    public void ChangePasswordPositiveTest() throws Exception {
        PasswordDto mockRequest = this.getMockRequestChangePassword();
        // Perform API call
        MvcResult mvcResult = this.performPostRequest(CHANGE_PASSWORD_URI, mockRequest, status().isOk());

        String content = mvcResult.getResponse().getContentAsString();
        logger.info("===== Test Change password =========");
        logger.info(content);

        assertTrue("Response should not be empty", StringUtils.isNotEmpty(content));
        BaseResponseDto response = mapFromJson(content, BaseResponseDto.class);
        assertTrue("Message should not be empty", StringUtils.isNotEmpty(response.getMessage()));
        assertTrue("Status should not be empty", StringUtils.isNotEmpty(response.getStatus()));
    }

    private PasswordDto getMockRequestChangePassword() {
        PasswordDto dto = new PasswordDto();
        dto.setToken("106782");
        dto.setCurrentPassword("Password@126");
        dto.setNewPassword("Password@123");
        dto.setEmail("xujiao7078@gmail.com");
        return dto;
    }

    @Test
    public void SendOTPPositiveTest() throws Exception {
        EmailDto mockRequest = this.getMockRequestForgetPassword();
        // Perform API call
        MvcResult mvcResult = this.performPostRequest(SEND_OTP_URI, mockRequest, status().isOk());

        String content = mvcResult.getResponse().getContentAsString();
        logger.info("===== Test send otp =========");
        logger.info(content);

        assertTrue("Response should not be empty", StringUtils.isNotEmpty(content));
        OTPDto response = mapFromJson(content, OTPDto.class);
        assertTrue("Message should not be empty", StringUtils.isNotEmpty(response.getMessage()));
        assertTrue("Status should not be empty", StringUtils.isNotEmpty(String.valueOf(response.getStatus())));
    }


}
