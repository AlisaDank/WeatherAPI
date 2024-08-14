package com.roadmap.WeatherAPI.repository;

import com.roadmap.WeatherAPI.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {
    @Modifying
    @Query(value = "delete from locations where user_id = :userId and latitude = :lat and longitude = :lon",
            nativeQuery = true)
    void deleteLocationByUserIdAndLatAndLon(Integer userId, Double lat, Double lon);

    @Query(value = "select * from locations where user_id = :userId and latitude = :lat and longitude = :lon",
            nativeQuery = true)
    Optional<Location> findByUserIdAndLatAndLon(Integer userId, Double lat, Double lon);
}
