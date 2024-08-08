package com.roadmap.WeatherAPI.service;

import com.roadmap.WeatherAPI.model.User;
import com.roadmap.WeatherAPI.repository.UserRepository;
import com.roadmap.WeatherAPI.util.UserValidation;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.validation.SimpleErrors;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class UserServiceIT {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserValidation userValidation;

    @Container
    public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:alpine")
            .withInitScript("db/initTestUserDB.sql")
            .withReuse(true);

    @DynamicPropertySource
    public static void overrideProperties(DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add("spring.datasource.url", container::getJdbcUrl);
        propertyRegistry.add("spring.datasource.username", container::getUsername);
        propertyRegistry.add("spring.datasource.password", container::getPassword);
        propertyRegistry.add("spring.datasource.driver-class-name", container::getDriverClassName);
    }

    @Test
    void registerUser_addNewUserToDb() {
        User user = userRepository.save(createUser("new_login"));
        assertThat(user).isNotNull();
        Optional<User> optionalUser = userRepository.findUserByLogin("new_login");
        assertThat(optionalUser).isPresent();
    }

    @Test
    void registerUser_withExistedLogin_createErrorInValidator() {
        User user = createUser("test_login");
        userRepository.save(user);
        SimpleErrors errors = new SimpleErrors(user);
        userValidation.validate(user, errors);
        assertThat(errors.getFieldErrors()).hasSizeGreaterThan(0);
    }

    private User createUser(String login) {
        User user = new User();
        user.setLogin(login);
        user.setPassword("test_pass");
        return user;
    }
}