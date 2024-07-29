package com.roadmap.WeatherAPI.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
@Entity
@Table(name = "users")
public class User implements Serializable {
    private static final long serialVersionUID = 28072024L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "login")
    @NotEmpty(message = "This field should not be empty")
    private String login;
    @Column(name = "password")
    private String password;

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
