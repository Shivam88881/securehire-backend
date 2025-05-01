package com.curtin.securehire.repository;

import com.curtin.securehire.constant.LocationType;
import com.curtin.securehire.entity.Location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {
    Optional<Location> findByName(String name);
    List<Location> findByType(LocationType type);
    List<Location> findByParentId(Integer parentId);
    List<Location> findByNameContaining(String name);
    boolean existsByName(String name);
}
