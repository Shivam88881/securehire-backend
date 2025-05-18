package com.curtin.securehire.service.db;

import com.curtin.securehire.constant.LocationType;
import com.curtin.securehire.dto.LocationDTO;
import com.curtin.securehire.entity.db.Location;


import java.util.List;

public interface LocationService {
    // Basic CRUD operations
    Location findById(Integer locationId);
    Location findByName(String name);
    List<Location> findAll();
    Location save(LocationDTO location);
    Location update(Integer locationId, Location location);
    void delete(Integer locationId);

    // Additional operations
    List<Location> findByType(LocationType type);
    List<Location> findByParentId(Integer parentId);
    List<Location> findByNameContaining(String name);
    boolean existsByName(String name);
}
