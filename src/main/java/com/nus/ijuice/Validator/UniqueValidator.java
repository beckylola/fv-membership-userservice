package com.nus.ijuice.Validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.nus.ijuice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;


public class UniqueValidator implements ConstraintValidator<Unique, Object> {
	
	Logger logger = LoggerFactory.getLogger(UniqueValidator.class);

	private String field;
	private String dependField;
	
	@Autowired
	private UserService userService;

	public void initialize(Unique constraintAnnotation) {
		this.field = constraintAnnotation.field();
		this.dependField = constraintAnnotation.dependField();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		BeanWrapper beanWrapper = new BeanWrapperImpl(value);
		Object fieldValue = beanWrapper.getPropertyValue(field);
		Object dependFieldValue = beanWrapper.getPropertyValue(dependField);
		
		if(fieldValue == null || !StringUtils.isEmpty(dependFieldValue)) {
			return true;
		}
		
		if(field.equals("email")) {
			return !userService.existsByEmail(fieldValue.toString());
		}
		return true;	
		
	}

}
