package com.deep.electronic.store.validate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ImageNameValidator implements ConstraintValidator<ImageNameValid, String> {
	
	Logger logger =LoggerFactory.getLogger(ImageNameValidator.class);

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		logger.info("Image Not validated :{}", value);
		if (value.isBlank()) {
			return false;
		} else {
			return true;
		}

	}

}
