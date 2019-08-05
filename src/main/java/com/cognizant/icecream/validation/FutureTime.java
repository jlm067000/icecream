package com.cognizant.icecream.validation;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Constraint(validatedBy = FutureTimeValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FutureTime {
    String message() default "{com.cognizant.icecream.validation.FutureTime.message}";
    Class[] groups() default {};
    Class[] payload() default {};
}
