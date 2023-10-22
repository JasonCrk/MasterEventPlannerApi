package com.LP2.EventScheduler.validation.isImage;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsImageValidator.class)
@Documented
public @interface IsImage {
    String message() default "The file must be a image (jpeg o png)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
