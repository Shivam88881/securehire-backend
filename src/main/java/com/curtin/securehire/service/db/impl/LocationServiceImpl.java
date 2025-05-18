package com.curtin.securehire.service.db.impl;

import com.curtin.securehire.constant.LocationType;
import com.curtin.securehire.dto.LocationDTO;
import com.curtin.securehire.entity.db.Location;

import com.curtin.securehire.exception.BadRequestException;
import com.curtin.securehire.exception.NotFoundException;
import com.curtin.securehire.repository.db.LocationRepository;
import com.curtin.securehire.repository.es.LocationSearchRepository;
import com.curtin.securehire.service.db.LocationService;
import com.curtin.securehire.service.es.LocationSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LocationServiceImpl implements LocationService {

    private static final Logger logger = LoggerFactory.getLogger(LocationServiceImpl.class);

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationSearchService locationSearchService;

    @Autowired
    private LocationSearchRepository locationSearchRepository;

    @Override
    public Location findById(Integer locationId) {
        logger.info("Finding location by ID: {}", locationId);

        Optional<Location> locationOpt = locationRepository.findById(locationId);
        if (locationOpt.isEmpty()) {
            logger.error("Location not found with ID: {}", locationId);
            throw new NotFoundException("Location not found with ID: " + locationId);
        }

        logger.info("Found location with ID: {}", locationId);
        return locationOpt.get();
    }

    @Override
    public Location findByName(String name) {
        logger.info("Finding location by name: {}", name);

        Optional<Location> locationOpt = locationRepository.findByName(name);
        if (locationOpt.isEmpty()) {
            logger.error("Location not found with name: {}", name);
            throw new NotFoundException("Location not found with name: " + name);
        }

        logger.info("Found location with name: {}", name);
        return locationOpt.get();
    }

    @Override
    public List<Location> findAll() {
        logger.info("Fetching all locations");

        try {
            List<Location> locations = locationRepository.findAll();
            logger.info("Fetched {} locations", locations.size());
            return locations;
        } catch (Exception e) {
            logger.error("Error fetching all locations: {}", e.getMessage(), e);
            throw new BadRequestException("Failed to fetch locations: " + e.getMessage());
        }
    }

    @Override
    public Location save(LocationDTO location) {
        logger.info("Creating new location: {}", location.getName());
        System.out.println("creating location = .................................."+location.getName());
        if (locationRepository.existsByName(location.getName())) {
            logger.error("Location already exists with name: {}", location.getName());
            throw new BadRequestException("Location already exists with name: " + location.getName());
        }

        // Validate parent location if specified
        if (location.getParent() != null) {
            if (!locationRepository.existsById(location.getParent().getId())) {
                logger.error("Parent location not found with ID: {}", location.getParent());
                throw new NotFoundException("Parent location not found with ID: " + location.getParent());
            }
        }

        Location newLocation = new Location();
        newLocation.setName(location.getName());
        newLocation.setType(location.getType());
        Location parentLocation = new Location();
        Integer parentId = location.getParent() != null ? location.getParent().getId() : null;
        parentLocation.setId(parentId);
        newLocation.setParent(parentLocation);
        if(parentId==null){
            newLocation.setParent(null);
        }
        List<Location> childLocations = new ArrayList<>();
        if (location.getChildren() != null) {
            for (LocationDTO.ChildLocationDTO childLocation : location.getChildren()) {
                Location childLocationEntity = new Location();
                childLocationEntity.setId(childLocation.getId());
                if(childLocation.getId()==null){
                    childLocations.add(childLocationEntity);
                }

            }
        }
        newLocation.setChildren(childLocations);


        try {
            Location savedLocation = locationRepository.save(newLocation);
            logger.info("Location created successfully with ID: {}", savedLocation.getId());
            return savedLocation;
        } catch (Exception e) {
            logger.error("Error creating location: {}", e.getMessage(), e);
            throw new BadRequestException("Failed to create location: " + e.getMessage());
        }
    }

    @Override
    public Location update(Integer locationId, Location location) {
        logger.info("Updating location with ID: {}", locationId);

        if (!locationRepository.existsById(locationId)) {
            logger.error("Cannot update location: Location not found with ID: {}", locationId);
            throw new NotFoundException("Location not found with ID: " + locationId);
        }

        // Check if new name conflicts with existing location (except itself)
        Optional<Location> existingLocationWithSameName = locationRepository.findByName(location.getName());
        if (existingLocationWithSameName.isPresent() && !existingLocationWithSameName.get().getId().equals(locationId)) {
            logger.error("Cannot update location: Another location already exists with name: {}", location.getName());
            throw new BadRequestException("Another location already exists with name: " + location.getName());
        }

        // Validate parent location if specified
        if (location.getParent() != null ) {
            if (!locationRepository.existsById(location.getParent().getId())) {
                logger.error("Parent location not found with ID to update the location: {}", location.getParent());
                throw new NotFoundException("Parent location not found with ID: " + location.getParent());
            }

            // Prevent circular reference
            if (location.getParent().equals(locationId)) {
                logger.error("Cannot set location as its own parent");
                throw new BadRequestException("Cannot set location as its own parent");
            }
        }

        try {
            location.setId(locationId);
            Location updatedLocation = locationRepository.save(location);
            logger.info("Location updated successfully with ID: {}", locationId);

            locationSearchService.indexLocation(updatedLocation);
            return updatedLocation;
        } catch (Exception e) {
            logger.error("Error updating location with ID {}: {}", locationId, e.getMessage(), e);
            throw new BadRequestException("Failed to update location: " + e.getMessage());
        }
    }

    @Override
    public void delete(Integer locationId) {
        logger.info("Deleting location with ID: {}", locationId);

        if (!locationRepository.existsById(locationId)) {
            logger.error("Cannot delete location: Location not found with ID: {}", locationId);
            throw new NotFoundException("Location not found with ID: " + locationId);
        }

        // Check if location has child locations
        List<Location> childLocations = locationRepository.findByParent(locationId);
        if (!childLocations.isEmpty()) {
            logger.error("Cannot delete location: It has {} child locations", childLocations.size());
            throw new BadRequestException("Cannot delete location: It has child locations");
        }

        try {
            locationRepository.deleteById(locationId);
            logger.info("Location deleted successfully with ID: {}", locationId);

            locationSearchRepository.deleteById(locationId);
        } catch (Exception e) {
            logger.error("Error deleting location with ID {}: {}", locationId, e.getMessage(), e);
            throw new BadRequestException("Failed to delete location: " + e.getMessage());
        }
    }

    @Override
    public List<Location> findByType(LocationType type) {
        logger.info("Finding locations by type: {}", type);

        try {
            List<Location> locations = locationRepository.findByType(type);
            logger.info("Found {} locations with type: {}", locations.size(), type);
            return locations;
        } catch (Exception e) {
            logger.error("Error finding locations by type {}: {}", type, e.getMessage(), e);
            throw new BadRequestException("Failed to find locations by type: " + e.getMessage());
        }
    }

    @Override
    public List<Location> findByParentId(Integer parentId) {
        logger.info("Finding locations by parent ID: {}", parentId);

        try {
            List<Location> locations = locationRepository.findByParent(parentId);
            logger.info("Found {} locations with parent ID: {}", locations.size(), parentId);
            return locations;
        } catch (Exception e) {
            logger.error("Error finding locations by parent ID {}: {}", parentId, e.getMessage(), e);
            throw new BadRequestException("Failed to find locations by parent ID: " + e.getMessage());
        }
    }

    @Override
    public List<Location> findByNameContaining(String name) {
        logger.info("Finding locations with name containing: {}", name);

        try {
            List<Location> locations = locationRepository.findByNameContaining(name);
            logger.info("Found {} locations with name containing: {}", locations.size(), name);
            return locations;
        } catch (Exception e) {
            logger.error("Error finding locations with name containing {}: {}", name, e.getMessage(), e);
            throw new BadRequestException("Failed to find locations by name: " + e.getMessage());
        }
    }

    @Override
    public boolean existsByName(String name) {
        logger.info("Checking if location exists with name: {}", name);
        return locationRepository.existsByName(name);
    }
}
