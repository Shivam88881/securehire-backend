package com.curtin.securehire.service;

import com.curtin.securehire.constant.LocationType;
import com.curtin.securehire.entity.Location;


import java.util.List;

public interface LocationService {
    // Basic CRUD operations
    Location findById(Integer locationId);
    Location findByName(String name);
    List<Location> findAll();
    Location save(Location location);
    Location update(Integer locationId, Location location);
    void delete(Integer locationId);

    // Additional operations
    List<Location> findByType(LocationType type);
    List<Location> findByParentId(Integer parentId);
    List<Location> findByNameContaining(String name);
    boolean existsByName(String name);
}
