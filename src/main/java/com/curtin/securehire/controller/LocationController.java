package com.curtin.securehire.controller;

import com.curtin.securehire.dto.LocationDTO;
import com.curtin.securehire.entity.db.Location;
import com.curtin.securehire.constant.LocationType;
import com.curtin.securehire.service.db.LocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping("/{locationId}")
    public ResponseEntity<Location> findById(@PathVariable Integer locationId) {
        log.info("Entering findById method with locationId: {}", locationId);
        Location location = locationService.findById(locationId);
        log.info("Exiting findById method. Retrieved location: {}", location);
        return ResponseEntity.ok(location);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Location> findByName(@PathVariable String name) {
        log.info("Entering findByName method with name: {}", name);
        Location location = locationService.findByName(name);
        log.info("Exiting findByName method. Retrieved location: {}", location);
        return ResponseEntity.ok(location);
    }

    @GetMapping
    public ResponseEntity<List<Location>> findAll() {
        log.info("Entering findAll method.");
        List<Location> locations = locationService.findAll();
        log.info("Exiting findAll method. Retrieved {} locations.", locations.size());
        return ResponseEntity.ok(locations);
    }

    @PostMapping
    public ResponseEntity<Location> save(@RequestBody LocationDTO location) {
        log.info("Entering save method with location details: {}", location);
        System.out.println("location = ..................................................."+location);
        Location savedLocation = locationService.save(location);
        log.info("Exiting save method. Saved location: {}", savedLocation);
        return ResponseEntity.ok(savedLocation);
    }

    @PutMapping("/{locationId}")
    public ResponseEntity<Location> update(@PathVariable Integer locationId, @RequestBody Location location) {
        log.info("Entering update method with locationId: {} and updated details: {}", locationId, location);
        Location updatedLocation = locationService.update(locationId, location);
        log.info("Exiting update method. Updated location: {}", updatedLocation);
        return ResponseEntity.ok(updatedLocation);
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<Void> delete(@PathVariable Integer locationId) {
        log.info("Entering delete method with locationId: {}", locationId);
        locationService.delete(locationId);
        log.info("Exiting delete method. Location deleted successfully.");
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Location>> findByType(@PathVariable LocationType type) {
        log.info("Entering findByType method with type: {}", type);
        List<Location> locations = locationService.findByType(type);
        log.info("Exiting findByType method. Retrieved {} locations.", locations.size());
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/parent/{parentId}")
    public ResponseEntity<List<Location>> findByParentId(@PathVariable Integer parentId) {
        log.info("Entering findByParentId method with parentId: {}", parentId);
        List<Location> locations = locationService.findByParentId(parentId);
        log.info("Exiting findByParentId method. Retrieved {} locations.", locations.size());
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Location>> findByNameContaining(@RequestParam String name) {
        log.info("Entering findByNameContaining method with name fragment: {}", name);
        List<Location> locations = locationService.findByNameContaining(name);
        log.info("Exiting findByNameContaining method. Retrieved {} locations.", locations.size());
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/exists/{name}")
    public ResponseEntity<Boolean> existsByName(@PathVariable String name) {
        log.info("Entering existsByName method with name: {}", name);
        boolean exists = locationService.existsByName(name);
        log.info("Exiting existsByName method. Location exists: {}", exists);
        return ResponseEntity.ok(exists);
    }
}
