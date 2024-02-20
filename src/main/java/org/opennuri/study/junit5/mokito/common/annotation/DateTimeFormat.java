package org.opennuri.study.junit5.mokito.common.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.opennuri.study.junit5.mokito.common.DateTimeValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateTimeValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateTimeFormat {
    String message() default "일시는 yyyy-MM-dd HH:mm:ss 형식이어야 합니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload> [] payload() default {};
    String pattern() default "yyyy-MM-dd HH:mm:ss";
}
