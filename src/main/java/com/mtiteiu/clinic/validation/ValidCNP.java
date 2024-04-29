package com.mtiteiu.clinic.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CNPValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCNP {
    String message() default "Invalid CNP format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
