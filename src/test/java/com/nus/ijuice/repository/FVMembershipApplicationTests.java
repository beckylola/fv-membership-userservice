package com.nus.ijuice.repository;

import com.nus.ijuice.dto.UserDto;
import org.junit.Before;
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
public class FVMembershipApplicationTests extends AbstractApplicationTest{

	private static Logger logger = LoggerFactory.getLogger(FVMembershipApplicationTests.class);

	private static String USER_REGISTER_URI = "/fv/v1.0/register";

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
	public void registerPositiveTest() throws Exception {

		//this.prepareData();
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
	/**
	 * This is used create Mock Request user Object
	 *
	 * @return
	 */
	private UserDto getMockRequestUser() {
		UserDto dto = new UserDto();
		dto.setUsername("xj");
		dto.setEmail("xj@gmail.com");
		dto.setPassword("1234567");

		return dto;
	}


}
