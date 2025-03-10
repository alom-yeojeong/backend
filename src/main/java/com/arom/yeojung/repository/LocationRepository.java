package com.arom.yeojung.repository;

import com.arom.yeojung.object.Location;
import com.arom.yeojung.object.LocationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByLocationType(LocationType locationType);
}
