package com.roadmap.WeatherAPI.util;

import com.roadmap.WeatherAPI.model.User;
import com.roadmap.WeatherAPI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidation implements Validator {
    private final UserService userService;

    @Autowired
    public UserValidation(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        if (userService.findByLogin(user.getLogin()).isPresent()) {
            errors.rejectValue("login", "", "Этот логин уже используется");
        }
    }
}
