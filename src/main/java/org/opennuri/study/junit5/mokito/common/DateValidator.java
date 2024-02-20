package org.opennuri.study.junit5.mokito.common;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.opennuri.study.junit5.mokito.common.annotation.DateFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
public class DateValidator implements ConstraintValidator<DateFormat, String> {

    private String pattern;

    @Override
    public void initialize(DateFormat constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            //noinspection ResultOfMethodCallIgnored
            LocalDate.from(LocalDate.parse(value, DateTimeFormatter.ofPattern(this.pattern)));
        } catch (Exception e) {
            log.error("DateValidator : {}", e.getMessage());
            return false;
        }
        return true;
    }
}
