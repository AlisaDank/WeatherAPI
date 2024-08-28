package com.roadmap.WeatherAPI.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class UserDTO {
    @NotEmpty(message = "Это поле не может быть пустым")
    @Size(min = 3, message = "Логин должен быть не короче 3х символов")
    private String login;
    @NotEmpty(message = "Это поле не может быть пустым")
    @Size(min = 3, message = "Придумайте пароль не короче 3х символов")
    private String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
