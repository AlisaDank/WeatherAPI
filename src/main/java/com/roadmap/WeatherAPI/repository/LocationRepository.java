package com.roadmap.WeatherAPI.repository;

import com.roadmap.WeatherAPI.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {
    Optional<Location> findByName(String name);
    Optional<Location> findByNameAndLatAndLon(String name, Double lat, Double lon);
}
