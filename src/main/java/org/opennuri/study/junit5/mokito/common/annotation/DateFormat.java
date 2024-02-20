package org.opennuri.study.junit5.mokito.common.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.opennuri.study.junit5.mokito.common.DateValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateFormat {

    String message() default "6자리의 yyyyMMdd 형식이어야 합니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload> [] payload() default {};
    String pattern() default "yyyyMMdd";
}
