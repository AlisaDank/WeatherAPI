package com.roadmap.WeatherAPI.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

@Entity
@Table(name = "locations",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"latitude", "longitude", "user_id"},
                name = "unique_location")})
public class Location implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    @NotEmpty
    private String name;
    @Column(name = "latitude")
    @NotNull
    private double lat;
    @Column(name = "longitude")
    @NotNull
    private double lon;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    @NotNull
    private User user;

    public Location() {
    }

    public Location(String name, double lat, double lon) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double longitude) {
        this.lon = longitude;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
