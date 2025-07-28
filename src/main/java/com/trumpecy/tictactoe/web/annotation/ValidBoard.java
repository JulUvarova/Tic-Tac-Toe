package com.trumpecy.tictactoe.web.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = BoardValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBoard {
    String message() default "Invalid board: must be a 3x3 matrix with values 0, 1, or 2";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}