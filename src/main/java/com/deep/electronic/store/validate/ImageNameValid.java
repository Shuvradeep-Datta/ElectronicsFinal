package com.deep.electronic.store.validate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(value=RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy=ImageNameValidator.class)
public @interface ImageNameValid {
	
	//for message purpose only
	String message() default "Image Name Not Valid";


	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

}
