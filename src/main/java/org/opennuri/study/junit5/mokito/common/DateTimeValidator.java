package org.opennuri.study.junit5.mokito.common;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.opennuri.study.junit5.mokito.common.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class DateTimeValidator implements ConstraintValidator<DateTimeFormat, String> {

    private String pattern;

    @Override
    public void initialize(DateTimeFormat constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            //noinspection ResultOfMethodCallIgnored
            LocalDateTime.parse(value, DateTimeFormatter.ofPattern(pattern));
        } catch (Exception e) {
            log.error("DateTime Format is not valid : {}", e.getMessage());
            return false;
        }
        return true;
    }
}
