package com.github.haskiro.musicapp.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

public class ErrorUtil {
    public static String returnErrorsAsString(BindingResult bindingResult) {
        List<FieldError> errors = bindingResult.getFieldErrors();
        StringBuilder errorMessage = new StringBuilder();

        errors.forEach(fieldError -> {
            errorMessage.append(fieldError.getField())
                    .append(" - ").append(fieldError.getDefaultMessage())
                    .append(";");
        });

        return errorMessage.toString();
    }
}
