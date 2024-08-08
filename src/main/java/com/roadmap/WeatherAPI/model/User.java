package com.roadmap.WeatherAPI.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 28072024L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "login", unique = true)
    @NotEmpty(message = "Введите логин")
    private String login;
    @Column(name = "password")
    @NotEmpty(message = "Введите пароль")
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Location> locations;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

}
