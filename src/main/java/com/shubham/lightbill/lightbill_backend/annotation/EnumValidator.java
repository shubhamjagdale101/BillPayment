package com.shubham.lightbill.lightbill_backend.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {
    private ValidEnum validEnum;
    @Override
    public void initialize(ValidEnum validEnum) {
        this.validEnum = validEnum;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if(value == null || value.isEmpty()) return false;

        Class<? extends Enum<?>> enm = validEnum.enumClass();
        return Arrays.stream(enm.getEnumConstants())
                .map(Enum::name)
                .anyMatch(name -> name.equals(value));

    }
}
