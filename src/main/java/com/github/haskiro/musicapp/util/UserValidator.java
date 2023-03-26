package com.github.haskiro.musicapp.util;

import com.github.haskiro.musicapp.models.User;
import com.github.haskiro.musicapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class UserValidator implements Validator {

    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        Optional<User> existsUser = userService.findByEmail(user.getEmail());

        if (existsUser.isPresent() && user.getId() != existsUser.get().getId()  ) {
            errors.rejectValue("email", "", "User with this email already exists");
        }
    }
}
